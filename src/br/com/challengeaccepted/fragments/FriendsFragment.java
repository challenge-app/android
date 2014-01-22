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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.adapter.FriendsAdapter;
import br.com.challengeaccepted.api.UserAPI;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;

@SuppressLint("ValidFragment")
public class FriendsFragment extends ListFragment {

	// Layout Variables
	private ListView listView;
	private LinearLayout loadingLayout;
	private Context context;
	private AsyncGetFriends asyncGetFriends;
		
	public FriendsFragment(){}
	
	public FriendsFragment(Context context) {
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

        asyncGetFriends = new AsyncGetFriends(context);
        asyncGetFriends.execute();

    }
    
    @Override
	public void onDetach() {
		if(asyncGetFriends != null && asyncGetFriends.getStatus() != AsyncTask.Status.FINISHED){
			asyncGetFriends.cancel(true);
		}
		super.onDetach();
	}
    
    public class AsyncGetFriends extends AsyncTask<Void, Void, ArrayList<User>> {
    	private Context context;
    	private Exception e;

    	public AsyncGetFriends(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() {}
    	
    	@Override
		protected ArrayList<User> doInBackground(Void... params) {
			ArrayList<User> friends = null;
			try {
				friends = UserAPI.getFriends();
			} catch (NoConnectionException e) {
				this.e = e;
			} catch (UnauthorizedException e) {
				this.e = e;
			}
			return friends;
		}

		@Override
		protected void onPostExecute(ArrayList<User> result) {
			if (!isCancelled() && isAdded()){
				if (e instanceof NoConnectionException) {
					Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
					loadingLayout.setVisibility(View.GONE);
				} else if (e instanceof UnauthorizedException) {
					// TODO: login
				} else {
					if (result != null) {		
						listView.setVisibility(View.VISIBLE);
						setListAdapter(new FriendsAdapter (result, context));
					}
				}
			}
		}
	}    
}
