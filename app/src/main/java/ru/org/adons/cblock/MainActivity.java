package ru.org.adons.cblock;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import ru.org.adons.cblock.auto.AutoTextDataAdapter;
import ru.org.adons.cblock.auto.AutoIncomingCallLoader;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "CBLOCK";
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_main);

        AutoTextDataAdapter adapter = new AutoTextDataAdapter(this, android.R.id.text1);
        final AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.main_auto_text_phone_number);
        tv.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, new AutoIncomingCallLoader(this, adapter));

        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tv.showDropDown();
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

    public void startService(MenuItem item) {
        int color = getResources().getColor(R.color.green01);
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
        Log.d(LOG_TAG, "startService");
    }
}
