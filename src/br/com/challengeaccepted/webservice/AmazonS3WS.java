package br.com.challengeaccepted.webservice;

import java.io.File;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import br.com.challengeaccepted.bean.S3TaskResult;
import br.com.challengeaccepted.commons.CommonsUtilities;
import br.com.challengeaccepted.commons.Constants;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AmazonS3WS {

	private AmazonS3WS() {
	}
	
	public static S3TaskResult sendFileToS3(Uri fileUri, boolean isVideo, Context context, ProgressListener listener) {
		
		AmazonS3Client s3Client = new AmazonS3Client(
				new BasicAWSCredentials(Constants.AWS_ACCESS_KEY_ID,
						Constants.AWS_SECRET_ACCESS_KEY));
		
		// The file location of the image selected.
		Uri selectedImage = getImageContentUri(context, new File(fileUri.toString()));

        ContentResolver resolver = context.getContentResolver();
        String fileSizeColumn[] = {OpenableColumns.SIZE}; 
        
		Cursor cursor = resolver.query(selectedImage,
                fileSizeColumn, null, null, null);
		
        cursor.moveToFirst();

        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
        // If the size is unknown, the value stored is null.  But since an int can't be
        // null in java, the behavior is implementation-specific, which is just a fancy
        // term for "unpredictable".  So as a rule, check if it's null before assigning
        // to an int.  This will happen often:  The storage API allows for remote
        // files, whose size might not be locally known.
        String size = null;
        if (!cursor.isNull(sizeIndex)) {
            // Technically the column stores an int, but cursor.getString will do the
            // conversion automatically.
            size = cursor.getString(sizeIndex);
        } 
        
		cursor.close();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(resolver.getType(selectedImage));
		if(size != null){
		    metadata.setContentLength(Long.parseLong(size));
		}
		
		S3TaskResult result = new S3TaskResult();
		String s3Path;
		
		if (isVideo){
			s3Path = CommonsUtilities.generateAWSPath(isVideo)
					+ CommonsUtilities.generateAWSVideoName(selectedImage.getPath());
		} else {
			s3Path = CommonsUtilities.generateAWSPath(isVideo)
					+ CommonsUtilities.generateAWSPictureName(selectedImage.getPath());
		}

		// Put the image data into S3.
		try {
			s3Client.createBucket(Constants.AWS_BUCKET);

			PutObjectRequest por = new PutObjectRequest(
					Constants.AWS_BUCKET, s3Path,
					resolver.openInputStream(selectedImage),metadata);
			
//			por.setCannedAcl(CannedAccessControlList.PublicRead);
			
			if (listener == null) {
				listener = new ProgressListener() {
	                 int total = 0;

	                 @SuppressWarnings("deprecation")
					@Override
	                 public void progressChanged(ProgressEvent pv) {
	                     total += (int) pv.getBytesTransfered();
	                 }

	             };
			}
			
			 por.setProgressListener(listener);
			 
			 s3Client.putObject(por);
			
		} catch (Exception exception) {

			result.setErrorMessage(exception.getMessage());
		}
		
		result.setUri("https://s3.amazonaws.com/" + Constants.AWS_BUCKET + "/" + s3Path);

		return result;
	}

	public static boolean oldSendFileToS3(Uri fileUri, boolean isVideo, Context context, ProgressListener listener) {		
		AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(
				Constants.AWS_ACCESS_KEY_ID, Constants.AWS_SECRET_ACCESS_KEY));
		s3Client.setEndpoint(Constants.AWS_ENDPOINT);

		String s3Path;
		
		if (isVideo){
			s3Path = CommonsUtilities.generateAWSPath(isVideo)
					+ CommonsUtilities.generateAWSPictureName(fileUri.getPath());
		} else {
			s3Path = CommonsUtilities.generateAWSPath(isVideo)
					+ CommonsUtilities.generateAWSVideoName(fileUri.getPath());
		}
		
		PutObjectResult result = null;
		
		try {
			PutObjectRequest por = new PutObjectRequest(
					Constants.AWS_BUCKET, s3Path, 
					new java.io.File(fileUri.getPath()));
			
			if (listener == null) {
				listener = new ProgressListener() {
	                 int total = 0;

	                 @SuppressWarnings("deprecation")
					@Override
	                 public void progressChanged(ProgressEvent pv) {
	                     total += (int) pv.getBytesTransfered();
	                 }

	             };
			}
			
			 por.setProgressListener(listener);
			
			result = s3Client.putObject(por);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}


		return true;
	}
	
	public static Uri getImageContentUri(Context context, File imageFile) {
	    String filePath = imageFile.getAbsolutePath();
	    Cursor cursor = context.getContentResolver().query(
	            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	            new String[] { MediaStore.Images.Media._ID },
	            MediaStore.Images.Media.DATA + "=? ",
	            new String[] { filePath }, null);

	    if (cursor != null && cursor.moveToFirst()) {
	        int id = cursor.getInt(cursor
	                .getColumnIndex(MediaStore.MediaColumns._ID));
	        Uri baseUri = Uri.parse("content://media/external/images/media");
	        return Uri.withAppendedPath(baseUri, "" + id);
	    } else {
	        if (imageFile.exists()) {
	            ContentValues values = new ContentValues();
	            values.put(MediaStore.Images.Media.DATA, filePath);
	            return context.getContentResolver().insert(
	                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        } else {
	            return null;
	        }
	    }
	}
	
}