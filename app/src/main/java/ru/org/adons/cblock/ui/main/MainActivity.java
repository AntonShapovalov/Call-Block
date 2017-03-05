package ru.org.adons.cblock.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import javax.inject.Inject;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.app.Preferences;
import ru.org.adons.cblock.service.BlockService;

public class MainActivity extends AppCompatActivity {

    @Inject Preferences pref;
    private boolean isServiceEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CBlockApplication) getApplication()).applicationComponent().inject(this);

        isServiceEnabled = pref.getBoolean(getString(R.string.main_pref_key_switch));
    }

    /**
     * Handle click switch 'Start/Stop Service'
     */
    public void startService(MenuItem item) {
        if (isServiceEnabled) {
            isServiceEnabled = false;
            BlockService.enable(this);
        } else {
            isServiceEnabled = true;
            BlockService.disable(this);
        }
        pref.putBoolean(getString(R.string.main_pref_key_switch), isServiceEnabled);
    }

}
