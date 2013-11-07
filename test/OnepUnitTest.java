/*============================================================================= * OnepUnitTest.java * Unit Tests for Onep class.  * Note that CIK strings ("PUTA40CHARACTER...") need to be replaced with a valid * value.  *============================================================================== * * Tested with JDK 1.6 * * Copyright (c) 2011, Exosite LLC * All rights reserved.  */ import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import onepv1.EmptyOption;
import onepv1.HttpRPCRequestException;
import onepv1.HttpRPCResponseException;
import onepv1.OneException;
import onepv1.OnePlatformException;
import onepv1.Onep;
import onepv1.Result;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OnepUnitTest extends TestCase {
	private String cik_ = null;
	private String rid_ = null;
	private Onep conn_ = null;

	public static Test suite() {
		return new TestSuite(OnepUnitTest.class);
	}

	protected void setUp() {
        // NOTE: The easiest way to get a Client Interface Key (CIK - "clientKey") 
        // the first time is probably from Exosite Portals https://portals.exosite.com 
		this.cik_ = "PUTA40CHARACTERCIKHERE";
        // NOTE: Use Onep.listing() to get a client's RIDs
		this.rid_ = "PUTA40CHARACTERRIDHERE";
		this.conn_ = new Onep("http://m2.exosite.com/api:v1/rpc/process", 3);
	}

	protected void tearDown() {
	}
	
	public void testCreateDrop() throws OneException{
		Map<String, Object> desc = getDescObject();
		Result res = conn_.create(cik_, "dataport", desc);
		assertEquals(Result.OK, res.getStatus());
		String rid = res.getMessage();
		res = conn_.drop(cik_,rid);
		assertEquals(Result.OK, res.getStatus());
	}
	
	public void testFlush() throws OneException{
		Result res = conn_.flush(cik_, rid_);
		assertEquals(Result.OK, res.getStatus());
		res = conn_.read(cik_, rid_,EmptyOption.getInstance());
		assertEquals(Result.OK, res.getStatus());
		assertEquals("[]", res.getMessage());
	}
		
	public void testInfo() throws OneException{
		Result res = conn_.info(cik_, rid_,EmptyOption.getInstance());
		assertEquals(Result.OK, res.getStatus());
		//String infomsg = res.getMessage();
	}
	
	public void testListing() throws OneException{
		LinkedList<Object> options = new LinkedList<Object>();
		options.add("dataport");
		Result res = conn_.listing(cik_, options);
		assertEquals(Result.OK, res.getStatus());
		//String listingmsg = res.getMessage();
	}
	
	public void testLookup() throws OneException{		
		Result res = conn_.lookup(cik_, "alias", "X1");
		assertEquals(Result.OK, res.getStatus());
		assertEquals(rid_, res.getMessage());
	}
	
	public void testMapUnmap() throws OneException{
		Map<String, Object> desc = getDescObject();
		//create
		Result res = conn_.create(cik_, "dataport", desc);
		assertEquals(Result.OK, res.getStatus());
		String rid = res.getMessage();
		String alias = "test";
		//map
		res = conn_.map(cik_, rid, alias);
		assertEquals(Result.OK, res.getStatus());
		//lookup
		res = conn_.lookup(cik_, "alias", alias);
		assertEquals(Result.OK, res.getStatus());		
		//unmap
		res = conn_.unmap(cik_, alias);
		assertEquals(Result.OK, res.getStatus());
		res = conn_.lookup(cik_, "alias", alias);
		assertEquals("invalid", res.getMessage());		
		//drop
		res = conn_.drop(cik_, rid);
		assertEquals(Result.OK, res.getStatus());
	}
	
	public void testReadWrite() throws OneException{
		int value = 67;
		Result res = conn_.write(cik_,rid_,value);
		assertEquals(Result.OK, res.getStatus());
		Map<String, Object> argu = new LinkedHashMap<String, Object>();
		argu.put("limit",1);
		argu.put("sort","desc");
		res = conn_.read(cik_, rid_, argu);
		assertEquals(Result.OK, res.getStatus());
		String resmsg = res.getMessage();
		Object obj = JSONValue.parse(resmsg);
		JSONArray readArray = null;
		try {
			readArray = (JSONArray) obj;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		JSONArray readEntry = (JSONArray) readArray.get(0);
		assertEquals(value, Integer.parseInt(readEntry.get(1).toString()));	
	}
	
	public void testRecord()  throws OneException{
		int value1 = 1, value2 = 2;
		int offset1 = -2, offset2 = -1;
	    LinkedList<Object> data1 = new LinkedList<Object>();
	    data1.add(offset1);
	    data1.add(value1);
	    LinkedList<Object> data2 = new LinkedList<Object>();
	    data2.add(offset2);
	    data2.add(value2);
	    LinkedList<Object> entries = new LinkedList<Object>();
	    entries.add(data1);
	    entries.add(data2);
	    Result res = conn_.record(cik_,rid_,entries,EmptyOption.getInstance());
	    assertEquals(Result.OK, res.getStatus());		
	}	
	
	public void testUpdate() throws OneException{
		Map<String, Object> description = new LinkedHashMap<String, Object>();
		description.put("name","my_new_name");
		LinkedList<Object> p1 = new LinkedList<Object>();
		p1.add("add");
		p1.add(0);
		LinkedList<Object> preprocess = new LinkedList<Object>();
		preprocess.add(p1);
		description.put("preprocess",preprocess);
		Result res = conn_.update(cik_,rid_,description);
		assertEquals(Result.OK, res.getStatus());	
	}
	
	public void testRequestException() throws OneException{
		try{
			conn_ = new Onep("http://0.0.0.1/api:v1/rpc/process",3);
			conn_.lookup(cik_,"alias","X1");
			fail("Failed to throw HttpRPCRequestException.");
	    }catch(HttpRPCRequestException e){
	    	assertTrue(true);
	    }
	}
	
	public void testResponseException() throws OneException{
		try{
			conn_ = new Onep("http://m2.exosite.com/api:v1/rpc/processes",3);
			conn_.lookup(cik_,"alias","X1");
			fail("Failed to throw HttpRPCResponseException.");
	    }catch(HttpRPCResponseException e){
	    	assertTrue(true);
	    }	   
	}
	
	public void testInvalidAuth() throws OneException{
		try{
			String cik = "PUTA40CHARACTERWRONGCIKHERE";
			conn_.lookup(cik,"alias","X1");
			fail("Failed to throw OnePlatformException.");
	    }catch(OnePlatformException e){
	    	assertTrue(true);
	    }
	}

	private Map<String, Object> getDescObject(){			
		Map<String, Object> desc = new LinkedHashMap<String, Object>();
		desc.put("format","integer");
		desc.put("name","testcreate");
		desc.put("visibility","parent");
		Map<String, Object> retention = new LinkedHashMap<String, Object>();
		retention.put("count","infinity");
		retention.put("duration","infinity");
		desc.put("retention",retention);
		LinkedList<Object> p1 = new LinkedList<Object>();
		p1.add("mul");
		p1.add(3);
		LinkedList<Object> preprocess = new LinkedList<Object>();
		preprocess.add(p1);		
		desc.put("preprocess",preprocess);
		return desc;
	}	

}
