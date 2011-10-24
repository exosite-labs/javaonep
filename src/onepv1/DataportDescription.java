/*=============================================================================
* DataportDescription.java
* Description class used by client to create dataport.
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

public class DataportDescription implements IDescription{
	
	public class Retention {

		private Object count_ = null;
		private Object duration_ = null;

		public void setCount(Object count) {
			count_ = count;
		}
		public Object getCount() {
			return count_;
		}
		public void setDuration(Object duration) {
			duration_ = duration;
		}
		public Object getDuration() {
			return duration_;
		}
	}
	
	public DataportDescription(String format){
		format_ = format;
		retention = new Retention();
		preprocess = new  LinkedList<Object>();
	}
	
	private String format_ = null;
	private String name_ = null;
	private String subscribe_ = null;
	private String visibility_ = null;
	
	public LinkedList<Object> preprocess = null;
	public Retention retention = null;
	
	public void setFormat(String format){
		format_ = format;
	}	
	
	public void setName(String name){
		name_ = name;
	}
	 
	public void setSubscribe(String subscribe){
		subscribe_ = subscribe;
	}	
	
	public void setVisibility(String visibility){
		visibility_ = visibility;
	}
	
	public Map<String,Object> getMapObject(){
		Map<String, Object> desc = new LinkedHashMap<String, Object>();
		if ( format_ != null ) desc.put("format",format_);
		if ( name_ != null ) desc.put("name",name_);
		if ( subscribe_ != null ) desc.put("subscribe",subscribe_);
		if ( visibility_ != null ) desc.put("visibility",visibility_);
		Map<String, Object> retentionObj = new LinkedHashMap<String, Object>();
		boolean hasElement = false;
		if( retention.getCount() != null ) {
			retentionObj.put("count",retention.getCount());
			hasElement = true;
		}
		if( retention.getDuration() != null ) {
			retentionObj.put("duration",retention.getDuration());
			hasElement = true;
		}		
		if (hasElement){
			desc.put("retention",retentionObj);
		}		
		if ( preprocess.size() > 0 ){
			desc.put("preprocess",preprocess);
		}		
		return desc;		
	}
}
