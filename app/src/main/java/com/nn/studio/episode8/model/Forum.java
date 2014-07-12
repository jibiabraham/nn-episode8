package com.nn.studio.episode8.model;

import android.content.ContentValues;

import com.nn.studio.episode8.provider.PGContract;

/**
 * Created by jibi on 10/7/14.
 */
public class Forum {
    public String title;
    public String urlStartsWith;
    public Long created;
    public Long modified;

    public Forum(String title, String urlStartsWith, Long created, Long modified) {
        this.title = title;
        this.urlStartsWith = urlStartsWith;
        this.created = created;
        this.modified = modified;
    }

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        Long now = System.currentTimeMillis();
        if(created == null){
            created = now;
        }
        if(modified == null){
            modified = now;
        }
        cv.put(PGContract.Forums.COLUMN_NAME_TITLE, title);
        cv.put(PGContract.Forums.COLUMN_NAME_URL_STARTSWITH, urlStartsWith);
        cv.put(PGContract.Forums.COLUMN_NAME_CREATE_DATE, created);
        cv.put(PGContract.Forums.COLUMN_NAME_MODIFICATION_DATE, modified);
        return cv;
    }
}
