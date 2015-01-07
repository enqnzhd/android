package io.lutics.app.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.lutics.app.android.consts.GacConst;
import io.lutics.app.android.util.Logger;

/**
 * @author Sunny-J
 * @description Activity for Non-Service Dependencies
 */
public class BaseActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Log Tag
    private static final String LOG_TAG = BaseActivity.class.getSimpleName();

    // Variable
    protected GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Google Api Client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.APP_INDEX_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Google Api Client
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Google Api Client
        googleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Google Api Client
        if (requestCode == GacConst.REQUEST_RESOLVE_ERROR) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
    }

    /**
     * @author Sunny-J
     * @description Implements for Google Api Client
     */
    @Override
    public void onConnected(Bundle bundle) {
        // Logging
        Logger.w(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // Logging
        Logger.w(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + cause);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Logging
        Logger.w(LOG_TAG, Thread.currentThread().getStackTrace()[2].getMethodName());

        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, GacConst.REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                googleApiClient.connect();
            }
        } else {
            GacConst.GacDialogFragment dialogFragment = new GacConst.GacDialogFragment();
            Bundle args = new Bundle();
            args.putInt(GacConst.GacDialogFragment.ERROR_CODE, connectionResult.getErrorCode());
            dialogFragment.setArguments(args);
            dialogFragment.show(getFragmentManager(), GacConst.GacDialogFragment.class.getName());
        }
    }
}