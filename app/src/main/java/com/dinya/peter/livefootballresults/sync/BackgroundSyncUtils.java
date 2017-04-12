package com.dinya.peter.livefootballresults.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import com.dinya.peter.livefootballresults.R;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

public class BackgroundSyncUtils {
    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){

        if (sInitialized) return;
        sInitialized = true;
        startImmediateSync(context);


    }

    public static boolean startImmediateSync(@NonNull final Context context) {
        if (NetworkUtils.isConnected(context)) {
            Intent intentToSync = new Intent(context, BackgroundSyncIntentService.class);
            context.startService(intentToSync);
            CoordinatorLayout container = (CoordinatorLayout) ((Activity)context).findViewById(R.id.cl_container);
            Snackbar.make(container,"Data updated!", Snackbar.LENGTH_SHORT).show();
            return true;
        }else {
            CoordinatorLayout container = (CoordinatorLayout) ((Activity)context).findViewById(R.id.cl_container);
            Snackbar.make(container,"No internet connection!", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }
}
