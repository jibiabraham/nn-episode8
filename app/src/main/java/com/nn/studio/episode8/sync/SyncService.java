package com.nn.studio.episode8.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.nn.studio.episode8.sync.SyncAdapter;

/**
 * Created by jibi on 8/7/14.
 */
public class SyncService extends Service {
    private final String TAG = "SyncService";
    private static SyncAdapter syncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if(syncAdapter == null){
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
