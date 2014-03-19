package br.com.challengeaccepted.commons;

public class Constants {

	public static final String PROTOCOL = "http";
	public static final String HOST = "mauriciogiordano.com";
	public static final int PORT = 3000;
	public static final String API_VERSION = "application/vnd.challengeaccepted.v1";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String API_KEY_AUTHORIZATION = "BATATA QUENTE";
	public static final String USER_AGENT = "ChallengeAccepted/0.1 android/" + android.os.Build.VERSION.SDK_INT + " " + android.os.Build.DEVICE+"/"+android.os.Build.MODEL;
	
	public static final String AWS_ENDPOINT = "s3-website-us-east-1.amazonaws.com";
	public static final String AWS_ACCESS_KEY_ID = "AKIAIGDAABCJXL3WFI6Q";
	public static final String AWS_SECRET_ACCESS_KEY = "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";
	public static final String AWS_BUCKET = "challengeapp";
	public static final String AWS_ENVIRONMENT_PHOTO = "picture";
	public static final String AWS_ENVIRONMENT_VIDEO = "video";
	public static final String AWS_SENDER_APP_NAME = "challengeapp";
	
	public static final int STATUS_NOT_SEEN = -1;
	public static final int STATUS_SEEN = 0;
	public static final int STATUS_DONE = 1;
	public static final int STATUS_REFUSES = 2;
}
