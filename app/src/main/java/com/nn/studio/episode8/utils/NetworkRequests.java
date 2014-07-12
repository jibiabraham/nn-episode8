package com.nn.studio.episode8.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by jibi on 9/7/14.
 * References and credits
 *  1, http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
 *  2. https://github.com/dmytrodanylyk/dmytrodanylyk/blob/gh-pages/articles/volley-part-3.md
 */
public final class NetworkRequests {
    private static NetworkRequests mInstance;
    private RequestQueue mRequestQueue;
    private RequestQueue mImageRequestQueue;
    private ImageLoader mImageLoader;
    private BitmapLruCache mImageLruCache;
    private static Context context;

    private NetworkRequests(Context context){
        this.context = context;
    }

    public static synchronized NetworkRequests getInstance(Context context){
        if(mInstance == null){
            mInstance = new NetworkRequests(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        getmImageRequestQueue();
        if(mImageLruCache == null){
            mImageLruCache = new BitmapLruCache(context.getApplicationContext());
        }
        if(mImageLoader == null){
            mImageLoader = new ImageLoader(mImageRequestQueue, mImageLruCache);
        }
        return mImageLoader;
    }

    public RequestQueue getmImageRequestQueue(){
        if(mImageRequestQueue == null){
            mImageRequestQueue = NetworkImageRequests.newRequestQueue(context.getApplicationContext());
        }
        return mImageRequestQueue;
    }

    public <T> void addToImageRequestQueue(Request<T> req){
        getmImageRequestQueue().add(req);
    }
}
