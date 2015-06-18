package ru.org.adons.cblock;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Random;

public class MainActivity extends AppCompatListActivity {

    private static final String LOG_TAG = "CBLOCK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void startService(MenuItem item) {
        int color;
        Random r = new Random();
        if (r.nextInt(10) % 2 == 0) {
            color = R.color.green01;
        } else {
            color = R.color.orange01;
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        Log.d(LOG_TAG, "startService");
    }
}
