package br.com.challengeaccepted;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import br.com.challengeaccepted.bean.Challenge;

public class ChallengeDetailsActivity extends ActionBarActivity {

	private Challenge challenge;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mapLayoutComponents();
	}

	private void mapLayoutComponents() {
	}
}
