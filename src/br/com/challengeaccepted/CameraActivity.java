package br.com.challengeaccepted;

import static br.com.challengeaccepted.util.CameraHelper.cameraAvailable;
import static br.com.challengeaccepted.util.CameraHelper.getCameraInstance;
import static br.com.challengeaccepted.util.MediaHelper.getOutputPictureFile;
import static br.com.challengeaccepted.util.MediaHelper.getOutputVideoFile;
import static br.com.challengeaccepted.util.MediaHelper.saveToFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import br.com.challengeaccepted.util.CameraPreview;
import br.com.challengeaccepted.util.Log;

/**
 * Takes a photo saves it to the SD card and returns the path of this photo to the calling Activity
 * @author paul.blundell
 *
 */
public class CameraActivity extends Activity implements PictureCallback {

	protected static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

	private Camera camera;
	private CameraPreview cameraPreview;
	private boolean isVideo;
	private boolean recording;
	private MediaRecorder mediaRecorder;
	private MyCameraSurfaceView myCameraSurfaceView;
	private String videoPath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		recording = false;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null)
			isVideo = extras.getBoolean("isVideo");
		
		setResult(RESULT_CANCELED);
		// Camera may be in use by another activity or the system or not available at all
		camera = getCameraInstance();
		if(cameraAvailable(camera)){
			initCameraPreview();
			
			if (!isVideo) {
			    setupPictureParameters();
			}
			
		} else {
			finish();
		}
		
		if (isVideo) {
			myCameraSurfaceView = new MyCameraSurfaceView(this, camera);
	        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
	        myCameraPreview.addView(myCameraSurfaceView);
		}
		
	}

	@SuppressLint("InlinedApi") 
	private void setupPictureParameters() {
		Camera.Parameters params = camera.getParameters();		    
		
		params = camera.getParameters();
		
		params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
		params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
		params.setExposureCompensation(0);
		params.setPictureFormat(ImageFormat.JPEG);
		params.setJpegQuality(60);
		params.setRotation(0);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		} else {
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		
		List<Size> sizes = params.getSupportedPictureSizes();
		Camera.Size size = sizes.get(0);
		for(int i=0;i<sizes.size();i++)
		{
		    if(sizes.get(i).width > size.width && size.width < 1000)
		        size = sizes.get(i);
		}
		params.setPictureSize(size.width, size.height);
		
		camera.setParameters( params );
	}

	// Show the camera view on the activity
	private void initCameraPreview() {
		cameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
		cameraPreview.init(camera);
	}

	@FromXML
	public void onCaptureClick(View button){
		// Take a picture with a callback when the photo has been created
		// Here you can add callbacks if you want to give feedback when the picture is being taken
		
		AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
		
		
		if (isVideo) {
			
			if(recording){
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                
                Intent intent = new Intent();
        		intent.setData(Uri.parse(videoPath));
        		setResult(RESULT_OK, intent);
               
                //Exit after saved
                finish();
            }else{
                //Release Camera before MediaRecorder start
                releaseCamera();
               
                if(!prepareMediaRecorder()){
                    Toast.makeText(CameraActivity.this,
                            "Fail in prepareMediaRecorder()!\n - Ended -",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
               
                mediaRecorder.start();
                recording = true;
            }
		} else {
			try {
				camera.takePicture(null, null, this);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.d("Picture taken");
		if (data != null) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // Notice that width and height are reversed
                Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                int w = scaled.getWidth();
                int h = scaled.getHeight();
                // Setting post rotate to 90
                Matrix mtx = new Matrix();
                mtx.postRotate(90);
                // Rotating Bitmap
                bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
            }else{// LANDSCAPE MODE
                //No need to reverse width and height
                Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
                bm=scaled;
            }
            
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String path = savePictureToFileSystem(byteArray);
    		setResult(path);
        }
		finish();
	}

	private static String savePictureToFileSystem(byte[] data) {
		File file = getOutputPictureFile();
		saveToFile(data, file);
		return file.getAbsolutePath();
	}

	private void setResult(String path) {
		Intent intent = new Intent();
		intent.setData(Uri.parse(path));
		setResult(RESULT_OK, intent);
	}

	// ALWAYS remember to release the camera when you are finished
	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}

	private void releaseCamera() {
		if(camera != null){
			camera.release();
			camera = null;
		}
	}
	
	private boolean prepareMediaRecorder(){
        camera = getCameraInstance();
        mediaRecorder = new MediaRecorder();

        camera.unlock();
        mediaRecorder.setCamera(camera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));

        videoPath = getOutputVideoFile().getPath();
        mediaRecorder.setOutputFile(videoPath);
        mediaRecorder.setMaxDuration(8000); // Set max duration 8 sec.
        mediaRecorder.setMaxFileSize(1000000); // Set max file size 2M

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
       
    }
	
	private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            camera.lock();           // lock camera for later use
        }
    }
	
	public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;
        
        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
              // preview surface does not exist
              return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
              // ignore: tried to stop a non-existent preview
            }
            
            Parameters parameters = mCamera.getParameters();
            Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

            if(display.getRotation() == Surface.ROTATION_0)
            {
                parameters.setPreviewSize(height, width);                           
                mCamera.setDisplayOrientation(90);
            }

            if(display.getRotation() == Surface.ROTATION_90)
            {
                parameters.setPreviewSize(width, height);                           
            }

            if(display.getRotation() == Surface.ROTATION_180)
            {
                parameters.setPreviewSize(height, width);               
            }

            if(display.getRotation() == Surface.ROTATION_270)
            {
                parameters.setPreviewSize(width, height);
                mCamera.setDisplayOrientation(180);
            }

            // start preview with new settings
            try {
            	mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            
        }
    }
	
}
