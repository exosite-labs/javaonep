/*=============================================================================
* Onep.java
* Binding class for One Platform API procedures.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package onepv1;

import java.util.LinkedList;

public class Onep {
	private ITransport transport_;

	/** This class represents a binding library of One Platform API.
	 * 
	 * @param url			The URL of Exosite One-Platform JSON-RPC service.
	 * @param timeout   	Set time out of request
	 */	
	public Onep(String url, int timeout) {
		transport_ = new HttpTransport(url, timeout);
	}
	
	private Result callRPC(String clientkey, String proc, LinkedList<Object> options)
	        throws OneException {
		String request = JsonHandler.getRequest(clientkey, proc, options);		
		String response = transport_.send(request);		
		return JsonHandler.parseResponse(response);
	}
	
	/** Given an activation code, the associated entity is activated for the calling Client.
	 * 
	 * @param clientkey		The cik of client.
	 * @param codetype  	Could be "client" or "dispatch" or "share"
	 * @param code			Activation code.
	 * @return				The Result Object
	 */	
	
	public Result activate(String clientkey,String codetype, String code)
	        throws OneException{
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(codetype);
		argu.add(code);
		return callRPC(clientkey, "activate", argu);
	}

	/**Create a One Platform resource of specified Type and Description or Create a clone from an existing One Platform resource.
	 * 
	 * @param clientkey		The cik of client.
	 * @param type  		Could be "clone" or "client" or "dataport"or "datarule" or "dispatch"
	 * @param description	The description object of resource to be created.
	 * @return				The Result Object
	 */	
	public Result create(String clientkey,String type,Object description)
	        throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(type);
		argu.add(description);		
		return callRPC(clientkey,"create",argu);
	}
	
	/** Given an activation code, the associated entity is deactivated for the calling Client.
	 * 
	 * @param clientkey		The cik of client.
	 * @param codetype  	Could be "client" or "dispatch" or "share"
	 * @param code			Activation code.
	 */	
	public Result deactivate(String clientkey,String codetype, String code)
	        throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(codetype);
		argu.add(code);
		return callRPC(clientkey, "deactivate", argu);
	}
	
	/** Drop specified and all associated resources and aliases.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource id to be dropped.
	 * @return	 			The Result object.
	 */	
	public Result drop(String clientkey,String rid)
	        throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);		
		return callRPC(clientkey,"drop",argu);
	}

	/** Empty specified resource of all data.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource id to be emptied.
	 * @return	 			The Result object.
	 */	
	public Result flush(String clientkey, String rid)
	        throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);		
		return callRPC(clientkey, "flush", argu);
	}
	
	/** Provide creation and usage information of specified resource according to the
	 *  specified Options (eg, Options could specify to only return the Description).
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource id.
	 * @param options       Options to retrieve information.		
	 * @return	 			The Result object.
	 */	
	public Result info(String clientkey, String rid, Object options)
            throws OneException {
        LinkedList<Object> argu = new LinkedList<Object>();
        argu.add(rid);
        argu.add(options);
        return callRPC(clientkey, "info", argu);
	}

	/** List all resourceids of given type(s).
	 *  
	 * @param clientkey		The cik of client.
	 * @param types         Lists of ["client" | "dataport" | "datarule" | "dispatch"...]		
	 * @return	 			The Result object.
	 */	
	public Result listing(String clientkey, Object types)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(types);		
		return callRPC(clientkey, "listing", argu);
	}
	
	/** Lookup an aliased resource, or a resource's owner.
	 * 
	 * @param clientkey		The cik of client.
	 * @param type		  	Could be "alias" or "owner".
	 * @param mapping       Could be alias or Resource ID.		
	 * @return	 			The Result object.
	 */	
	public Result lookup(String clientkey, String type, String mapping)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(type);
		argu.add(mapping);
		return callRPC(clientkey, "lookup", argu);
	}	

	/** Give the specified ResourceID an alias. Subsequently the ResourceID can be looked up using the "lookup" method.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.
	 * @param alias         The alias name of the resource.		
	 * @return	 			The Result object.
	 */
	public Result map(String clientkey, String rid, String alias)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add("alias");
		argu.add(rid);
		argu.add(alias);
		return callRPC(clientkey, "map", argu);
	}	

	/** Read data from the specified resource(s).
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.
	 * @param options       Options of read data.		
	 * @return	 			The Result object.
	 */
	public Result read(String clientkey, String rid, Object options)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);
		argu.add(options);
		return callRPC(clientkey, "read", argu);
	}	

	/** Records a list of historical entries to the resource specified.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.
	 * @param entries	    The array data of format [[Timestamp ,Value],...]
	 * @param options       Options of read data.		
	 * @return	 			The Result object.
	 */
	public Result record(String clientkey, String rid, Object entries, Object options)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);
		argu.add(entries);
		argu.add(options);
		return callRPC(clientkey, "record", argu);
	}	

	/** Given an Activation Code, the associated entity is revoked after which the Activation Code can no longer be used.
	 * 
	 * @param clientkey		The cik of client.
	 * @param codetype  	Could be "client" or "dispatch" or "share"
	 * @param code			Activation code.
	 * @return				The Result Object
	 */	
	public Result revoke(String clientkey, String codetype, String code)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(codetype);
		argu.add(code);
		return callRPC(clientkey, "revoke", argu);
	}	

	/** Generates a share code for the given ResourceID.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.	 
	 * @param options       Options of share data.		
	 * @return	 			The Result object.
	 */
	public Result share(String clientkey, String rid, Object options)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);
		argu.add(options);
		return callRPC(clientkey, "share", argu);
	}	

	/** Unmap the given Alias.
	 * 
	 * @param clientkey		The cik of client.
	 * @param alias		  	Alias name to be unmapped.		
	 * @return	 			The Result object.
	 */
	public Result unmap(String clientkey, String alias)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add("alias");
		argu.add(alias);
		return callRPC(clientkey, "unmap", argu);
	}	

	/** Update the description content for the given ResourceID.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.	 
	 * @param description   Updated description content.		
	 * @return	 			The Result object.
	 */
	public Result update(String clientkey, String rid, Object description)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);
		argu.add(description);
		return callRPC(clientkey, "update", argu);
	}
	
	/**  Writes a single value to the resource specified.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.
	 * @param value		  	Data value to be written.	 
	 * @param options       Options of write data(Currently unused).		
	 * @return	 			The Result object.
	 */
	public Result write(String clientkey, String rid, Object value,Object options)
			throws OneException {
		LinkedList<Object> argu = new LinkedList<Object>();
		argu.add(rid);
		argu.add(value);
		argu.add(options);
		return callRPC(clientkey, "write", argu);
	}	

	/** Writes a single value to the resource specified.
	 * 
	 * @param clientkey		The cik of client.
	 * @param rid		  	Resource ID.
	 * @param value		  	Data value to be written.	
	 * @return	 			The Result object.
	 */
	public Result write(String clientkey, String rid, Object value)
			throws OneException {
		return write(clientkey,rid,value,EmptyOption.getInstance());		
	}	
}
