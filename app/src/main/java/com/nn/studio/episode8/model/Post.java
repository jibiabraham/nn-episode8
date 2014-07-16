package com.nn.studio.episode8.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.nn.studio.episode8.provider.PGContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jibi on 10/7/14.
 */
public class Post implements Parcelable {
    public Long _id;
    public String content;
    public Integer type;
    public String options;
    public Integer isMultiChoice;
    public String author;
    public Integer likesCount;
    public Integer commentsCount;
    public Long created;
    public Long modified;
    public Long discussion_id;
    public Long forum_id;
    public Long author_id;

    public final String AUTHORITY = "smaug.pagalguy.com";
    public final Integer DEFAULT_COMMENTS_PAGINATION_SIZE = 10;

    public Post(Long _id, String content, String author, Integer likesCount, Integer commentsCount, Long created, Long modified, Long discussion_id, Long forum_id, Long author_id) {
        this._id = _id;
        this.content = content;
        this.author = author;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.created = created;
        this.modified = modified;
        this.discussion_id = discussion_id;
        this.forum_id = forum_id;
        this.author_id = author_id;
    }

    public Post(Long id, String content, String author, Integer commentsCount, Integer likesCount, Long authorId) {
        this._id = id;
        this.content = content;
        this.author = author;
        this.commentsCount = commentsCount;
        this.likesCount = likesCount;
        this.author_id = authorId;
    }

    public Post(Long _id, String content, Integer type, String options, Integer isMultiChoice, String author, Integer likesCount, Integer commentsCount, Long author_id) {
        this._id = _id;
        this.content = content;
        this.type = type;
        this.options = options;
        this.isMultiChoice = isMultiChoice;
        this.author = author;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.created = created;
        this.modified = modified;
        this.discussion_id = discussion_id;
        this.forum_id = forum_id;
        this.author_id = author_id;
    }

    public Post (Cursor data){
        _id = data.getLong(data.getColumnIndex(PGContract.Posts._ID));
        content = data.getString(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_CONTENT));
        type = data.getInt(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_POST_TYPE));
        options = data.getString(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_OPTIONS));
        isMultiChoice = data.getInt(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_IS_MULTICHOICE));
        author = data.getString(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_AUTHOR));
        likesCount = data.getInt(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_LIKES_COUNT));
        commentsCount = data.getInt(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_COMMENTS_COUNT));
        created = data.getLong(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_CREATE_DATE));
        modified = data.getLong(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_MODIFICATION_DATE));
        discussion_id = data.getLong(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_DISCUSSION_ID));
        forum_id = data.getLong(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_FORUM_ID));
        author_id = data.getLong(data.getColumnIndex(PGContract.Posts.COLUMN_NAME_AUTHOR_ID));
    }

    public Post (Parcel parcel){
        _id = parcel.readLong();
        content = parcel.readString();
        type = parcel.readInt();
        options = parcel.readString();
        isMultiChoice = parcel.readInt();
        author = parcel.readString();
        likesCount = parcel.readInt();
        commentsCount = parcel.readInt();
        created = parcel.readLong();
        modified = parcel.readLong();
        discussion_id = parcel.readLong();
        forum_id = parcel.readLong();
        author_id = parcel.readLong();
    }

    public static Post fromJson(JSONObject post){
        try{
            Long id = post.getLong("id");
            String content = post.getString("content");
            Integer type = post.has("isQuestion") && post.getBoolean("isQuestion") ? PGContract.Posts.POST_TYPES.QUESTION : PGContract.Posts.POST_TYPES.POST;
            String options = post.has("options") ? post.getJSONArray("options").toString() : "";
            Integer isMultiChoice = post.has("isMultiChoice") && post.getBoolean("isMultiChoice") ? 1 : 0;
            String author = post.getJSONObject("author").getString("nick");
            Integer commentsCount = post.has("cmc") ? post.getInt("cmc") : 0;
            Integer likesCount = post.has("lkc") ? post.getInt("lkc") : 0;
            Long authorId = post.getJSONObject("author").getLong("id");
            return new Post(id, content, type, options, isMultiChoice, author, commentsCount, likesCount, authorId);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
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
        if(_id != null){
            cv.put(PGContract.Posts._ID, _id);
        }
        cv.put(PGContract.Posts.COLUMN_NAME_CONTENT, content);
        cv.put(PGContract.Posts.COLUMN_NAME_POST_TYPE, type);
        cv.put(PGContract.Posts.COLUMN_NAME_OPTIONS, options);
        cv.put(PGContract.Posts.COLUMN_NAME_IS_MULTICHOICE, isMultiChoice);
        cv.put(PGContract.Posts.COLUMN_NAME_AUTHOR, author);
        cv.put(PGContract.Posts.COLUMN_NAME_LIKES_COUNT, likesCount);
        cv.put(PGContract.Posts.COLUMN_NAME_COMMENTS_COUNT, commentsCount);
        cv.put(PGContract.Posts.COLUMN_NAME_CREATE_DATE, created);
        cv.put(PGContract.Posts.COLUMN_NAME_MODIFICATION_DATE, modified);
        cv.put(PGContract.Posts.COLUMN_NAME_DISCUSSION_ID, discussion_id);
        cv.put(PGContract.Posts.COLUMN_NAME_FORUM_ID, forum_id);
        cv.put(PGContract.Posts.COLUMN_NAME_AUTHOR_ID, author_id);
        return cv;
    }

    public String getCommentsUrl(){
        if(_id == null)
            return null;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").authority(AUTHORITY).appendPath("comments").appendPath(Long.toString(_id)).appendQueryParameter("pages", "1,2,3,4,5");
        return builder.build().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(_id);
        parcel.writeString(content);
        parcel.writeInt(type);
        parcel.writeString(options);
        parcel.writeInt(isMultiChoice);
        parcel.writeString(author);
        parcel.writeInt(likesCount);
        parcel.writeInt(commentsCount);
        parcel.writeLong(created);
        parcel.writeLong(modified);
        parcel.writeLong(discussion_id);
        parcel.writeLong(forum_id);
        parcel.writeLong(author_id);
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel parcel) {
            return new Post(parcel);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getAuthorAsString(){
        return "with love, " + author;
    }

    public String getCommentsCountAsString(){
        if(commentsCount == 1){
            return "1 person has commented on this";
        } else if (commentsCount > 1){
            return Integer.toString(commentsCount) + " people have commented on this";
        }
        return "No one seems interested in this, yet.";
    }

    public String getLikesCountAsString(){
        if(likesCount == 1){
            return "1 person likes this";
        } else if (likesCount > 1){
            return Integer.toString(likesCount) + " people like this";
        }
        return "This seems to an ugly duckling. No one likes it :(";
    }

    public JSONArray getOptions(){
        if(type == PGContract.Posts.POST_TYPES.QUESTION){
            try {
                JSONArray json = new JSONArray(options);
                return json;
            } catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    public Boolean isQuestion(){
        return type == PGContract.Posts.POST_TYPES.QUESTION;
    }
}
