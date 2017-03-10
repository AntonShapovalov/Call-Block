package ru.org.adons.cblock.ui.adapter;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.util.Locale;

/**
 * Decorator to fill list item with data
 */

class ListItemDecorator {

    @SuppressWarnings("deprecation")
    static String formatPhoneNumber(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
        } else {
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
    }

    static String getDescription(String name, long date) {
        String desc = !TextUtils.isEmpty(name) ? name + ", " : "";
        return desc + DateUtils.getRelativeTimeSpanString(date);
    }

}
