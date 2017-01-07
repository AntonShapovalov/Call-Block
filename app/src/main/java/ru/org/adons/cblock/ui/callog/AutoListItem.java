package ru.org.adons.cblock.ui.callog;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

public class AutoListItem {

    private Context context;
    private String phoneNumber;
    private String date;
    private String name;

    public AutoListItem(Context context, String phoneNumber, String date, String name) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.name = name;
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

    public String getName() {
        return name;
    }

    // AutoCompleteTextView call this to get Text
    @Override
    public String toString() {
        return getPhoneNumber();
    }
}
