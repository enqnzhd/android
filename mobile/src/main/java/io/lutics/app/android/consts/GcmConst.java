package io.lutics.app.android.consts;

import android.content.Context;
import android.content.SharedPreferences;

import io.lutics.app.android.util.Logger;

/**
 * @author Sunny-J
 * @description Google Cloud Message Const
 */
public class GcmConst {

    // Log Tag
    private static final String LOG_TAG = GcmConst.class.getName();

    // GCM - STATUS
    private static final String STATUS = "status";

    // GCM - API
    public static final String API_ID = "173174582647";
    public static final String API_KEY = "AIzaSyAY7SHCkPsiOTLoa_tVAbM0EcPdHuckEWw";

    // GCM - Notification Types
    public static final int NOTIFICATION_NORMAL = 1;

    // GCM - Is Notification Status
    public static boolean isNotificationStatus(Context context) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        return Integer.parseInt(getNotificationStatus(context)) > 0;
    }

    // GCM - Set Notification Status
    public static void setNotificationStatus(Context context, String value) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        SharedPreferences.Editor editor = context.getSharedPreferences(LOG_TAG, Context.MODE_PRIVATE).edit();
        editor.putString(STATUS, value);
        editor.apply();
    }

    // GCM - Get Notification Status
    public static String getNotificationStatus(Context context) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        SharedPreferences session = context.getSharedPreferences(LOG_TAG, Context.MODE_PRIVATE);

        return session.getString(STATUS, "1");
    }
}