package com.nn.studio.episode8.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nn.studio.episode8.model.Discussion;
import com.nn.studio.episode8.provider.PGContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jibi on 8/7/14.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private final String TAG = "SyncAdapter";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.w(TAG, "onPerformSync");

        if(bundle.getBoolean("dumbSync")){
            final ContentResolver cr = getContext().getContentResolver();
            Cursor forums = cr.query(PGContract.Forums.CONTENT_URI, PGContract.Forums.DEFAULT_PROJECTION, null, null, null);
            final HashMap<String, Long> forumIdMap = new HashMap<String, Long>();
            if (forums.getCount() > 0){
                int indexId = forums.getColumnIndex(PGContract.Forums._ID);
                int indexUrlStarsWith = forums.getColumnIndex(PGContract.Forums.COLUMN_NAME_URL_STARTSWITH);
                for (int i = 0; i < forums.getCount(); i++) {
                    forums.moveToPosition(i);
                    Long forumId = forums.getLong(indexId);
                    String urlStartsWith = forums.getString(indexUrlStarsWith);
                    forumIdMap.put(urlStartsWith, forumId);
                }
                RequestProxy.getDiscussionsInForums(getContext(), forumIdMap);
                Log.w(TAG, forumIdMap.toString());
            }
        }

    }
}
