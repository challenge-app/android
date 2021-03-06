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
import br.com.challengeaccepted.adapter.FeedAdapter;
import br.com.challengeaccepted.api.FeedAPI;
import br.com.challengeaccepted.bean.Feed;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;

@SuppressLint("ValidFragment")
public class FeedFragment extends ListFragment {

	// Layout Variables
	private ListView listView;
	private LinearLayout loadingLayout;
	private Context context;
	private AsyncGetReceivedChallenges asyncGetReceivedChallenges;
		
	public FeedFragment(){}
	
	public FeedFragment(Context context) {
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

    }
    
    @Override
	public void onDetach() {
		if(asyncGetReceivedChallenges != null && asyncGetReceivedChallenges.getStatus() != AsyncTask.Status.FINISHED){
			asyncGetReceivedChallenges.cancel(true);
		}
		super.onDetach();
	}
    
    public class AsyncGetReceivedChallenges extends AsyncTask<Void, Void, ArrayList<Feed>> {
    	private Context context;
    	private Exception e;

    	public AsyncGetReceivedChallenges(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() { }
    	
    	@Override
		protected ArrayList<Feed> doInBackground(Void... params) {
			ArrayList<Feed> feed = null;
			try {
				feed = FeedAPI.getReceivedChallenges();
			} catch (NoConnectionException e) {
				this.e = e;
			} catch (UnauthorizedException e) {
				this.e = e;
			}
			return feed;
		}

		protected void onPostExecute(ArrayList<Feed> result) {
			
			if (!isCancelled() && isAdded()){
				if (e instanceof NoConnectionException) {
//					loadingLayout.setVisibility(View.GONE);
				} else if (e instanceof UnauthorizedException) {
//	            	   Toast.makeText(context, "UNAUTHORIZED", Toast.LENGTH_SHORT).show();
					// TODO: login
				} else {
					if (result != null) {
						listView.setVisibility(View.VISIBLE);
						setListAdapter(new FeedAdapter(result, context));
					}
				}
			}
		}
	} 
        
}
