package io.lutics.app.android.activity;

import android.app.Activity;
import android.os.Bundle;

import io.lutics.app.android.R;

public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
    }
}
