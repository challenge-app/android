package br.com.challengeaccepted.model;

import java.util.ArrayList;

import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.ChallengeInfo;

import com.google.gson.Gson;

public class ChallengeInfoModel {
	
	private ChallengeInfoModel() {}
	
	public static ChallengeInfo loadFromJSON(String jsonString) {
		Gson gson = new Gson();
		System.out.println(jsonString);
		ChallengeInfo challenge = gson.fromJson(jsonString, ChallengeInfo.class);
		return challenge;
	}
	
	public static ArrayList<ChallengeInfo> parseChallengeArray(String jsonString) {
		Gson gson = new Gson();
		System.out.println(jsonString);
		ChallengeInfo[] challenges = gson.fromJson(jsonString, ChallengeInfo[].class);
		
		ArrayList<ChallengeInfo> challengeList = new ArrayList<ChallengeInfo>();
		for (ChallengeInfo c : challenges) {
			challengeList.add(c);
		}
		return challengeList;
	}
}