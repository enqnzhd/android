package io.lutics.app.android.util;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.WebView;

/**
 * @author Sunny-J
 * @description Base Settings for WebView
 */
public class WebBaseSettings {

    // Log Tag
    private static final String LOG_TAG = WebBaseSettings.class.getName();

    // Settings for WebView
    public static void setWebViewSettings(final WebView webView) {
        // Logging
        Logger.d(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        // Setting - Script
        webView.getSettings().setJavaScriptEnabled(true);

        // Setting - Web
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportMultipleWindows(true);

        // Setting - Local Storage & Inner Database
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Setting - Download Listener
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                webView.getContext().startActivity(intent);
            }
        });
    }
}