package br.com.challengeaccepted.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import br.com.challengeaccepted.ChallengeReplyActivity;
import br.com.challengeaccepted.R;
import br.com.challengeaccepted.bean.Feed;
import br.com.challengeaccepted.commons.Constants;
import br.com.challengeaccepted.util.MediaHelper;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class FeedAdapter extends BaseAdapter {
	
	private ArrayList<Feed> feeds;
	private Context context;
	private ProgressDialog progressBar;
	
	private class ViewHolder {
		TextView usersTextView;
		TextView challengeTextView;
		ImageView challengeImageView;
		VideoView challengeVideoView;
		ProgressBar progressBar;
	}
	
	public FeedAdapter(ArrayList<Feed> feeds, Context context) {
		this.feeds = feeds;
		this.context = context;
    }

	@Override
	public int getCount() {
		return feeds.size();
	}

	@Override
	public Object getItem(int position) {
		return feeds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<Feed> getFriends() {
		return this.feeds;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Feed feed = feeds.get(position); 
		
		if (convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_feed, parent, false);
			holder = new ViewHolder();
			
			// Mapear componentes
			holder.usersTextView =  (TextView) convertView.findViewById(R.id.usersTextView);
			holder.challengeTextView = (TextView) convertView.findViewById(R.id.challengeTextView);
			holder.challengeImageView = (ImageView) convertView.findViewById(R.id.challengeImageView);
			holder.challengeVideoView = (VideoView) convertView.findViewById(R.id.challengeVideoView);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			
			convertView.setTag(holder);
			
		} else {  
		      holder = (ViewHolder)convertView.getTag();  
		}  
		
		String users = feed.getChallenge().getSender().getEmail() +
				" >> " + feed.getChallenge().getReceiver().getEmail();
		holder.usersTextView.setText(users);
		
		String description = feed.getChallenge().getInfo().getDescription();
		holder.challengeTextView.setText(description);
		
		// DONE
		if (feed.getChallenge().getStatus() == Constants.STATUS_DONE){
			// PICTURE
			if (feed.getChallenge().getType().equals("picture")){
				int size = (int) (getScreenWidth() * 0.96);
				holder.challengeImageView.getLayoutParams().width = size;
				holder.challengeImageView.getLayoutParams().height = size;
				((View)holder.challengeImageView.getParent()).getLayoutParams().width = size;
				((View)holder.challengeImageView.getParent()).getLayoutParams().height = size;
				
				ImageLoader.getInstance().displayImage(feed.getChallenge().getUrl(),
						holder.challengeImageView, new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								holder.progressBar.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onLoadingFailed(String imageUri, View view,
									FailReason failReason) {
								holder.progressBar.setVisibility(View.GONE);
							}
							
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								holder.progressBar.setVisibility(View.GONE);
								view.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								holder.progressBar.setVisibility(View.GONE);
							}
						});
			// VIDEO
			} else {
				holder.challengeImageView.setVisibility(View.GONE);
			}
		}
		
		return convertView;
	}	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
	private int getScreenWidth(){
		// Get screen size
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	if (Build.VERSION.SDK_INT >= 13){
    		Point screen = new Point();
    		display.getSize(screen);
    		return screen.x;
    	} else {
    		return display.getWidth();
    	}
	}
	
	public class AsyncDownloadVideo extends AsyncTask<Void, Void, Void> {
    	private Context context;
    	private long total, transfered, progress;
    	
    	public AsyncDownloadVideo(Context context) {
    		this.context = context;
    	}
    	
		@Override
		protected void onPreExecute() { 
			// prepare for a progress bar dialog
			progressBar = new ProgressDialog(context);
			progressBar.setCancelable(true);
			progressBar.setMessage(context.getString(R.string.downloading_video));
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();
		}
    	
    	@Override
		protected Void doInBackground(Void... params) {
    		AWSCredentials credential = new BasicAWSCredentials(Constants.AWS_ACCESS_KEY_ID,
    				Constants.AWS_SECRET_ACCESS_KEY);
    		 // TODO
    		TransferManager manager = new TransferManager(credential);
//    		GetObjectRequest obj = new GetObjectRequest(arg0, arg1)
    		
    		Download download = manager.download(Constants.AWS_ACCESS_KEY_ID,
    				Constants.AWS_SECRET_ACCESS_KEY, MediaHelper.getCacheVideoFile());
    		
    		while (!download.isDone()){
    			total = download.getProgress().getTotalBytesToTransfer();
    			transfered = download.getProgress().getBytesTransferred();
    			
    			progress = (transfered / total) * 100;
    			progressBar.setProgress((int) progress);
    		}
    		
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			if(progressBar != null)
				progressBar.dismiss();
		}
    	
	} 
	
}
