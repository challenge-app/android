package br.com.challengeaccepted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.challengeaccepted.api.UserAPI;
import br.com.challengeaccepted.bean.LoginResult;
import br.com.challengeaccepted.bean.User;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.commons.Session;
import br.com.challengeaccepted.db.BancoCreate;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UserNotFoundException;
import br.com.challengeaccepted.exception.WrongLoginException;

public class LoginActivity extends ActionBarActivity {
	
	private EditText edtEmail;
	private EditText edtPassword;
	private Button btnLogin;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Session.getSession().loadSessionUser(LoginActivity.this);
		if (Session.getSession().loggedIn()){
			Intent i = new Intent(LoginActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
		
		mapLayoutComponents();
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = edtEmail.getText().toString();
				String password = edtPassword.getText().toString();
				if (edtEmail.getText().length() != 0 && edtPassword.getText().length() != 0)
					login(email, password);
				else
					Toast.makeText(LoginActivity.this, getString(R.string.fill_all_blanks),
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void mapLayoutComponents() {
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void login(final String email, final String password) {
		new AsyncTask<Void, Void, LoginResult>() {
			private Exception e = null;
			
			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(LoginActivity.this,
						null, getString(R.string.logging_in));
			}

			@Override
			protected LoginResult doInBackground(Void... params) {
				try {
					return UserAPI.login(email, password);
				} catch (NoConnectionException e) {
					this.e = e;
				} catch (UserNotFoundException e) {
					this.e = e;
				} catch (WrongLoginException e) {
					this.e = e;
				} 
				return null;
			}
			
			@Override
			protected void onPostExecute(LoginResult result) {
				if (progressDialog != null) {
					progressDialog.dismiss();
					progressDialog = null;
				}
				
				if (e instanceof NoConnectionException || result == null) {
					Toast.makeText(LoginActivity.this, getString(R.string.no_connection),
							Toast.LENGTH_SHORT).show();
				} else if (result.getErrorMessage() != null) {
					switch (Integer.parseInt(result.getErrorMessage())) {
					case Constants.ERROR_PASSWORD_INCORRECT:
						Toast.makeText(LoginActivity.this, getString(R.string.wrong_login_or_password),
								Toast.LENGTH_SHORT).show();
						break;
					case Constants.ERROR_USER_NOT_FOUND:
						Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
						i.putExtra("email", email);
						i.putExtra("password", password);
						startActivity(i);
						Toast.makeText(LoginActivity.this, getString(R.string.user_not_found_please_register),
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}
				} else {
					BancoCreate bd = new BancoCreate(LoginActivity.this);
					bd.setFriends(result.getResult());
					Toast.makeText(LoginActivity.this, getString(R.string.login_success),
							Toast.LENGTH_SHORT).show();
					Session.getSession().login(result.getResult(), LoginActivity.this);
					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}
			}
		}.execute();
	}

}
