package br.com.challengeaccepted.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.challengeaccepted.bean.User;

public class BancoCreate {

	private SQLiteDatabase banco; 
	private Sqlite helper;
    private String SQL;
    private Context ctx;


	public BancoCreate(Context context) 
	{
		helper = new Sqlite(context);
		ctx = context;
		
		this.banco = helper.getWritableDatabase();
		banco.close();
		helper.close();
	}
 
	
	public void setFriends(User user) {
		
		ArrayList<User> friends = user.getFollowing();
		ArrayList<String> friendsEmails = new ArrayList<String>();
		int i = 0;
		
		for (i = 0; i < friends.size() ; i++) {
			friendsEmails.add(friends.get(i).getEmail());
		}
		
		//Abre banco
		banco = helper.getWritableDatabase();
	
		for (String email : friendsEmails) {
			
			ContentValues ctv = new ContentValues();
			ctv.put("USER_EMAIL", user.getEmail());
			ctv.put("FRIEND_EMAIL", email);
			banco.insertWithOnConflict("FRIENDS", null, ctv, SQLiteDatabase.CONFLICT_REPLACE);
		}
		
		banco.close();

	}

	
	public ArrayList<String> getFriends(String userEmail) {
    	ArrayList<String> list = new ArrayList<String>();
 
        SQLiteDatabase db = helper.getWritableDatabase();
        String SQL;
        
        SQL = "SELECT FRIEND_EMAIL FROM FRIENDS WHERE USER_EMAIL = \'" + userEmail + "\'";
        
        Cursor cursor = db.rawQuery(SQL, null);
        
        if (cursor.moveToFirst()) {
            do {
            	String email = cursor.getString(cursor.getColumnIndex("FRIEND_EMAIL"));
                list.add(email);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
        
        return list;
    }
	
	public ArrayList<String> getFriends(String userEmail, String query) {
    	ArrayList<String> list = new ArrayList<String>();
 
        SQLiteDatabase db = helper.getWritableDatabase();
        String SQL;
        
        SQL = "SELECT FRIEND_EMAIL FROM FRIENDS WHERE USER_EMAIL = \'" + userEmail
        		+ "\' AND FRIEND_EMAIL LIKE \'" + query + "%\'";
        
        Cursor cursor = db.rawQuery(SQL, null);
        
        if (cursor.moveToFirst()) {
            do {
            	String email = cursor.getString(cursor.getColumnIndex("FRIEND_EMAIL"));
                list.add(email);
            } while (cursor.moveToNext());
        }
 
        cursor.close();
        db.close();
        
        return list;
    }
}
