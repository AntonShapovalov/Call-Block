package ru.org.adons.cblock.app;

import android.content.Context;
import android.content.SharedPreferences;

import ru.org.adons.cblock.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Wrapper for {@link SharedPreferences}
 */
public class Preferences {

    private static final String SERVICE_SWITCH_KEY = "SERVICE_SWITCH_KEY";

    private final SharedPreferences pref;

    Preferences(Context context) {
        pref = context.getSharedPreferences(context.getString(R.string.app_preferences_file), MODE_PRIVATE);
    }

    public boolean getServiceState() {
        return pref.getBoolean(SERVICE_SWITCH_KEY, false);
    }

    public void setServiceState(boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(SERVICE_SWITCH_KEY, value);
        editor.apply();
    }

}
