package br.com.challengeaccepted.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ChallengeInfo implements Serializable {

	private static final long serialVersionUID = 2342558694930145288L;
	
	@SerializedName("_id")
	private String id;
	private String description;
	private int generalLikes;
	private int generalDoubts;
	private String timestamp;
	private ArrayList<Challenge> challenges;
	private boolean def;
	private int difficulty;
	
	public int getGeneralLikes() {
		return generalLikes;
	}

	public void setGeneralLikes(int generalLikes) {
		this.generalLikes = generalLikes;
	}

	public int getGeneralDoubts() {
		return generalDoubts;
	}

	public void setGeneralDoubts(int generalDoubts) {
		this.generalDoubts = generalDoubts;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
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

	public boolean isDef() {
		return def;
	}

	public void setDef(boolean def) {
		this.def = def;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
}
