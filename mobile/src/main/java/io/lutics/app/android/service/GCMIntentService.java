package io.lutics.app.android.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import io.lutics.app.android.BuildConfig;
import io.lutics.app.android.receiver.GcmWakefulBroadcastReceiver;
import io.lutics.app.android.util.Logger;

/**
 * @author Sunny-J
 * @description GCM Background-Service ( Using 'Google Play Services' Library )
 */
public class GcmIntentService extends IntentService {

    // Log Tag
    private static final String LOG_TAG = GcmIntentService.class.getName();

    // Property
    private static final String PROPERTY_APP_VERSION = "app_version";
    private static final String PROPERTY_REG_ID = "registration_id";


	/* Constructor
    ---------------------------------------------------------------------------------- */

    public GcmIntentService() {
        super(LOG_TAG);
    }


	/* Overrides
    ---------------------------------------------------------------------------------- */

    @Override
    protected void onHandleIntent(Intent intent) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        // Receive GCM
        String messageType = GoogleCloudMessaging.getInstance(getBaseContext()).getMessageType(intent);
        if (!messageType.isEmpty()) {
            switch (messageType) {
                case GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE: {
                    break;
                }

                case GoogleCloudMessaging.MESSAGE_TYPE_DELETED: {
                    break;
                }

                case GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR: {
                    break;
                }

                default:
                    break;
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmWakefulBroadcastReceiver.completeWakefulIntent(intent);
    }


	/* Functions
    ---------------------------------------------------------------------------------- */

    private void showNotification() {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        NotificationCompat notificationCompat = new NotificationCompat();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify();
    }

    private String getRegistrationId() {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        final SharedPreferences prefs = getSharedPreferences(LOG_TAG, Context.MODE_PRIVATE);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            Logger.i(LOG_TAG, "Registration not found.");
            return "";
        }

        // Check if App was updated
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = BuildConfig.VERSION_CODE;

        if (registeredVersion != currentVersion) {
            Logger.i(LOG_TAG, "App version changed or registration expired.");
            return "";
        }

        return registrationId;
    }

    private void setRegistrationId(String deviceToken) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + deviceToken);

        Editor editor = getSharedPreferences(LOG_TAG, Context.MODE_PRIVATE).edit();
        editor.putString(PROPERTY_REG_ID, deviceToken);
        editor.putInt(PROPERTY_APP_VERSION, BuildConfig.VERSION_CODE);
        editor.commit();
    }
}