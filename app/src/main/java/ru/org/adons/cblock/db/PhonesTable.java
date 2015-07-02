package ru.org.adons.cblock.db;

import android.provider.CallLog;

public final class PhonesTable extends CallLog.Calls {

    private PhonesTable() {
    }

    public static final String TABLE_NAME = "phones";
    public static final String[] PHONES_SUMMARY_PROJECTION = new String[]{
            _ID,
            NUMBER,
            DATE
    };
    public static final int COLUMN_NUMBER_INDEX = 1;
    public static final int COLUMN_DATE_INDEX = 2;

}
