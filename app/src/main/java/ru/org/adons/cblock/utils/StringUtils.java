package ru.org.adons.cblock.utils;

import android.os.Build;
import android.telephony.PhoneNumberUtils;

import java.util.Locale;

/**
 * String conversation utils
 */
public class StringUtils {

    @SuppressWarnings("deprecation")
    public static String formatPhoneNumber(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
        } else {
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
    }

}
