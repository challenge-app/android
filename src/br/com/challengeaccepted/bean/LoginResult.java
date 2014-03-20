package br.com.challengeaccepted.bean;


public class LoginResult {
	
	String errorMessage;
	User result;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public User getResult() {
		return result;
	}

	public void setResult(User result) {
		this.result = result;
	}
}
