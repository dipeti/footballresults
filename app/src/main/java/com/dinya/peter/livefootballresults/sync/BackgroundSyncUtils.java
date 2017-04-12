package com.dinya.peter.livefootballresults.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class BackgroundSyncUtils {
    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){
        if (sInitialized) return;

        sInitialized = true;
        
        startImmediateSync(context);
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSync = new Intent(context,BackgroundSyncIntentService.class);
        context.startService(intentToSync);
    }
}
