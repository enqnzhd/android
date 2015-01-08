package io.lutics.app.android.activity;

import android.os.Bundle;

import io.lutics.app.android.R;
import io.lutics.app.android.util.GcmRegistrationAsyncTask;

public class IntroActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new GcmRegistrationAsyncTask(this).execute();
    }
}
