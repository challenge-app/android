package br.com.challengeaccepted.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ChallengeInfo implements Serializable {

	private static final long serialVersionUID = 9172411203611430871L;

	@SerializedName("_id")
	private String id;
	private String description;
	
	private ArrayList<Challenge> challenges;
	
	private int generalVotes;

	public String getDescription() {
		return description;
	}
	
	public int getGeneralVotes() {
		return generalVotes;
	}

	public String getId() {
		return id;
	}
	
	public ArrayList<Challenge> getChallenges()
	{
		return challenges;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setGeneralVotes(int generalVotes)
	{
		this.generalVotes = generalVotes;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setChallenges(ArrayList<Challenge> challenges)
	{
		this.challenges = challenges;
	}
	
}
