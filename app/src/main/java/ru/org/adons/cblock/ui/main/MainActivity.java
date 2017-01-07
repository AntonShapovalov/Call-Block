package ru.org.adons.cblock.ui.main;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.Date;

import javax.inject.Inject;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.app.Preferences;
import ru.org.adons.cblock.ui.callog.AutoDataAdapter;
import ru.org.adons.cblock.ui.callog.AutoListItem;
import ru.org.adons.cblock.ui.callog.IncomingCallLoader;
import ru.org.adons.cblock.db.DBContentProvider;
import ru.org.adons.cblock.db.PhonesTable;
import ru.org.adons.cblock.service.BlockService;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "CBLOCK";
    public static final int NOTIFICATION_ID = 201507;
    // Action Bar and Action Button 'Start/Stop Service'
    private ActionBar actionBar;
    private boolean isServiceEnabled;
    private Intent serviceIntent;
    // Auto Text View for Incoming call
    private AutoCompleteTextView incomingPhoneView;
    private AutoListItem autoListItem;

    @Inject Preferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }
        setContentView(R.layout.activity_main);

        ((CBlockApplication) getApplication()).applicationComponent().inject(this);

        // set background for Action Bar and Action Button 'Start/Stop Service'
        isServiceEnabled = pref.getBoolean(getString(R.string.main_pref_key_switch));
        setActionBarBackground();
        serviceIntent = new Intent(this, BlockService.class);

        // set Incoming Call list
        AutoDataAdapter adapter = new AutoDataAdapter(this, android.R.id.text1);
        incomingPhoneView = (AutoCompleteTextView) findViewById(R.id.main_auto_text_phone_number);
        incomingPhoneView.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, new IncomingCallLoader(this, adapter));
        // always show all drop down on touch
        incomingPhoneView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                incomingPhoneView.showDropDown();
                incomingPhoneView.setError(null);
                return false;
            }
        });
        // store selected list item to get name
        incomingPhoneView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoListItem = (AutoListItem) parent.getAdapter().getItem(position);
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
        pref.putBoolean(getString(R.string.main_pref_key_switch), isServiceEnabled);
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
            // get Name
            String name = null;
            if (autoListItem != null) {
                name = autoListItem.getName();
                autoListItem = null;
            }
            new AddPhone().execute(new String[]{phoneNumber, name});
        }
    }

    private class AddPhone extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            ContentValues values = new ContentValues();
            values.put(PhonesTable.NUMBER, params[0]);
            values.put(PhonesTable.DATE, new Date().getTime());
            values.put(PhonesTable.CACHED_NAME, params[1]);
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
                color = getResources().getColor(R.color.green);
            } else {
                color = getResources().getColor(R.color.orange);
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

}
