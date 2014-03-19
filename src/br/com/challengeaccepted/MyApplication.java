package br.com.challengeaccepted;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MyApplication extends Application {
	
	private static MyApplication instance;
	
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
	        .defaultDisplayImageOptions(DisplayDefault(500))
	        .discCache(new LimitedAgeDiscCache(CacheDirDefault(getApplicationContext()), 1800))
	        .discCacheExtraOptions(300, 300, CompressFormat.JPEG, 60, null)
	        .discCacheSize(50 * 1024 * 1024)
	        .memoryCacheExtraOptions(250, 250)
	        .memoryCache(new LruMemoryCache(50 * 1024 * 1024))
	        .memoryCacheSize(50 * 1024 * 1024)
	        .memoryCacheSizePercentage(13)
	        .threadPriority(Thread.MAX_PRIORITY)
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCache(new WeakMemoryCache()).build();
		
		ImageLoader.getInstance().init(config);
		
	}
	
	public static DisplayImageOptions DisplayDefault(int fadeDurationMillis) {
		 
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                        .cacheOnDisc(true).cacheInMemory(true)
                        .resetViewBeforeLoading(true)
                        .displayer(new FadeInBitmapDisplayer(fadeDurationMillis))
                        .bitmapConfig(Bitmap.Config.RGB_565).build();

        return defaultOptions;

	}
	
	private static File CacheDirDefault(Context ctx) {
		 
		File cacheDir = StorageUtils.getCacheDirectory(ctx);
        cacheDir.mkdirs();

        return cacheDir;
}
	
	public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

}
