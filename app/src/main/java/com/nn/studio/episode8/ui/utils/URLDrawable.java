package com.nn.studio.episode8.ui.utils;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by jibi on 9/7/14.
 * References and credits
 *  1. http://stackoverflow.com/a/7442725/1177441
 *  2. https://github.com/dmytrodanylyk/dmytrodanylyk/blob/gh-pages/articles/volley-part-3.md
 */
public class URLDrawable extends BitmapDrawable {
    // the drawable that you need to set, you could set the initial drawing
    // with the loading image if you need to
    protected Drawable drawable;

    @Override
    public void draw(Canvas canvas) {
        // override the draw to facilitate refresh function later
        Log.w(this.getClass().getName(), "Attempting to draw");
        if(drawable != null) {
            Log.w(this.getClass().getName(), "Has a drawable");
            drawable.draw(canvas);
            Log.w(this.getClass().getName(), Boolean.toString(drawable.isVisible()));
            Log.w(this.getClass().getName(), drawable.getBounds().toString());
        }
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
