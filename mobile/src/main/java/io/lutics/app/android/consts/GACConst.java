package io.lutics.app.android.consts;

import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;

import io.lutics.app.android.BuildConfig;

/**
 * @author Sunny-J
 * @description Google Api Client Const
 */
public class GacConst {

    // Log Tag
    private static final String LOG_TAG = GacConst.class.getName();

    // Common
    public static final int REQUEST_RESOLVE_ERROR = 1001;

    // Api - App-Indexing
    public static Uri getAppIndexingAppUri(String url) {
        return Uri.parse("android-app://" + BuildConfig.APPLICATION_ID + "/" + url.replaceFirst("://", "/"));
    }

    public static Uri getAppIndexingWebUri(String url) {
        return Uri.parse(url);
    }

    // Api
    public static class GacDialogFragment extends DialogFragment {

        // Variable
        public static final String ERROR_CODE = "error_code";

        // Constructor
        public GacDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(ERROR_CODE);

            return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), REQUEST_RESOLVE_ERROR);
        }
    }
}