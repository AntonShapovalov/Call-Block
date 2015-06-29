package ru.org.adons.cblock.auto;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

public class AutoTextItem {

    private Context context;
    private String phoneNumber;
    private String date;

    public AutoTextItem(Context context, String phoneNumber, String date) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        setDate(date);
    }

    public void setDate(String date) {
        StringBuilder sb = new StringBuilder();
        Date dt = new Date(Long.parseLong(date));
        sb.append(DateFormat.getDateFormat(context).format(dt));
        sb.append(" ");
        sb.append(DateFormat.getTimeFormat(context).format(dt));
        this.date = sb.toString();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDate() {
        return date;
    }

    // AutoCompleteTextView call this to get Text
    @Override
    public String toString() {
        return getPhoneNumber();
    }
}
