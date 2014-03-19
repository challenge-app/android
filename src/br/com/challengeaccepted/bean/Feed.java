package br.com.challengeaccepted.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Feed implements Serializable {
	
	private static final long serialVersionUID = -5561056673348050996L;

	@SerializedName("_id")
	private String id;	
	private Challenge challenge;
	private int type;
	private User culprit;
	private ArrayList<User> whoElse;
	private String timestamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Challenge getChallenge() {
		return challenge;
	}
	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public User getCulprit() {
		return culprit;
	}
	public void setCulprit(User culprit) {
		this.culprit = culprit;
	}
	public ArrayList<User> getWhoElse() {
		return whoElse;
	}
	public void setWhoElse(ArrayList<User> whoElse) {
		this.whoElse = whoElse;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}