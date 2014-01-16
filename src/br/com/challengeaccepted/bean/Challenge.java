package br.com.challengeaccepted.bean;

import java.io.Serializable;

public class Challenge implements Serializable {

	private static final long serialVersionUID = 9172414403611430871L;
	
	private String description;
	private String senderId;
	private String receiverId;
	private String challengeBaseId;
	private int votes;
	private int reward;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getChallengeBaseId() {
		return challengeBaseId;
	}
	public void setChallengeBaseId(String challengeBaseId) {
		this.challengeBaseId = challengeBaseId;
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