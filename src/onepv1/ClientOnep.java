/*=============================================================================
* ClientOnep.java
* Provides simple use for 1p client layer given CIK.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package onepv1;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/** Description of ClientOnep() 
 * 
 * @throws MyException 		Description of myException
 */
public class ClientOnep extends Onep {

	private String cik_;
	private String rid_;
	private Map<String, String> aliasMap_ = new LinkedHashMap<String, String>();
	
	/** This class object represents a client resource of Exosite One-Platform.
	 * 
	 * @param url			The URL of Exosite One-Platform JSON-RPC service.
	 * @param timeout   	Set time out of request.
	 * @param cik			The cik of the client.
	 */	
	public ClientOnep(String url, int timeout,String cik) {
		super(url, timeout);
		cik_ = cik;
	}
	
	private Result doCreate(String type, String alias, Object desc) throws OneException{
		Result res1 = this.create(cik_, type, desc);
		if (res1.getStatus() == Result.OK){
			String rid  = res1.getMessage();
			Result res2 = this.map(cik_, rid, alias);
			if (res2.getStatus() != Result.OK){
				this.drop(cik_, rid);
				return res2;
			}
			aliasMap_.put(alias, rid);
		}
		return res1;		
	}
	
	private String getRID(String alias) throws OneException{
		if (aliasMap_.containsKey(alias)){
			return aliasMap_.get(alias);
		}
		else{
			Result res = this.lookup(cik_, "alias", alias);
			if (Result.OK == res.getStatus()){
				String rid = res.getMessage();
				aliasMap_.put(alias, rid);
				return rid;
				
			}
			else throw new OnePlatformException(res.getMessage());
		}	
	}
	/** Create resource and name its alias by given description.
	 * 
	 * @param alias			The alias name of resource
	 * @param description  	The description object of type IDescription.
	 * @return				The Result object.
	 */
	public Result create(String alias,IDescription description) throws OneException{
		if (description instanceof DataportDescription){
			return this.doCreate("dataport", alias, ((DataportDescription) description).getMapObject());
		}
		return null;
	}
	
	/** Set client interface key to given cik string.
	 * 
	 * @param cik			The cik string.	 
	 */	
	public void setCIK(String cik) throws OneException{
		cik_ = cik;
		rid_ = null;
		aliasMap_.clear();
		this.getAllAliasesInfo();	
	}
	
	/** Get all aliases information of client resource.
	 * 
	 * @return				The Map of <String alias,String rid>.
	 */	
	public Map<String, String> getAllAliasesInfo() throws OneException {
		Map<String, String> retMap = new LinkedHashMap<String, String>();
		if (null == rid_) {
			Result res = this.lookup(cik_, "alias", "");
			if (Result.OK == res.getStatus()) {
				rid_ = res.getMessage();
			} else
				return retMap;
		}
		Map<String, Object> option = new LinkedHashMap<String, Object>();
		option.put("aliases", true);
		Result res = this.info(cik_, rid_, option);
		if (Result.OK == res.getStatus()) {
			aliasMap_.clear();
			String resMsg = res.getMessage();
			JSONParser parser = new JSONParser();			
			JSONObject readObj = null;
			try {
				readObj = (JSONObject) parser.parse(resMsg);
			} catch (ParseException e) {
				throw new OneException("Unable to decode returned aliases info string.");
			}			
			JSONObject dataMap = (JSONObject) readObj.get("aliases");
			for (Object rid : dataMap.keySet()) {
				JSONArray aliasArray = (JSONArray) dataMap.get(rid);
				if (aliasArray.size() > 0) {
					aliasMap_.put((String) aliasArray.get(0), (String) rid);
					retMap.put((String) aliasArray.get(0), (String) rid);					
				}
			}			
		}
		return retMap;
	}
	
	/** Get information of alias given some options.
	 * 
	 * @param alias			The alias name of resource.
	 * @param options       The options to retrieve the information.
	 * @return				The Result object.
	 */
	public Result info(String alias, Object options) throws OneException{
		String rid = this.getRID(alias);
		Result res = this.info(cik_, rid, options);
		if( res.getStatus() != Result.OK && res.getMessage().equals("restricted")){
			this.getAllAliasesInfo();
		}
		return res;		
	}
	
	/** Read N records of data from given alias.
	 * 
	 * @param alias			The alias name of resource.
	 * @param count         The number of records to read.
	 * @return				The Result object.
	 */
	public Result read(String alias,int count) throws OneException{
		if ( count <= 0 ) count =1;
		String rid = this.getRID(alias);
		Map<String, Object> argu = new LinkedHashMap<String, Object>();
		argu.put("limit",count);
		argu.put("sort","desc");
		Result res = this.read(cik_, rid, argu);
		if( res.getStatus() != Result.OK && res.getMessage().equals("restricted")){
			this.getAllAliasesInfo();
		}
		return res;		
	}
	
	/** Read most recent of data from given alias.
	 * 
	 * @param alias			The alias name of resource.	
	 * @return				The Result object.
	 */
	public Result read(String alias) throws OneException{
		return this.read(alias,1);
	}
	
	/** Update the description content of given alias.
	 * 
	 * @param alias			The alias name of resource.
	 * @param description	The description object of type IDescription.	
	 * @return				The Result object.
	 */
	public Result update(String alias, Object description) throws OneException{
		String rid = this.getRID(alias);
		Result res = this.update(cik_, rid, description);
		if( res.getStatus() != Result.OK && res.getMessage().equals("restricted")){
			this.getAllAliasesInfo();
		}
		return res;		
	}
	
	/** Write data to given alias.
	 * 
	 * @param alias			The alias name of resource.
	 * @param data			The data to be written.	
	 * @return				The Result object.
	 */
	public Result write(String alias, Object data) throws OneException{
		String rid = this.getRID(alias);
		Result res = this.write(cik_, rid, data);
		if( res.getStatus() != Result.OK && res.getMessage().equals("restricted")){
			this.getAllAliasesInfo();
		}
		return res;
	}
	
	/** Write data to multiple aliases simultaneously.
	 * 
	 * @param entries		The data to be written which is of type Map<String alias,Object data>.	 *
	 * @return				The Result object.
	 */
	public Result write( Map<String,Object> entries) throws OneException{
		LinkedList<Object> data = new LinkedList<Object>();
		for( String alias:entries.keySet()){
			String rid = null;
			try{
				rid = this.getRID(alias);				
			}catch(OneException e){
				continue;
			}
			LinkedList<Object> entry = new LinkedList<Object>();
			entry.add(rid);
			entry.add(entries.get(alias));
			data.add(entry);			
		}
		return this.write(cik_, data);
	}	
	
}
