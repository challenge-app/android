package br.com.challengeaccepted.commons;

import android.content.Context;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.model.UserModel;

public class Session {

	private static Session instance;
	private User sessionUser;
	
	private Session() {
	}
	
	public static Session getSession() {
		if (instance == null) {
			instance = new Session();
		}
		
		return instance;
	}
	
	public User loadSessionUser(Context context) {
		this.sessionUser = UserModel.loadUserSaved(context);
		return this.sessionUser;
	}
	
	public User getSessionUser() {
		return this.sessionUser;
	}

	
	public boolean loggedIn() {
		return (getSessionUser() != null) && (getSessionUser().getId() != null);
	}
	
	
	
	public void login(User user, Context context) {
		UserModel.saveUser(user, context);
		Session.getSession().loadSessionUser(context);
	}
	
	public void logout(Context context) {
		UserModel.clearCache(context);
		UserModel.removeSavedUser(context);
		Session.getSession().loadSessionUser(context);
	}


}