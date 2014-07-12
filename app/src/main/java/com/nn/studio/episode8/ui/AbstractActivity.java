package com.nn.studio.episode8.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.nn.studio.episode8.R;
import com.nn.studio.episode8.provider.PGContract;
import com.nn.studio.episode8.utils.HtmlImageParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by jibi on 7/7/14.
 */
public class AbstractActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ABSTRACT ACTIVITY";
    private static final int URL_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getApplicationContext(), PGContract.Forums.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}
