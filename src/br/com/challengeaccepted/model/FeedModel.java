package br.com.challengeaccepted.model;

import java.util.ArrayList;

import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.Feed;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class FeedModel {
	
	private FeedModel() {}
	
	public static Feed loadFromJSON(String jsonString) {
		Gson gson = new Gson();
		System.out.println(jsonString);
		Feed feed = gson.fromJson(jsonString, Feed.class);
		return feed;
	}
	
	public static ArrayList<Feed> parseChallengeArray(String jsonString) {
		Gson gson = new Gson();
	    JsonParser parser = new JsonParser();
	    JsonArray jArray = parser.parse(jsonString).getAsJsonArray();

	    ArrayList<Feed> lcs = new ArrayList<Feed>();

	    for(JsonElement obj : jArray )
	    {
	    	Feed cse = gson.fromJson( obj , Feed.class);
	        lcs.add(cse);
	    }
	    
	    return lcs;
	}
}