package br.com.challengeaccepted.commons;

import java.util.Calendar;
import java.util.UUID;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonsUtilities {

	private CommonsUtilities() {}
	
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	public static String generateAWSPath(boolean isVideo) {
//		Calendar cal = Calendar.getInstance();
		
		StringBuilder path;
		if (isVideo)
			path = new StringBuilder(Constants.AWS_ENVIRONMENT_VIDEO);
		else
			path = new StringBuilder(Constants.AWS_ENVIRONMENT_PHOTO);
//		path.append("/" + Constants.AWS_SENDER_APP_NAME);
//		path.append("/" + cal.get(Calendar.YEAR));
//		path.append("/" + (cal.get(Calendar.MONTH) + 1));
//		path.append("/" + cal.get(Calendar.DAY_OF_MONTH) + "/");
		
		return path.toString();
	}
	
	public static String generateAWSPictureName(String localFileName) {
		Calendar cal = Calendar.getInstance();
		StringBuilder pictureName = new StringBuilder("");
//		pictureName.append("/" + Constants.AWS_SENDER_APP_NAME);
		pictureName.append("/" + cal.get(Calendar.YEAR));
		pictureName.append(cal.get(Calendar.MONTH) + 1);
		pictureName.append(cal.get(Calendar.DAY_OF_MONTH));
		pictureName.append(UUID.randomUUID().toString() + ".jpg");
		return pictureName.toString();
	}
	
	public static String generateAWSVideoName(String localFileName) {
		Calendar cal = Calendar.getInstance();
		StringBuilder videoName = new StringBuilder("");
//		pictureName.append("/" + Constants.AWS_SENDER_APP_NAME);
		videoName.append("/" + cal.get(Calendar.YEAR));
		videoName.append(cal.get(Calendar.MONTH) + 1);
		videoName.append(cal.get(Calendar.DAY_OF_MONTH));
		videoName.append(UUID.randomUUID().toString() + ".mp4");
		return videoName.toString();
	}
}