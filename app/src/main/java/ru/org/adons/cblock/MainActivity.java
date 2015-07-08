package ru.org.adons.cblock;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.Date;

import ru.org.adons.cblock.auto.AutoDataAdapter;
import ru.org.adons.cblock.auto.IncomingCallLoader;
import ru.org.adons.cblock.db.DBContentProvider;
import ru.org.adons.cblock.db.PhonesTable;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "CBLOCK";
    public static final int NOTIFICATION_ID = 201507;
    // Action Bar and Action Button 'Start/Stop Service'
    private ActionBar actionBar;
    private SharedPreferences pref;
    private boolean isServiceEnabled;
    private Intent serviceIntent;
    // Auto Text View for Incoming call
    private AutoCompleteTextView incomingPhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main);

        // set background for Action Bar and Action Button 'Start/Stop Service'
        pref = getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE);
        isServiceEnabled = pref.getBoolean(getString(R.string.main_pref_key_switch), false);
        setActionBarBackground();
        serviceIntent = new Intent(this, BlockService.class);

        // set Incoming Call list
        AutoDataAdapter adapter = new AutoDataAdapter(this, android.R.id.text1);
        incomingPhoneView = (AutoCompleteTextView) findViewById(R.id.main_auto_text_phone_number);
        incomingPhoneView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, new IncomingCallLoader(this, adapter));

        incomingPhoneView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                incomingPhoneView.showDropDown();
                incomingPhoneView.setError(null);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle click Action Button 'Start/Stop Service'
     */
    public void startService(MenuItem item) {
        Toast toast;
        if (isServiceEnabled) {
            isServiceEnabled = false;
            stopService(serviceIntent);
            toast = Toast.makeText(this, R.string.main_service_disabled_message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 8);
            toast.show();
            Log.d(LOG_TAG, "stopService");
        } else {
            isServiceEnabled = true;
            startService(serviceIntent);
            toast = Toast.makeText(this, R.string.main_service_enabled_message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 8);
            toast.show();
            Log.d(LOG_TAG, "startService");
        }
        setActionBarBackground();
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getString(R.string.main_pref_key_switch), isServiceEnabled);
        editor.commit();
    }

    /**
     * Handle click Image Button 'Add Phone Number'
     */
    public void addPhone(View view) {
        String phoneNumber = incomingPhoneView.getText().toString();
        if (!(TextUtils.isEmpty(phoneNumber))) {
            phoneNumber = phoneNumber.replaceAll("\\s+", "");
            // check Phone Number is valid
            if (!phoneNumber.matches("\\+\\d+")) {
                incomingPhoneView.setError(getString(R.string.main_auto_text_validation));
                return;
            } else {
                incomingPhoneView.setError(null);
            }
            new AddPhone().execute(phoneNumber);
        }
    }

    private class AddPhone extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            ContentValues values = new ContentValues();
            values.put(PhonesTable.NUMBER, params[0]);
            values.put(PhonesTable.DATE, new Date().getTime());
            getContentResolver().insert(DBContentProvider.CONTENT_URI, values);
            return null;
        }
    }

    /**
     * Handle LongClick FragmentList : Delete item
     */
    public void deletePhone(long rowID) {
        new DeletePhone().execute(rowID);
    }

    private class DeletePhone extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... params) {
            long rowID = params[0];
            String where = "(" + PhonesTable._ID + " = " + rowID + ")";
            Uri uri = ContentUris.withAppendedId(DBContentProvider.CONTENT_ID_URI, rowID);
            getContentResolver().delete(uri, where, null);
            return null;
        }
    }

    private void setActionBarBackground() {
        if (actionBar != null) {
            int color;
            if (isServiceEnabled) {
                color = getResources().getColor(R.color.green01);
            } else {
                color = getResources().getColor(R.color.orange01);
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

}
