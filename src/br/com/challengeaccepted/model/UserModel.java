package br.com.challengeaccepted.model;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.com.challengeaccepted.bean.User;

import com.google.gson.Gson;

public class UserModel {

	private static String PREF_USER_DATA = "user_data";
	
	private UserModel() {}
	
	public static User loadUserSaved(Context context) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String userData = pref.getString(PREF_USER_DATA, null);
		User user = new User();
		if (userData != null) {
			Gson gson = new Gson();
			user = gson.fromJson(userData, User.class);
		}
		return user;
	}
	
	public static void saveUser(User user, Context context) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		Gson gson = new Gson();
		editor.putString(PREF_USER_DATA, gson.toJson(user));
		editor.commit();
	}
	
	public static void removeSavedUser(Context context) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.remove(PREF_USER_DATA);
		editor.commit();
	}
	
	public static void clearCache(Context context){
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.clear();
		editor.commit();
	}
	
	public static User loadFromJSON(String jsonString) {
		System.out.println(jsonString);
		Gson gson = new Gson();
		User contact = gson.fromJson(jsonString, User.class);
		return contact;
	}
	
	public static ArrayList<User> parseContactArray(String jsonString) {
		Gson gson = new Gson();
		User[] contacts = gson.fromJson(jsonString, User[].class);
		
		ArrayList<User> contactList = new ArrayList<User>();
		for (User c : contacts) {
			contactList.add(c);
		}
		return contactList;
	}
}