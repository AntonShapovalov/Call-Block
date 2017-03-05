package ru.org.adons.cblock.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.org.adons.cblock.utils.Logging;

/**
 * Base Activity class to log lifecycle and define base Fragment Interaction Listener
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ACTIVITY_LIFECYCLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logging.d(LOG_TAG, this.getClass(), "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logging.d(LOG_TAG, this.getClass(), "onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Logging.d(LOG_TAG, this.getClass(), "onRestoreInstanceState");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Logging.d(LOG_TAG, this.getClass(), "onPostCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logging.d(LOG_TAG, this.getClass(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logging.d(LOG_TAG, this.getClass(), "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logging.d(LOG_TAG, this.getClass(), "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logging.d(LOG_TAG, this.getClass(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logging.d(LOG_TAG, this.getClass(), "onDestroy");
    }

}
