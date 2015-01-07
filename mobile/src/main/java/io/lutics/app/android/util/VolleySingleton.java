package io.lutics.app.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * @author Sunny-J
 * @description Singleton for Volley
 */
public class VolleySingleton {

    // Variable
    private static VolleySingleton mInstance;

    private static Context context;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    // Constructor
    private VolleySingleton(Context context) {
        this.context = context;

        this.mRequestQueue = getRequestQueue();
        this.mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8));
    }

    // Getter
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * @author Sunny-J
     * @description Lru Bitmap Cache
     */
    private class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

        // Constructor
        public LruBitmapCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
    }
}