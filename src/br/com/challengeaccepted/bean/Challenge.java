package br.com.challengeaccepted.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Challenge implements Serializable {

	private static final long serialVersionUID = 9172414403611430871L;

	@SerializedName("_id")
	private String id;

	private ChallengeInfo info;
	
	private User sender;
	private User receiver;
	
	private int reward;
	private int votes;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ChallengeInfo getInfo() {
		return info;
	}
	
	public void setInfo(ChallengeInfo info) {
		this.info = info;
	}
	
	public User getSender() {
		return sender;
	}
	
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public User getReceiver() {
		return receiver;
	}
	
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	public int getReward() {
		return reward;
	}
	
	public void setReward(int reward) {
		this.reward = reward;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void setVotes(int votes) {
		this.votes = votes;
	}	
	
}