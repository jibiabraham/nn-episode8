package com.nn.studio.episode8.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.nn.studio.episode8.utils.TestData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jibi on 7/7/14.
 * References and credist
 *  1. NotePad sample app provided in the Android SDK
 */
public class PGProvider extends ContentProvider {
    // Debugging and logging only
    private static final String TAG = "PGContentProvider";

    private static final String DATABASE_NAME = "pg.db";
    private static final int DATABASE_VERSION = 16;

    private static HashMap<String, String> sForumsProjectionMap;
    private static HashMap<String, String> sDiscussionsProjectionMap;
    private static HashMap<String, String> sPostsProjectionMap;
    private static HashMap<String, String> sCommentsProjectionMap;
    private static HashMap<String, String> sUsersProjectionMap;

    // URI matches
    private static final int FORUMS = 100;
    private static final int FORUM_ID = 101;
    private static final int DISCUSSIONS = 200;
    private static final int DISCUSSION_ID = 201;
    private static final int FORUM_DISCUSSIONS = 202;
    private static final int FORUM_DISCUSSION_ID = 203;
    private static final int POSTS = 300;
    private static final int POST_ID = 301;
    private static final int FORUM_DISCUSSION_POSTS = 302;
    private static final int FORUM_DISCUSSION_POST_ID = 303;
    private static final int COMMENTS = 400;
    private static final int COMMENT_ID = 401;
    private static final int FORUM_DISCUSSION_POST_COMMENTS = 402;
    private static final int FORUM_DISCUSSION_POST_COMMENT_ID = 403;
    private static final int USERS = 500;
    private static final int USER_ID = 501;

