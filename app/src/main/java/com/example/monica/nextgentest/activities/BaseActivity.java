package com.example.monica.nextgentest.activities;

import android.app.Activity;

/**
 * Created by monica on 10/25/16.
 */

public class BaseActivity extends Activity {
    private boolean visible = false;

    @Override
    protected void onResume() {
        super.onResume();
        visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }
}
