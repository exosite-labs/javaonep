/*=============================================================================
* ClientOnepExamples.java
* Use-case examples.
* Note that CIK strings ("PUTA40CHARACTER...") need to be replaced with a valid
* value.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import onepv1.*;

public class ClientOnepExamples {
	public static void main(String[] args){
		String cik = "PUTA40CHARACTERCIKHERE";
		ClientOnep conn = new ClientOnep("http://m2.exosite.com/api:v1/rpc/process", 3, cik);
		
		Random rand = new Random();
		int value = rand.nextInt(100);
		
		//write data to alias
	    try {
	    	Result res = conn.write("X1",value);
	    	if (res.getStatus() == Result.OK){
	    		System.out.println("Data: " + value + " is written.");
	    	}
	    	
		} catch (OneException e) {
			System.out.println(e.getMessage());
		}
		
		// read data from alias
		try {
	    	Result res = conn.read("X1");
	    	if (res.getStatus() == Result.OK){
	    		String read = res.getMessage();
	    		JSONArray dataarr = (JSONArray)JSONValue.parse(read);
	    		JSONArray data1 = (JSONArray)dataarr.get(0);
	    		System.out.println("Data: " + data1.get(1) + " is read.");	    				
	    	}
	    	
		} catch (OneException e) {
			System.out.println(e.getMessage());
		}
		
		// get all aliases information
		try {
			Map<String, String> aliasMap = conn.getAllAliasesInfo();
			for(String alias:aliasMap.keySet()){
				String rid = aliasMap.get(alias);
				System.out.println( "(Alias,RID): (" + alias + " , " + rid + ")" );
			}
		} catch (OneException e) {			
			System.out.println(e.getMessage());
		}
		
		//create dataport
		DataportDescription desc = new DataportDescription("float");
		desc.setName("TEST_name");
		desc.retention.setCount(10);
		desc.retention.setDuration("infinity");
		LinkedList<Object> p1= new LinkedList<Object>();
		p1.add("div");
		p1.add(2);
		desc.preprocess.add(p1);
		try {
			conn.create("TEST_alias", desc);
		} catch (OneException e) {
			System.out.println(e.getMessage());
		}
		
		//drop dataport
		try {
			Result res = conn.lookup(cik, "alias", "TEST_alias");
			if (res.getStatus() == Result.OK){
				String rid = res.getMessage();
				conn.drop(cik, rid);
			}
		} catch (OnePlatformException e) {		
			e.printStackTrace();
		} catch (HttpRPCRequestException e) {		
			e.printStackTrace();
		} catch (HttpRPCResponseException e) {		
			e.printStackTrace();
		} catch (OneException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
