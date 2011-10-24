/*=============================================================================
* JsonHandler.java
* Parse and process JSON-RPC request/response string.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package onepv1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

class JsonHandler {
	private JsonHandler() {
	}

	static String getRequest(String parentkey, String proc,	LinkedList<Object> args) {
		Map<String, Object> call = new LinkedHashMap<String, Object>();		
		call.put("id", new Integer(1));
		call.put("procedure", proc);
		call.put("arguments", args);
		ArrayList<Map<String, Object>> calls = new ArrayList<Map<String, Object>>();
		calls.add(call);
		Map<String, Object> auth = new LinkedHashMap<String, Object>();
		auth.put("cik", parentkey);
		Map<String, Object> req = new LinkedHashMap<String, Object>();
		req.put("auth", auth);
		req.put("calls", calls);
		return JSONValue.toJSONString(req);
	}

	static Result parseResponse(String response) throws OnePlatformException {
		Object obj = JSONValue.parse(response);
		JSONArray callRespArray = null;
		try {
			callRespArray = (JSONArray) obj;
		} catch (Exception e) {
			JSONObject callError = (JSONObject) obj;
			String errormsg = callError.get("error").toString();
			throw new OnePlatformException(errormsg);
		}
		JSONObject callRespObj = (JSONObject) callRespArray.get(0);

		if (callRespObj.containsKey("error")) {
			String errormsg = callRespObj.get("error").toString();
			throw new OnePlatformException(errormsg);
		}
		String status = callRespObj.get("status").toString();
		if (Result.OK.equals(status)) {
			if (callRespObj.containsKey("result"))
				return new Result(Result.OK, callRespObj.get("result")
						.toString());
			else
				return new Result(Result.OK, Result.OK);
		} else {
			return new Result(Result.FAIL, status);
		}

	}

}
