package br.com.challengeaccepted.bean;

import android.net.Uri;

public class S3TaskResult {
	
	String errorMessage = null;
	String url = null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUrl() {
		return url;
	}

	public void setUri(String url) {
		this.url = url;
	}
}
