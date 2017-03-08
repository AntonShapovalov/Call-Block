package ru.org.adons.cblock.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import javax.inject.Inject;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.app.Preferences;
import ru.org.adons.cblock.service.BlockService;

public class MainActivity extends AppCompatActivity implements IMainListener {

    @Inject Preferences pref;
    private boolean isServiceEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CBlockApplication) getApplication()).applicationComponent().inject(this);

        isServiceEnabled = pref.getBoolean(getString(R.string.main_pref_key_switch));

        MainFragment fragment = getMainFragment();
        if (fragment == null) {
            fragment = new MainFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, MainFragment.MAIN_FRAGMENT_TAG).commit();
        }
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.MAIN_FRAGMENT_TAG);
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
