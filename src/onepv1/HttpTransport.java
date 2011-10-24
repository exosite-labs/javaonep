/*=============================================================================
* HttpTransport.java
* HTTP-based JSON-RPC request call.
*==============================================================================
*
* Tested with JDK 1.6
*
* Copyright (c) 2011, Exosite LLC
* All rights reserved.
*/

package onepv1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpTransport implements ITransport {
	private String url_;
	private int timeout_;

	HttpTransport(String url, int timeout) {
		this.url_ = url;
		this.timeout_ = timeout;
	}

	public String send(String request) throws HttpRPCRequestException, HttpRPCResponseException {
		URL url = null;
		HttpURLConnection conn = null;
		OutputStreamWriter writer = null;
		StringBuffer response = new StringBuffer();
		try {
			url = new URL(this.url_);
		} catch (MalformedURLException ex) {
			throw new HttpRPCRequestException("Malformed URL.");
		}
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type",
					"application/json; charset=utf-8");
			conn.setRequestProperty("Content-Length", "" + request.length());
			conn.setConnectTimeout(this.timeout_ * 1000);
			try {
				writer = new OutputStreamWriter(conn.getOutputStream());
				writer.write(request);
				writer.flush();
			} catch (IOException e) {
				throw new HttpRPCRequestException(
						"Failed to make http request.");
			} finally {
				if (null != writer)
					writer.close();
			}
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(conn
						.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
			} catch (IOException e) {
				throw new HttpRPCResponseException(
						"Failed to get http response.");
			} finally {
				if (null != reader)
					reader.close();
			}
		} catch (IOException e) {
			throw new HttpRPCRequestException(
					"Failed to open/close url connection.");
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return response.toString();
	}
}
