/*=============================================================================
* EmptyOption.java
* For empty option in method call.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package onepv1;

import java.util.LinkedHashMap;
import java.util.Map;

public class EmptyOption {
	private static Map<String, Object> instance_ = null;

	private EmptyOption() {
	}

	public static Map<String, Object> getInstance() {
		if (instance_ == null) {
			instance_ = new LinkedHashMap<String, Object>();
		}
		return instance_;
	}

}
