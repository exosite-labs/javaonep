/*=============================================================================
* ITransport.java
* Transport tool interface - e.g. HTTP, XMPP.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package onepv1;

interface ITransport {
	String send(String request) throws HttpRPCRequestException,	HttpRPCResponseException;
}
