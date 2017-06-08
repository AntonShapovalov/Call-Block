package ru.org.adons.cblock.utils;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.util.Locale;

/**
 * Helper for UI
 */

public class UiUtils {

    /**
     * Default recycler list initialization
     *
     * @param list    recycler view list
     * @param adapter recycler adapter
     */
    public static void initList(Context context, RecyclerView list, RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
    }


    @SuppressWarnings("deprecation")
    public static String formatPhoneNumber(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
        } else {
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
    }

    public static String getDescription(String name, long date) {
        String desc = !TextUtils.isEmpty(name) ? name + ", " : "";
        return desc + DateUtils.getRelativeTimeSpanString(date);
    }

}
