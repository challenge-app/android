package br.com.challengeaccepted.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UserExistsException;
import br.com.challengeaccepted.exception.UserNotFoundException;
import br.com.challengeaccepted.exception.WrongLoginException;
import br.com.challengeaccepted.model.ChallengeModel;
import br.com.challengeaccepted.model.UserModel;
import br.com.challengeaccepted.webservice.WebserviceActions;

public class ChallengeAPI {

	private static final String RESOURCE = "/challenge";
	
	private ChallengeAPI() {}
	
	public static Challenge createChallenge(String description, String senderId,
			String receiverId, String challengeBaseId, int reward)
			 throws NoConnectionException, UserExistsException {

		JSONObject json = new JSONObject();
		try {
			json.put("description", description);
			json.put("senderId", senderId);
			json.put("receiverId", receiverId);
			json.put("challengeBaseId", challengeBaseId);
			json.put("reward", reward);
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT));
			
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				Challenge challenge = ChallengeModel.loadFromJSON(EntityUtils.toString(response.getEntity()));
				return challenge;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY){
			throw new UserExistsException();
		}

		return null;
	}	
	
	public static User login(String email, String password)
			 throws NoConnectionException, UserNotFoundException, WrongLoginException {
		
		String LOGIN_RESOURCE = "/user/auth";

		JSONObject json = new JSONObject();
		try {
			json.put("email", email);
			json.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, LOGIN_RESOURCE, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT));
			
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				User contact = UserModel.loadFromJSON(EntityUtils.toString(response.getEntity()));
				return contact;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY){
			throw new UserNotFoundException();
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			throw new WrongLoginException();
		}

		return null;
	}	
	
}