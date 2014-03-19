package br.com.challengeaccepted.api;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.ChallengeInfo;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.commons.Session;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;
import br.com.challengeaccepted.model.ChallengeInfoModel;
import br.com.challengeaccepted.model.ChallengeModel;
import br.com.challengeaccepted.webservice.WebserviceActions;

public class ChallengeAPI {

	private static final String RESOURCE = "/challenge";
	private static final String RESOURCE_RECEIVED = "/challenge/received";
	private static final String RESOURCE_ACCEPT = "/challenge/accept";
	
	private ChallengeAPI() {}
	
	public static Challenge createChallenge(String challengeId, String type, String receiverId, int reward)
			 throws NoConnectionException, UnauthorizedException {

		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("baseId", challengeId);
			json.put("type", type);
			json.put("receiverId", receiverId);
			json.put("reward", reward);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
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
			throw new UnauthorizedException();
		}

		return null;
	}	
	
	public static Challenge createCustomChallenge(String description, String type, String receiverId, int reward)
			 throws NoConnectionException, UnauthorizedException {

		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("description", description);
			json.put("type", type);
			json.put("receiverId", receiverId);
			json.put("reward", reward);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
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
			throw new UnauthorizedException();
		}

		return null;
	}	
	
	public static ArrayList<Challenge> sentChallenges()
			 throws NoConnectionException, UnauthorizedException {
		
		String GET_CHALLENGES_RESOURCE = "/challenge/sent";
		
		String auth = Session.getSession().getSessionUser().getAuthenticationToken();

		HttpResponse response = WebserviceActions.doGet(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, GET_CHALLENGES_RESOURCE, null,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				ArrayList<Challenge> challenges = ChallengeModel.parseChallengeArray(EntityUtils.toString(response.getEntity()));
				return challenges;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY){
			throw new UnauthorizedException();
		}

		return null;
	}	
	
	public static ArrayList<Challenge> getReceivedChallenges()
			 throws NoConnectionException, UnauthorizedException {
		
		String auth = Session.getSession().getSessionUser().getAuthenticationToken();

		HttpResponse response = WebserviceActions.doGet(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE_RECEIVED, null,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				ArrayList<Challenge> challenges = ChallengeModel.parseChallengeArray(EntityUtils.toString(response.getEntity()));
				return challenges;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY){
			throw new UnauthorizedException();
		}

		return null;
	}	
	
	public static ArrayList<ChallengeInfo> getRandomChallenges()
			 throws NoConnectionException, UnauthorizedException {
		
		String GET_CHALLENGES_RESOURCE = "/challenge/random";
		
		String auth = Session.getSession().getSessionUser().getAuthenticationToken();

		HttpResponse response = WebserviceActions.doGet(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, GET_CHALLENGES_RESOURCE, null,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				ArrayList<ChallengeInfo> challenges = ChallengeInfoModel.parseChallengeArray(EntityUtils.toString(response.getEntity()));
				return challenges;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY){
			throw new UnauthorizedException();
		}

		return null;
	}	
	
	public static Challenge acceptChallenge(String challengeId, String url)
			 throws NoConnectionException, UnauthorizedException {

		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("challengeId", challengeId);
			json.put("url", url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE_ACCEPT, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
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
			throw new UnauthorizedException();
		}

		return null;
	}	
	
}