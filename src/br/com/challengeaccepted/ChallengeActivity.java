package br.com.challengeaccepted;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import br.com.challengeaccepted.adapter.RandomChallengesAdapter;
import br.com.challengeaccepted.api.ChallengeAPI;
import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.ChallengeInfo;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;

public class ChallengeActivity extends ActionBarActivity {
	
	private ListView challengeList;
	private Button btnSendChallenge;
	private EditText edtCustomChallenge;
	private ArrayList<User> selectedFriends;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initLayout();

		selectedFriends = new ArrayList<User>();
		selectedFriends = (ArrayList<User>) getIntent().getExtras().get("selectedFriends");
		
		AsyncGetRandomChallenges asyncGetRandomChallenges = new AsyncGetRandomChallenges(this);
		asyncGetRandomChallenges.execute();
	}

	private void initLayout() {
		setContentView(R.layout.activity_challenge);
		
		challengeList = (ListView) findViewById(R.id.challengeList);
		btnSendChallenge = (Button) findViewById(R.id.btnSendChallenge);
		edtCustomChallenge = (EditText) findViewById(R.id.edtCustomChallenge);
		
		challengeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View arg1, int arg2,
					long arg3) {
				RandomChallengesAdapter adapter = (RandomChallengesAdapter) view.getAdapter();
				adapter.setSelectedIndex(arg2);
				adapter.notifyDataSetChanged();				
			}
		});
		
		edtCustomChallenge.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	        	if(s.length() > 0) {
					((RandomChallengesAdapter)challengeList.getAdapter()).setSelectedIndex(-1);
					((RandomChallengesAdapter)challengeList.getAdapter()).notifyDataSetChanged();
				}
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    }); 
					
		btnSendChallenge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AsyncCreateChallenge asyncCreateChallenge = new AsyncCreateChallenge(ChallengeActivity.this);
				asyncCreateChallenge.execute();			
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
		    	return true;
		    default:
		    	return super.onOptionsItemSelected(item);
	  }
	}
	
	public class AsyncGetRandomChallenges extends AsyncTask<Void, Void, ArrayList<ChallengeInfo>> {
    	private Context context;
    	private Exception e;

    	public AsyncGetRandomChallenges(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() {}
    	
    	@Override
		protected ArrayList<ChallengeInfo> doInBackground(Void... params) {
			ArrayList<ChallengeInfo> challenge = null;
			try {
				challenge = ChallengeAPI.getRandomChallenges();
			} catch (NoConnectionException e) {
				this.e = e;
			} catch (UnauthorizedException e) {
				this.e = e;
			}
			return challenge;
		}

		@Override
		protected void onPostExecute(ArrayList<ChallengeInfo> result) {
			if (e instanceof NoConnectionException) {
				Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
			} else if (e instanceof UnauthorizedException) {
			} else {
				if (result != null) {		
					challengeList.setAdapter(new RandomChallengesAdapter(result, context));
				}
			}
		}
	}  
	
	public class AsyncCreateChallenge extends AsyncTask<Void, Void, Challenge> {
    	private Context context;
    	private Exception e;

    	public AsyncCreateChallenge(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ChallengeActivity.this, null,
					getString(R.string.sending_challenge));
		}
    	
    	@Override
		protected Challenge doInBackground(Void... params) {
			Challenge challenge = null;
			try {
				if (edtCustomChallenge == null) {
					RandomChallengesAdapter adapter = (RandomChallengesAdapter) challengeList.getAdapter();
					ChallengeInfo challengeInfo = (ChallengeInfo) adapter.getItem(adapter.getSelectedIndex());
					challenge = ChallengeAPI.createChallenge(challengeInfo.getId(), "video", selectedFriends.get(0).getId(), 1);
				} else {
					challenge = ChallengeAPI.createCustomChallenge(edtCustomChallenge.getText().toString(),
							"picture", selectedFriends.get(0).getId(), 1);
				}
			} catch (NoConnectionException e) {
				this.e = e;
			} catch (UnauthorizedException e) {
				this.e = e;
			}
			return challenge;
		}

		@Override
		protected void onPostExecute(Challenge result) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null; 
			}
			
			if (e instanceof NoConnectionException) {
				Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
			} else if (e instanceof UnauthorizedException) {
			} else {
				if (result != null) {		
					Toast.makeText(context, getString(R.string.challenge_sent), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else {
					Toast.makeText(context, getString(R.string.challenge_send_fail), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}  
	
}