    private static final UriMatcher sUriMatcher;
    private DatabaseHelper mOpenHelper;

    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add the matcher URIs
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums", FORUMS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#", FORUM_ID);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/discussions", DISCUSSIONS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/discussions/#", DISCUSSION_ID);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/posts", POSTS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/posts/#", POST_ID);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/comments", COMMENTS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/comments/#", COMMENT_ID);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/users", USERS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/users/#", USER_ID);

        // Nested urls
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#/discussions", DISCUSSIONS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#/discussions/#", DISCUSSION_ID);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#/discussions/#/posts", POSTS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#/discussions/#/posts/#", POST_ID);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#/discussions/#/posts/#/comments", COMMENTS);
        sUriMatcher.addURI(PGContract.AUTHORITY, "/forums/#/discussions/#/posts/#/comments/#", COMMENT_ID);

        // Define the projection maps. Why, you ask? Keep asking for now.
        sForumsProjectionMap = new HashMap<String, String>(){{
            put(PGContract.Forums._ID, PGContract.Forums._ID);
            put(PGContract.Forums.COLUMN_NAME_TITLE, PGContract.Forums.COLUMN_NAME_TITLE);
            put(PGContract.Forums.COLUMN_NAME_URL_STARTSWITH, PGContract.Forums.COLUMN_NAME_URL_STARTSWITH);
            put(PGContract.Forums.COLUMN_NAME_CREATE_DATE, PGContract.Forums.COLUMN_NAME_CREATE_DATE);
            put(PGContract.Forums.COLUMN_NAME_MODIFICATION_DATE, PGContract.Forums.COLUMN_NAME_MODIFICATION_DATE);
        }};

        sDiscussionsProjectionMap = new HashMap<String, String>(){{
            put(PGContract.Discussions._ID, PGContract.Discussions._ID);
            put(PGContract.Discussions.COLUMN_NAME_TITLE, PGContract.Discussions.COLUMN_NAME_TITLE);
            put(PGContract.Discussions.COLUMN_NAME_URL, PGContract.Discussions.COLUMN_NAME_URL);
            put(PGContract.Discussions.COLUMN_NAME_CREATE_DATE, PGContract.Discussions.COLUMN_NAME_CREATE_DATE);
            put(PGContract.Discussions.COLUMN_NAME_MODIFICATION_DATE, PGContract.Discussions.COLUMN_NAME_MODIFICATION_DATE);
            put(PGContract.Discussions.COLUMN_NAME_FORUM_ID, PGContract.Discussions.COLUMN_NAME_FORUM_ID);
        }};

        sPostsProjectionMap = new HashMap<String, String>(){{
            put(PGContract.Posts._ID, PGContract.Posts._ID);
            put(PGContract.Posts.COLUMN_NAME_CONTENT, PGContract.Posts.COLUMN_NAME_CONTENT);
            put(PGContract.Posts.COLUMN_NAME_AUTHOR, PGContract.Posts.COLUMN_NAME_AUTHOR);
            put(PGContract.Posts.COLUMN_NAME_LIKES_COUNT, PGContract.Posts.COLUMN_NAME_LIKES_COUNT);
            put(PGContract.Posts.COLUMN_NAME_COMMENTS_COUNT, PGContract.Posts.COLUMN_NAME_COMMENTS_COUNT);
            put(PGContract.Posts.COLUMN_NAME_CREATE_DATE, PGContract.Posts.COLUMN_NAME_CREATE_DATE);
            put(PGContract.Posts.COLUMN_NAME_MODIFICATION_DATE, PGContract.Posts.COLUMN_NAME_MODIFICATION_DATE);
            put(PGContract.Posts.COLUMN_NAME_DISCUSSION_ID, PGContract.Posts.COLUMN_NAME_DISCUSSION_ID);
            put(PGContract.Posts.COLUMN_NAME_FORUM_ID, PGContract.Posts.COLUMN_NAME_FORUM_ID);
            put(PGContract.Posts.COLUMN_NAME_AUTHOR_ID, PGContract.Posts.COLUMN_NAME_AUTHOR_ID);
        }};

        sCommentsProjectionMap = new HashMap<String, String>(){{
            put(PGContract.Comments._ID, PGContract.Comments._ID);
            put(PGContract.Comments.COLUMN_NAME_CONTENT, PGContract.Comments.COLUMN_NAME_CONTENT);
            put(PGContract.Comments.COLUMN_NAME_AUTHOR, PGContract.Comments.COLUMN_NAME_AUTHOR);
            put(PGContract.Comments.COLUMN_NAME_LIKES_COUNT, PGContract.Comments.COLUMN_NAME_LIKES_COUNT);
            put(PGContract.Comments.COLUMN_NAME_COMMENTS_COUNT, PGContract.Comments.COLUMN_NAME_COMMENTS_COUNT);
            put(PGContract.Comments.COLUMN_NAME_CREATE_DATE, PGContract.Comments.COLUMN_NAME_CREATE_DATE);
            put(PGContract.Comments.COLUMN_NAME_MODIFICATION_DATE, PGContract.Comments.COLUMN_NAME_MODIFICATION_DATE);
            put(PGContract.Comments.COLUMN_NAME_POST_ID, PGContract.Comments.COLUMN_NAME_POST_ID);
            put(PGContract.Comments.COLUMN_NAME_DISCUSSION_ID, PGContract.Comments.COLUMN_NAME_DISCUSSION_ID);
            put(PGContract.Comments.COLUMN_NAME_FORUM_ID, PGContract.Comments.COLUMN_NAME_FORUM_ID);
            put(PGContract.Comments.COLUMN_NAME_AUTHOR_ID, PGContract.Comments.COLUMN_NAME_AUTHOR_ID);
        }};

        sUsersProjectionMap = new HashMap<String, String>(){{
            put(PGContract.Users._ID, PGContract.Users._ID);
            put(PGContract.Users.COLUMN_NAME_FULLNAME, PGContract.Users.COLUMN_NAME_FULLNAME);
            put(PGContract.Users.COLUMN_NAME_NICK, PGContract.Users.COLUMN_NAME_NICK);
            put(PGContract.Users.COLUMN_NAME_CREATE_DATE, PGContract.Users.COLUMN_NAME_CREATE_DATE);
            put(PGContract.Users.COLUMN_NAME_MODIFICATION_DATE, PGContract.Users.COLUMN_NAME_MODIFICATION_DATE);
        }};
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        int matchType = sUriMatcher.match(uri);
        List<String> pathSegments = uri.getPathSegments();

        String orderBy;

        switch (matchType){
            case FORUMS:
            case FORUM_ID:
                qb.setTables(PGContract.Forums.TABLE_NAME);
                if(matchType == FORUM_ID){
                    qb.appendWhere(
                            PGContract.Forums._ID + "="
                            + uri.getPathSegments().get(PGContract.Forums.FORUM_ID_PATH_POSITION));
                }
                orderBy = TextUtils.isEmpty(sortOrder) ? PGContract.Forums.DEFAULT_SORT_ORDER : sortOrder;
                Log.w(TAG, uri.getPathSegments().toString());
                break;
            case DISCUSSIONS:
            case DISCUSSION_ID:
                qb.setTables(PGContract.Discussions.TABLE_NAME);
                if(matchType == DISCUSSION_ID){
                    qb.appendWhere(
                            PGContract.Discussions._ID + "="
                            + uri.getPathSegments().get(PGContract.Discussions.DISCUSSION_ID_PATH_POSITION));
                }
                orderBy = TextUtils.isEmpty(sortOrder) ? PGContract.Discussions.DEFAULT_SORT_ORDER : sortOrder;
                break;
            case FORUM_DISCUSSIONS:
                String forumId = pathSegments.get(PGContract.Forums.FORUM_ID_PATH_POSITION);
                qb.setTables(PGContract.Discussions.TABLE_NAME);
                qb.appendWhere(PGContract.Forums._ID + "=" + forumId);
                orderBy = TextUtils.isEmpty(sortOrder) ? PGContract.Discussions.DEFAULT_SORT_ORDER : sortOrder;
                break;
            case POSTS:
            case POST_ID:
                qb.setTables(PGContract.Posts.TABLE_NAME);
                if(matchType == POST_ID){
                    qb.appendWhere(
                            PGContract.Posts._ID + "="
                            + uri.getPathSegments().get(PGContract.Posts.POST_ID_PATH_POSITION));
                }
                orderBy = TextUtils.isEmpty(sortOrder) ? PGContract.Posts.DEFAULT_SORT_ORDER : sortOrder;
                break;
            case COMMENTS:
            case COMMENT_ID:
                qb.setTables(PGContract.Comments.TABLE_NAME);
                if(matchType == COMMENT_ID){
                    qb.appendWhere(
                            PGContract.Comments._ID + "="
                            + uri.getPathSegments().get(PGContract.Comments.COMMENT_ID_PATH_POSITION));
                }
                orderBy = TextUtils.isEmpty(sortOrder) ? PGContract.Comments.DEFAULT_SORT_ORDER : sortOrder;
                break;
            case USERS:
            case USER_ID:
                qb.setTables(PGContract.Users.TABLE_NAME);
                if(matchType == USER_ID){
                    qb.appendWhere(
                            PGContract.Users._ID + "="
                            + uri.getPathSegments().get(PGContract.Users.USER_ID_PATH_POSITION));
                }
                orderBy = TextUtils.isEmpty(sortOrder) ? PGContract.Users.DEFAULT_SORT_ORDER : sortOrder;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case FORUMS:
            case FORUM_ID:
                return PGContract.Forums.CONTENT_TYPE;

            case DISCUSSIONS:
            case DISCUSSION_ID:
                return PGContract.Discussions.CONTENT_TYPE;

            case POSTS:
            case POST_ID:
                return PGContract.Posts.CONTENT_TYPE;

            case COMMENTS:
            case COMMENT_ID:
                return PGContract.Comments.CONTENT_TYPE;

            case USERS:
            case USER_ID:
                return PGContract.Users.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        SQLiteDatabase db;
        ContentValues values;
        Long now = System.currentTimeMillis();
        String activeTable;
        Uri baseUri;

        int match = sUriMatcher.match(uri);
        values = initialValues != null ? new ContentValues(initialValues) : new ContentValues();

        switch (match){
            case DISCUSSIONS:
            case FORUM_DISCUSSIONS:
            case DISCUSSION_ID:
                activeTable = PGContract.Discussions.TABLE_NAME;
                baseUri = PGContract.Discussions.CONTENT_ID_URI_BASE;
                break;
            case POSTS:
            case POST_ID:
                activeTable = PGContract.Posts.TABLE_NAME;
                baseUri = PGContract.Posts.CONTENT_ID_URI_BASE;
                break;
            case COMMENTS:
            case COMMENT_ID:
                activeTable = PGContract.Comments.TABLE_NAME;
                baseUri = PGContract.Comments.CONTENT_ID_URI_BASE;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI provided for ContentProvider::Insert");
        }


        db = mOpenHelper.getWritableDatabase();
        Long rowId = db.insertWithOnConflict(
                activeTable,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        if(rowId > 0){
            Uri notificationUri = ContentUris.withAppendedId(baseUri, rowId);
            getContext().getContentResolver().notifyChange(notificationUri, null);
            return notificationUri;
        } else {
            Log.w(TAG, "Failed to insert row into " + uri);
            return Uri.EMPTY;
        }

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + PGContract.Forums.TABLE_NAME + " ("
                    + PGContract.Forums._ID + " INTEGER PRIMARY KEY,"
                    + PGContract.Forums.COLUMN_NAME_TITLE + " TEXT,"
                    + PGContract.Forums.COLUMN_NAME_URL_STARTSWITH + " TEXT,"
                    + PGContract.Forums.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                    + PGContract.Forums.COLUMN_NAME_MODIFICATION_DATE + " INTEGER"
                    +");");

            db.execSQL("CREATE TABLE " + PGContract.Discussions.TABLE_NAME + " ("
                    + PGContract.Discussions._ID + " INTEGER PRIMARY KEY,"
                    + PGContract.Discussions.COLUMN_NAME_TITLE + " TEXT,"
                    + PGContract.Discussions.COLUMN_NAME_URL + " TEXT,"
                    + PGContract.Discussions.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                    + PGContract.Discussions.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"
                    + PGContract.Discussions.COLUMN_NAME_FORUM_ID + " INTEGER"
                    +");");

            db.execSQL("CREATE TABLE " + PGContract.Posts.TABLE_NAME + " ("
                    + PGContract.Posts._ID + " INTEGER PRIMARY KEY,"
                    + PGContract.Posts.COLUMN_NAME_CONTENT + " TEXT,"
                    + PGContract.Posts.COLUMN_NAME_AUTHOR + " TEXT,"
                    + PGContract.Posts.COLUMN_NAME_LIKES_COUNT + " INTEGER,"
                    + PGContract.Posts.COLUMN_NAME_COMMENTS_COUNT + " INTEGER,"
                    + PGContract.Posts.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                    + PGContract.Posts.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"
                    + PGContract.Posts.COLUMN_NAME_DISCUSSION_ID + " INTEGER,"
                    + PGContract.Posts.COLUMN_NAME_FORUM_ID + " INTEGER,"
                    + PGContract.Posts.COLUMN_NAME_AUTHOR_ID + " INTEGER"
                    +");");

            db.execSQL("CREATE TABLE " + PGContract.Comments.TABLE_NAME + " ("
                    + PGContract.Comments._ID + " INTEGER PRIMARY KEY,"
                    + PGContract.Comments.COLUMN_NAME_CONTENT + " TEXT,"
                    + PGContract.Comments.COLUMN_NAME_AUTHOR + " TEXT,"
                    + PGContract.Comments.COLUMN_NAME_LIKES_COUNT + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_COMMENTS_COUNT + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_MODIFICATION_DATE + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_POST_ID + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_DISCUSSION_ID + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_FORUM_ID + " INTEGER,"
                    + PGContract.Comments.COLUMN_NAME_AUTHOR_ID + " INTEGER"
                    +");");

            db.execSQL("CREATE TABLE " + PGContract.Users.TABLE_NAME + " ("
                    + PGContract.Users._ID + " INTEGER PRIMARY KEY,"
                    + PGContract.Users.COLUMN_NAME_FULLNAME + " TEXT,"
                    + PGContract.Users.COLUMN_NAME_NICK + " TEXT,"
                    + PGContract.Users.COLUMN_NAME_CREATE_DATE + " INTEGER,"
                    + PGContract.Users.COLUMN_NAME_MODIFICATION_DATE + " INTEGER "
                    +");");

            TestData.insertTestData(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + PGContract.Forums.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PGContract.Discussions.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PGContract.Posts.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PGContract.Comments.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + PGContract.Users.TABLE_NAME);

            onCreate(db);
        }
    }
}
