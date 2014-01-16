package br.com.challengeaccepted.api;

import br.com.challengeaccepted.commons.Constants;

public enum APIHeader {

	API_VERSIONING ("Accept", Constants.API_VERSION),
	CONTENT_TYPE ("Content-Type", Constants.CONTENT_TYPE_JSON),
	API_AUTHENTICATION("Authorization", Constants.API_KEY_AUTHORIZATION),
	USER_AUTHENTICATION("X-AUTH-TOKEN"),
	GEOLOCATION("Geolocation"),
	USER_AGENT("User-agent", Constants.USER_AGENT);
	
	private final String headerName;
	private final String headerValue;
	
	APIHeader(String headerName, String headerValue) {
		this.headerName = headerName;
		this.headerValue = headerValue;
	}
	
	APIHeader(String headerName) {
		this(headerName, null);
	}
	
	public String getHeaderName() {
		return this.headerName;
	}
	
	public String getHeaderValue() {
		return this.headerValue;
	}
}