package io.lutics.app.android.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import io.lutics.app.android.service.GcmIntentService;
import io.lutics.app.android.util.Logger;

public class GcmWakefulBroadcastReceiver extends WakefulBroadcastReceiver {

    // Log Tag
    private static final String LOG_TAG = GcmWakefulBroadcastReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}