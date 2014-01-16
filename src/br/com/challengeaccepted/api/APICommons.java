package br.com.challengeaccepted.api;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class APICommons {

	private APICommons() {}
	
	public static Header createHeader(APIHeader headerType) {
		String headerValue = headerType.getHeaderValue();
		if(headerValue == null) {
			headerValue = "";
		}
		return new BasicHeader(headerType.getHeaderName(), headerValue);
	}
	
	public static Header createHeader(APIHeader headerType, String headerValue) {
		return new BasicHeader(headerType.getHeaderName(), headerValue);
	}
}