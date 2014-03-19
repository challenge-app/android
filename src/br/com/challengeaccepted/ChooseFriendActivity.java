package br.com.challengeaccepted;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import br.com.challengeaccepted.adapter.FriendsAdapter;
import br.com.challengeaccepted.api.UserAPI;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.commons.Session;
import br.com.challengeaccepted.db.BancoCreate;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;

public class ChooseFriendActivity extends ActionBarActivity {
	
	private ListView friendList;
	private Button btnSendChallenge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initLayout();
		
//		BancoCreate bd = new BancoCreate(ChooseFriendActivity.this);
//		ArrayList<String> friendsEmails = bd.getFriends(Session.getSession().getSessionUser().getEmail());
//		ArrayList<User> friends = new ArrayList<User>();
//		int i = 0;
		
//		for (i = 0; i < friendsEmails.size(); i++){
//			User user = new User();
//			user.setEmail(friendsEmails.get(i));
//			friends.add(user);
//		}
//		friendList.setAdapter(new FriendsAdapter(friends, ChooseFriendActivity.this));
		
		// Loads friends list
		AsyncGetFollowers asyncGetFollowers = new AsyncGetFollowers(this);
		asyncGetFollowers.execute();
	}

	private void initLayout() {
		setContentView(R.layout.activity_choose_friend);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		friendList = (ListView) findViewById(R.id.friendList);
		btnSendChallenge = (Button) findViewById(R.id.btnSendChallenge);
		
		btnSendChallenge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				continueAction();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_friend, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
    		case android.R.id.home:
		    	finish();
		    	return true;
    		case R.id.menu_challenge:
    			continueAction();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
	  }
	}
	
	private void continueAction(){
		ArrayList<User> selectedFriends = new ArrayList<User>();
		FriendsAdapter adapter = (FriendsAdapter) friendList.getAdapter();
		if (adapter != null && adapter.getFriends() != null) {
			for (User c : adapter.getFriends()) {
				if (c.isSelected()){ 
					selectedFriends.add(c);
				}
			}
			
			if (selectedFriends.size() == 0) {
				Toast.makeText(ChooseFriendActivity.this, getString(R.string.select_one_friend),
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent i = new Intent(ChooseFriendActivity.this, ChallengeActivity.class);
			i.putExtra("selectedFriends", selectedFriends);
	  	  	startActivity(i);
	  	  	overridePendingTransition(R.anim.slide_out_left_transition, R.anim.slide_in_right_transition);
		}
	}
	
	public class AsyncGetFollowers extends AsyncTask<Void, Void, ArrayList<User>> {
    	private Context context;
    	private Exception e;

    	public AsyncGetFollowers(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() {}
    	
    	@Override
		protected ArrayList<User> doInBackground(Void... params) {
			ArrayList<User> friends = null;
			try {
				friends = UserAPI.getFollowers();
			} catch (NoConnectionException e) {
				this.e = e;
			} catch (UnauthorizedException e) {
				this.e = e;
			}
			return friends;
		}

		@Override
		protected void onPostExecute(ArrayList<User> result) {
			if (e instanceof NoConnectionException) {
				Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
			} else if (e instanceof UnauthorizedException) {
			} else {
				if (result != null) {		
					friendList.setAdapter(new FriendsAdapter(result, context));
				}
			}
		}
	}   
	
}
