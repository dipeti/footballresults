package com.dinya.peter.livefootballresults.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BackgroundSyncIntentService extends IntentService {

    public BackgroundSyncIntentService() {
        super("BackgroundSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        BackgroundSyncTask.startSync(this);
    }
}
