package br.com.challengeaccepted.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import br.com.challengeaccepted.bean.Feed;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.commons.Session;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;
import br.com.challengeaccepted.model.FeedModel;
import br.com.challengeaccepted.webservice.WebserviceActions;

public class FeedAPI {

	private static final String RESOURCE = "/feed";
	
	private FeedAPI() {}
	
	public static ArrayList<Feed> getReceivedChallenges()
			 throws NoConnectionException, UnauthorizedException {
		
		String auth = Session.getSession().getSessionUser().getAuthenticationToken();
				
		List<NameValuePair> query = new ArrayList<NameValuePair>();

		query.add(new BasicNameValuePair("offset", "0"));
		query.add(new BasicNameValuePair("limit", "10"));

		HttpResponse response = WebserviceActions.doGet(Constants.PROTOCOL, Constants.HOST,
				Constants.PORT, RESOURCE, query,
				APICommons.createHeader(APIHeader.API_VERSIONING),
				APICommons.createHeader(APIHeader.CONTENT_TYPE),
				APICommons.createHeader(APIHeader.API_AUTHENTICATION),
				APICommons.createHeader(APIHeader.USER_AGENT),
				APICommons.createHeader(APIHeader.USER_AUTHENTICATION, auth));
			
		if (response == null)
			throw new NoConnectionException();
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				ArrayList<Feed> feed = FeedModel.parseChallengeArray(EntityUtils.toString(response.getEntity()));
				return feed;
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