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

public class FriendAPI {

	private static final String RESOURCE = "/user/friends";
	
	private FriendAPI() {}
	
	public static ArrayList<User> getFriends()
			 throws NoConnectionException, UnauthorizedException {
		
		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
		
		HttpResponse response = WebserviceActions.doGet(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE, null,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
					
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				User contact = UserModel.loadFromJSON(EntityUtils.toString(response.getEntity()));
				return contact.getFriends();
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
	
}