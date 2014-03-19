package br.com.challengeaccepted.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.adapter.FriendsAdapter;
import br.com.challengeaccepted.adapter.UserSearchAdapter;
import br.com.challengeaccepted.api.UserAPI;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;
import br.com.challengeaccepted.exception.UserNotFoundException;

@SuppressLint("ValidFragment")
public class ChallengeFriendsFragment extends ListFragment {

	// Layout Variables
	private ListView listView;
	private LinearLayout loadingLayout;
	private Context context;
	
	private Button btnSearch;
	private EditText edtEmail;
		
	public ChallengeFriendsFragment(){}
	
	public ChallengeFriendsFragment(Context context) {
		this.context = context;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
               
        listView = (ListView) getView().findViewById(android.R.id.list);
        btnSearch = (Button) getView().findViewById(R.id.btnSearch);
        edtEmail = (EditText) getView().findViewById(R.id.edtEmail);

        btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (edtEmail.getText().toString().trim().length() != 0){
					AsyncSearchFriendByEmail asyncSearchFriendByEmail =
							new AsyncSearchFriendByEmail(getActivity(), edtEmail.getText().toString().trim());
					asyncSearchFriendByEmail.execute();
				} else {
					Toast.makeText(getActivity(), getString(R.string.fill_all_blanks),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
        
    }
    
    @Override
	public void onDetach() {
//		if(asyncGetFriends != null && asyncGetFriends.getStatus() != AsyncTask.Status.FINISHED){
//			asyncGetFriends.cancel(true);
//		}
		super.onDetach();
	}
    
    public class AsyncSearchFriendByEmail extends AsyncTask<Void, Void, ArrayList<User>> {
    	private Context context;
    	private Exception e;
    	private String email;
    	
    	public AsyncSearchFriendByEmail(Context context, String email) {
    		this.context = context;
    		this.email = email;
    	}
    	
		@Override
		protected void onPreExecute() {
//			loadingLayout.setVisibility(View.VISIBLE);
		}
    	
    	@Override
		protected ArrayList<User> doInBackground(Void... params) {
    		ArrayList<User> result = null;
			try {
				result = UserAPI.getUserByEmail(email);
			} catch (NoConnectionException e) {
				this.e = e;
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				this.e = e;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<User> result) {
			if (!isCancelled() && isAdded()){
				if (e instanceof NoConnectionException) {
					Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
//					loadingLayout.setVisibility(View.GONE);
				} else if (e instanceof UserNotFoundException) {
					Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
				} else if (result != null){
					setListAdapter(new UserSearchAdapter(result, ChallengeFriendsFragment.this, context));
				} else {
					Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}   
    
    public class AsyncFollowUser extends AsyncTask<Void, Void, ArrayList<User>> {
    	private Exception e;
    	private String id;
    	
    	public AsyncFollowUser(String id) {
    		this.id = id;
    	}
    	
		@Override
		protected void onPreExecute() {
//			loadingLayout.setVisibility(View.VISIBLE);
		}
    	
    	@Override
		protected ArrayList<User> doInBackground(Void... params) {
    		ArrayList<User> result = null;
			try {
				result = UserAPI.follow(id);
			} catch (NoConnectionException e) {
				this.e = e;
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				this.e = e;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<User> result) {
			if (!isCancelled() && isAdded()){
				if (e instanceof NoConnectionException) {
					Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
//					loadingLayout.setVisibility(View.GONE);
				} else if (e instanceof UserNotFoundException) {
//					Toast.makeText(context, getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
				} else {
					if (result != null) {
						Toast.makeText(context, getString(R.string.friend_follow_success),
								Toast.LENGTH_SHORT).show();
//						setListAdapter(new FriendsAdapter (result, context, false, ChallengeFriendsFragment.this));
					} else {
						Toast.makeText(context, getString(R.string.fail_try_again),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}   
    
    public static void followUser(ChallengeFriendsFragment challengeFriendsFragment, String id) {
    	AsyncFollowUser async = challengeFriendsFragment.new AsyncFollowUser(id);
    	async.execute();
    }
    
}
