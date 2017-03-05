package ru.org.adons.cblock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.org.adons.cblock.ui.main.MainActivity;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cblock.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PhonesTable.TABLE_NAME + " ("
                + PhonesTable._ID + " INTEGER PRIMARY KEY,"
                + PhonesTable.NUMBER + " TEXT,"
                + PhonesTable.DATE + " INTEGER,"
                + PhonesTable.CACHED_NAME + " TEXT"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PhonesTable.TABLE_NAME);
        onCreate(db);
    }
}
