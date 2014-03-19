package br.com.challengeaccepted.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import br.com.challengeaccepted.ChallengeReplyActivity;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.adapter.ReceivedChallengesAdapter;
import br.com.challengeaccepted.api.ChallengeAPI;
import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;

@SuppressLint("ValidFragment")
public class NotificationFragment extends ListFragment {

	// Layout Variables
	private ListView listView;
	private LinearLayout loadingLayout;
	private Context context;
	private AsyncGetReceivedChallenges asyncGetReceivedChallenges;
		
	public NotificationFragment(){}
	
	public NotificationFragment(Context context) {
		this.context = context;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
               
        listView = (ListView) getView().findViewById(android.R.id.list);

        asyncGetReceivedChallenges = new AsyncGetReceivedChallenges(context);
        asyncGetReceivedChallenges.execute();
        
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		Challenge selectedChallenge= (Challenge) listView.getItemAtPosition(position);
        		Intent i = new Intent(getActivity(), ChallengeReplyActivity.class);
        		i.putExtra("challenge", selectedChallenge);
        		startActivity(i);
        	}
        });

    }
    
    @Override
	public void onDetach() {
		if(asyncGetReceivedChallenges != null && asyncGetReceivedChallenges.getStatus() != AsyncTask.Status.FINISHED){
			asyncGetReceivedChallenges.cancel(true);
		}
		super.onDetach();
	}
    
    public class AsyncGetReceivedChallenges extends AsyncTask<Void, Void, ArrayList<Challenge>> {
    	private Context context;
    	private Exception e;

    	public AsyncGetReceivedChallenges(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() {}
    	
    	@Override
		protected ArrayList<Challenge> doInBackground(Void... params) {
			ArrayList<Challenge> challenges = null;
			try {
				challenges = ChallengeAPI.getReceivedChallenges();
			} catch (NoConnectionException e) {
				this.e = e;
			} catch (UnauthorizedException e) {
				this.e = e;
			}
			return challenges;
		}

		protected void onPostExecute(ArrayList<Challenge> result) {
			if (!isCancelled() && isAdded()){
				if (e instanceof NoConnectionException) {
					Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
					loadingLayout.setVisibility(View.GONE);
				} else if (e instanceof UnauthorizedException) {
					// TODO: login
				} else {
					if (result != null) {		
						listView.setVisibility(View.VISIBLE);
						setListAdapter(new ReceivedChallengesAdapter(result, context));
					}
				}
			}
		}
	} 
    
}
