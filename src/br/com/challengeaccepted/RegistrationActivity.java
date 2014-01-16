package br.com.challengeaccepted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.challengeaccepted.api.UserAPI;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.commons.Session;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UserExistsException;

public class RegistrationActivity extends ActionBarActivity {
	
	private EditText edtEmail;
	private EditText edtPassword;
	private Button btnRegister;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		mapLayoutComponents();
		
		// Handle Intent
		Bundle bundle = getIntent().getExtras();
		String email = bundle.getString("email");
		String password = bundle.getString("password");
		if (email != null)
			edtEmail.setText(email);
		if (password != null)
			edtPassword.setText(password);
		
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = edtEmail.getText().toString();
				String edtPassword = edtEmail.getText().toString();
				if (email != null && edtPassword != null)
					register(email, edtPassword);
				else
					Toast.makeText(RegistrationActivity.this, getString(R.string.fill_all_blanks),
							Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void register(final String email, final String password) {
		new AsyncTask<Void, Void, User>() {
			private Exception e = null;
			
			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(RegistrationActivity.this,
						null, getString(R.string.registering));
			}

			@Override
			protected User doInBackground(Void... params) {
				try {
					return UserAPI.register(email, password);
				} catch (NoConnectionException e) {
					this.e = e;
				} catch (UserExistsException e) {
					this.e = e;
				} 
				return null;
			}
			
			protected void onPostExecute(User result) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				
				if (e instanceof NoConnectionException) {
					Toast.makeText(RegistrationActivity.this, getString(R.string.no_connection),
							Toast.LENGTH_SHORT).show();
				} else if (e instanceof UserExistsException) {
					Toast.makeText(RegistrationActivity.this, getString(R.string.user_exists),
							Toast.LENGTH_SHORT).show();
				} else {
					if (result != null){
						Toast.makeText(RegistrationActivity.this, getString(R.string.register_success),
								Toast.LENGTH_SHORT).show();
						Session.getSession().login(result, RegistrationActivity.this);
						Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					        i.addFlags(0x8000); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
						startActivity(i);
						finish();
					} else {
						Toast.makeText(RegistrationActivity.this, getString(R.string.register_fail),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void mapLayoutComponents() {
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
	}

}
