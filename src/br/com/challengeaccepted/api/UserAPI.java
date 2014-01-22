package br.com.challengeaccepted.api;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.commons.Session;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;
import br.com.challengeaccepted.exception.UserExistsException;
import br.com.challengeaccepted.exception.UserNotFoundException;
import br.com.challengeaccepted.exception.WrongLoginException;
import br.com.challengeaccepted.model.UserModel;
import br.com.challengeaccepted.webservice.WebserviceActions;

public class UserAPI {

	private static final String RESOURCE = "/user";
	private static final String RESOURCE_FRIEND = "/user/friend";
	private static final String RESOURCE_FRIENDS = "/user/friends";
	private static final String RESOURCE_FIND = "/user/find";
	private static final String RESOURCE_AUTH = "/user/auth";
	
	private UserAPI() {}
	
	public static ArrayList<User> getFriends()
			throws NoConnectionException, UnauthorizedException {

		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
		
		HttpResponse response = WebserviceActions.doGet(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE_FRIENDS, null,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));

		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				User user = UserModel.loadFromJSON(EntityUtils.toString(response.getEntity()));
				return user.getFriends();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
			throw new UnauthorizedException();
		}

		return null;
	}
	
	public static ArrayList<User> getUserByEmail(String email)
			throws NoConnectionException, UserNotFoundException {
		
		JSONObject json = new JSONObject();
		
		try {
			json.put("email", email);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE_FIND, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT));

		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				ArrayList<User> result = UserModel.parseContactArray(EntityUtils.toString(response.getEntity()));
				return result;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
			throw new UserNotFoundException();
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			return null;
		}
		
		return null;
	}
	
	public static ArrayList<User> addFriend(String id)
			throws NoConnectionException, UserNotFoundException {
		
		JSONObject json = new JSONObject();
		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
		
		try {
			json.put("_id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE_FRIEND, json,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));

		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				User result = UserModel.loadFromJSON(EntityUtils.toString(response.getEntity()));
				return result.getFriends();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
			// TODO: login
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
			return null;
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
			throw new UserNotFoundException();
		}
		
		return null;
	}
		
	public static User register(String email, String password)
			 throws NoConnectionException, UserExistsException {

		JSONObject json = new JSONObject();
		try {
			json.put("email", email);
			json.put("password", password);
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
				User contact = UserModel.loadFromJSON(EntityUtils.toString(response.getEntity()));
				return contact;
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

		JSONObject json = new JSONObject();
		try {
			json.put("email", email);
			json.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		HttpResponse response = WebserviceActions.doPost(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE_AUTH, json,
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