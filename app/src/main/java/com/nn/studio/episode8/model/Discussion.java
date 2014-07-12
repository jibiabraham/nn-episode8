package com.nn.studio.episode8.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.nn.studio.episode8.provider.PGContract;

/**
 * Created by jibi on 10/7/14.
 *
 */
public class Discussion implements Parcelable {
    private static final String TAG = "MODEL:Discussion";
    public Long _id;
    public String title;
    public String url;
    public Long created;
    public Long modified;
    public Long forum_id;

    public Discussion(Long _id, String title, String url, Long created, Long modified, Long forum_id) {
        this._id = _id;
        this.title = title;
        this.url = url;
        this.created = created;
        this.modified = modified;
        this.forum_id = forum_id;
    }

    public Discussion(String title, String url, Long created, Long modified, Long forum_id) {
        this.title = title;
        this.url = url;
        this.created = created;
        this.modified = modified;
        this.forum_id = forum_id;
    }

    public Discussion(Long id, String title, String url, Long forum_id) {
        this._id = id;
        this.title = title;
        this.url = url;
        this.forum_id = forum_id;
    }

    public Discussion(Parcel source) {
        _id = source.readLong();
        title = source.readString();
        url = source.readString();
    }

    public Discussion(Cursor cursor){
        _id = cursor.getLong(cursor.getColumnIndex(PGContract.Discussions._ID));
        title = cursor.getString(cursor.getColumnIndex(PGContract.Discussions.COLUMN_NAME_TITLE));
        url = cursor.getString(cursor.getColumnIndex(PGContract.Discussions.COLUMN_NAME_URL));
        created = cursor.getLong(cursor.getColumnIndex(PGContract.Discussions.COLUMN_NAME_CREATE_DATE));
        modified = cursor.getLong(cursor.getColumnIndex(PGContract.Discussions.COLUMN_NAME_MODIFICATION_DATE));
        forum_id = cursor.getLong(cursor.getColumnIndex(PGContract.Discussions.COLUMN_NAME_FORUM_ID));
    }

    public static final Creator<Discussion> CREATOR = new Creator<Discussion>() {
        @Override
        public Discussion createFromParcel(Parcel parcel) {
            return new Discussion(parcel);
        }

        @Override
        public Discussion[] newArray(int size) {
            return new Discussion[size];
        }
    };

    public ContentValues toContentValues(){
        ContentValues cv = new ContentValues();
        Long now = System.currentTimeMillis();
        if(created == null){
            created = now;
        }
        if(modified == null){
            modified = now;
        }
        if(_id != null){
            cv.put(PGContract.Discussions._ID, _id);
        }
        cv.put(PGContract.Discussions.COLUMN_NAME_TITLE, title);
        cv.put(PGContract.Discussions.COLUMN_NAME_URL, url);
        cv.put(PGContract.Discussions.COLUMN_NAME_CREATE_DATE, created);
        cv.put(PGContract.Discussions.COLUMN_NAME_MODIFICATION_DATE, modified);
        cv.put(PGContract.Discussions.COLUMN_NAME_FORUM_ID, forum_id);
        return cv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(title);
        parcel.writeString(url);
    }
}
