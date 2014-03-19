package br.com.challengeaccepted.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class Challenge implements Serializable {

	private static final long serialVersionUID = 9172414403611430871L;

	@SerializedName("_id")
	private String id;
	@SerializedName("info")
	private ChallengeInfo info;
	private User sender;
	private User receiver;
	private String url;
	private String type;
	private int reward;
	private int likes;
	private int doubts;
	private int status;				// -1: not seen; 0: seen; 1: done; 2: refused
	private String timestamp;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDoubts() {
		return doubts;
	}

	public void setDoubts(int doubts) {
		this.doubts = doubts;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}	
	
}