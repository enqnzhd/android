package io.lutics.app.android.util;

import android.util.Log;

import io.lutics.app.android.BuildConfig;

/**
 * @author Sunny-J
 * @description Logger
 */
public class Logger {
    // Black
    public static void v(String LOG_TAG, String msg) {
        if (BuildConfig.LOG) Log.v(LOG_TAG, msg);
    }

    // Blue
    public static void d(String LOG_TAG, String msg) {
        if (BuildConfig.LOG) Log.d(LOG_TAG, msg);
    }

    // Red
    public static void e(String LOG_TAG, String msg) {
        if (BuildConfig.LOG) Log.e(LOG_TAG, msg);
    }

    // Green
    public static void i(String LOG_TAG, String msg) {
        if (BuildConfig.LOG) Log.i(LOG_TAG, msg);
    }

    // Yellow
    public static void w(String LOG_TAG, String msg) {
        if (BuildConfig.LOG) Log.w(LOG_TAG, msg);
    }

    // Parse To Json Tree
    public static String parseToJsonTree(String msg) throws NullPointerException {
        final String indentSpace = "    ";
        int indentDepth = 0;
        String targetString = null;

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < msg.length(); i++) {
            targetString = msg.substring(i, i + 1);

            if (targetString.equals("{") || targetString.equals("[")) {
                sb.append(targetString).append("\n");
                indentDepth++;

                for (int j = 0; j < indentDepth; j++) {
                    sb.append(indentSpace);
                }
            } else if (targetString.equals("}") || targetString.equals("]")) {
                sb.append("\n");
                indentDepth--;

                for (int j = 0; j < indentDepth; j++) {
                    sb.append(indentSpace);
                }
                sb.append(targetString);
            } else if (targetString.equals(",")) {
                sb.append(targetString);
                sb.append("\n");

                for (int j = 0; j < indentDepth; j++) {
                    sb.append(indentSpace);
                }
            } else {
                sb.append(targetString);
            }
        }

        return sb.toString();
    }
}