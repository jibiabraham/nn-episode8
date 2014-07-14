package com.nn.studio.episode8.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.nn.studio.episode8.ui.utils.URLDrawable;

/**
 * Created by jibi on 9/7/14.
 * References and credits
 *  1. https://gist.github.com/Antarix/4167655
 */
public class HtmlImageParser implements Html.ImageGetter {
    private final String TAG = "HtmlImageParser";
    private Context context;
    private View container;

    public HtmlImageParser(Context context, View container) {
        this.context = context;
        this.container = container;
    }

    @Override
    public Drawable getDrawable(final String source) {
        final URLDrawable img = new URLDrawable();
        Log.w(TAG, source);
        ImageRequest request = new ImageRequest(source,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Drawable fetchedBitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
                        if(source.contains("http://sm.ge")){
                            TextView tv = (TextView) container;
                            fetchedBitmapDrawable.setBounds(0, -tv.getLineHeight(), tv.getLineHeight(), 0);
                        } else {
                            fetchedBitmapDrawable.setBounds(0, 0, fetchedBitmapDrawable.getIntrinsicWidth(), fetchedBitmapDrawable.getIntrinsicHeight());
                            container.setMinimumHeight(container.getMeasuredHeight() + fetchedBitmapDrawable.getIntrinsicHeight());
                        }
                        img.setDrawable(fetchedBitmapDrawable);
                        container.invalidate();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.w(TAG, volleyError.toString());
                    }
                }
        );
        NetworkRequests.getInstance(context).getmImageRequestQueue().add(request);
        return img;
    }
}
