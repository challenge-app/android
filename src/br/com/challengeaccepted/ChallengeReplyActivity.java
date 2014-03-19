package br.com.challengeaccepted;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.com.challengeaccepted.api.ChallengeAPI;
import br.com.challengeaccepted.bean.Challenge;
import br.com.challengeaccepted.bean.S3TaskResult;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.exception.NoConnectionException;
import br.com.challengeaccepted.exception.UnauthorizedException;
import br.com.challengeaccepted.webservice.AmazonS3WS;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;

public class ChallengeReplyActivity extends ActionBarActivity {
	
	private static final int REQUEST_VIDEO = 100;
	private static final int REQUEST_PHOTO = 200;
	
	private ProgressDialog progressDialog;
	
	private Handler progressBarHandler = new Handler();
	private AmazonS3Client s3Client = new AmazonS3Client(
			new BasicAWSCredentials(Constants.AWS_ACCESS_KEY_ID,
					Constants.AWS_SECRET_ACCESS_KEY));
	
	private Challenge challenge;
	private TextView challengeTitle;
	private TextView challengeSender;
	private Button captureButton;
	private ProgressDialog progressBar;
	private boolean isVideo;
	private String lastUploadedMedia = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initLayout();
		handleIntent();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		captureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChallengeReplyActivity.this, CameraActivity.class);
				if (lastUploadedMedia == null) {
			    	if (isVideo){
			    		intent.putExtra("isVideo", true);
			    		startActivityForResult(intent, REQUEST_VIDEO);
			    	} else {
			    		intent.putExtra("isVideo", false);
			    		startActivityForResult(intent, REQUEST_PHOTO);
			    	}
				} else {
					acceptChallenge(lastUploadedMedia);
				}
		    	
//				if (isVideo) {
//					Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//					intent.putExtra("android.intent.extra.durationLimit", 8);
//					intent.putExtra("android.intent.extra.videoQuality", 0);
//					startActivityForResult(intent, REQUEST_VIDEO);
//				} else {
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					startActivityForResult(intent, REQUEST_PHOTO);
//				}
			}
		});
		
	}

	private void initLayout() {
		setContentView(R.layout.activity_challenge_reply);
		challengeSender = (TextView) findViewById(R.id.challengeSender);
		challengeTitle = (TextView) findViewById(R.id.challengeTitle);
		captureButton = (Button) findViewById(R.id.captureButton);
	}
	
	private void handleIntent() {
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			challenge = (Challenge) extras.get("challenge");
			challengeSender.setText(challenge.getSender().getEmail() + " " +
									getString(R.string.challenged_you));
			challengeTitle.setText(challenge.getInfo().getDescription());
			if (challenge.getType().equals("video"))
				isVideo = true;
			else
				isVideo = false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_VIDEO && resultCode == RESULT_OK) {
	        sendMediaToServer(data.getData());
	    }
	    if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
	        sendMediaToServer(data.getData());
	    }	 
	}
	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	      case android.R.id.home:
	    	  finish();
	    	  overridePendingTransition(R.anim.slide_out_left_transition, R.anim.slide_in_right_transition);
	        return true;
	      default:
            return super.onOptionsItemSelected(item);
	  }
	}
	
	private void sendMediaToServer(final Uri uri){
		
		new AsyncTask<Void, Void, S3TaskResult>() {
			private long startTime;

			protected void onPreExecute() {
				// prepare for a progress bar dialog
				progressBar = new ProgressDialog(ChallengeReplyActivity.this);
				progressBar.setCancelable(true);
				if (isVideo)
					progressBar.setMessage(getString(R.string.sending_video));
				else
					progressBar.setMessage(getString(R.string.sending_image));
				progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressBar.setProgress(0);
				progressBar.setMax(100);
				progressBar.show();
				
				startTime = System.currentTimeMillis();
			}
			
			@Override
			protected S3TaskResult doInBackground(Void... params) {
				S3TaskResult result = new S3TaskResult();
				try {
					
					// gets image size in bytes
					File f = new File(uri.toString());
					final float size = f.length();
					
					Log.d("progress", String.valueOf(size));
					
					// sends image to server
					result = AmazonS3WS.sendFileToS3(uri, isVideo, getApplicationContext(), new ProgressListener() {
						float total = 0;
						float progress = 0;
		                 
						@SuppressWarnings("deprecation")
						@Override
		                 public void progressChanged(ProgressEvent pv) {
		                     total += (int) pv.getBytesTransfered();
		                     progress = (total / size) * 100;
		                     
		                     // Update the progress bar
		                     progressBarHandler.post(new Runnable() {
		                    	 public void run() {
			   						progressBar.setProgress((int) progress);
			   						Log.d("progress", String.valueOf(progress));
		                    	 }
		                     });
		                 }

		             });
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return result;
			}
			
			@SuppressWarnings("deprecation")
			protected void onPostExecute(S3TaskResult result) {
				
				if(progressBar != null)
					progressBar.dismiss();
				
				if (result.getErrorMessage() != null) {
					Toast.makeText(ChallengeReplyActivity.this, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
				} else if (result.getUrl() != null) {
					lastUploadedMedia = result.getUrl();
					acceptChallenge(result.getUrl());
				}
			}
			
		}.execute();
	}
	
	private void acceptChallenge(final String url){
		
		new AsyncTask<Void, Void, Challenge>() {
			
			Exception e = null;

			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(ChallengeReplyActivity.this, null,
						getString(R.string.accepting_challenge));
			}
			
			@Override
			protected Challenge doInBackground(Void... params) {
				try {
					return ChallengeAPI.acceptChallenge(challenge.getId(), url);
				} catch (NoConnectionException e) {
					this.e = e;
				} catch (UnauthorizedException e) {
					this.e = e;
				}
				return null;
			}
			
			@SuppressWarnings("deprecation")
			protected void onPostExecute(Challenge result) {
				if (progressDialog != null)
					progressDialog.dismiss();
				
				if (e instanceof NoConnectionException) {
					Toast.makeText(ChallengeReplyActivity.this,
							getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
				} else if (result != null) {
					Toast.makeText(ChallengeReplyActivity.this,
							getString(R.string.challenge_accepted_success), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(ChallengeReplyActivity.this,
							getString(R.string.challenge_accepted_fail), Toast.LENGTH_SHORT).show();
				}
			}
			
		}.execute();
	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
    	  Cursor cursor = null;
    	  
    	  try { 
    		String[] proj = { MediaStore.Images.Media.DATA };
    		cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
    		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    		cursor.moveToFirst();
    		return cursor.getString(column_index);
    	  } finally {
    	    if (cursor != null) {
    	      cursor.close();
    	    }
    	  }
	}
		
	protected void displayErrorAlert(String title, String message) {

		AlertDialog.Builder confirm = new AlertDialog.Builder(this);
		confirm.setTitle(title);
		confirm.setMessage(message);

		confirm.setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						ChallengeReplyActivity.this.finish();
					}
				});

		confirm.show().show();
	}
	
}
