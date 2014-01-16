package br.com.challengeaccepted.model;

import java.util.ArrayList;

import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.User;

import com.google.gson.Gson;

public class ChallengeModel {
	
	private ChallengeModel() {}
	
	public static Challenge loadFromJSON(String jsonString) {
		Gson gson = new Gson();
		Challenge challenge = gson.fromJson(jsonString, Challenge.class);
		return challenge;
	}
	
	public static ArrayList<Challenge> parseContactArray(String jsonString) {
		Gson gson = new Gson();
		Challenge[] challenges = gson.fromJson(jsonString, Challenge[].class);
		
		ArrayList<Challenge> challengeList = new ArrayList<Challenge>();
		for (Challenge c : challenges) {
			challengeList.add(c);
		}
		return challengeList;
	}
}