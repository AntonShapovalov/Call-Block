package ru.org.adons.cblock.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import ru.org.adons.cblock.MainActivity;

public class DBContentProvider extends ContentProvider {

    public static final String AUTHORITY = "ru.org.adons.cblock.db";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PhonesTable.TABLE_NAME);
    public static final Uri CONTENT_ID_URI = Uri.parse("content://" + AUTHORITY + "/" + PhonesTable.TABLE_NAME + "/");

    public static final int PHONES = 1;
    public static final int PHONE_ID = 2;
    private UriMatcher uriMatcher;
    private DBHelper dbHelper;

    public DBContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PhonesTable.TABLE_NAME, PHONES);
        uriMatcher.addURI(AUTHORITY, PhonesTable.TABLE_NAME + "/#", PHONE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == PHONES) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(PhonesTable.TABLE_NAME, projection, selection,
                    selectionArgs, null /*no group*/, null /*no filter*/, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) == PHONES) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long rowId = 0;
            try {
                rowId = db.insert(PhonesTable.TABLE_NAME, null, values);
            } catch (SQLiteConstraintException e) {
                Log.e(MainActivity.LOG_TAG, "Unique constraint");
                return null;
            }
            if (rowId > 0) {
                Uri noteUri = ContentUris.withAppendedId(CONTENT_ID_URI, rowId);
                getContext().getContentResolver().notifyChange(noteUri, null);
                return noteUri;
            } else {
                throw new SQLException("Failed to insert row into " + uri);
            }
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == PHONE_ID) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowId = db.delete(PhonesTable.TABLE_NAME, selection, selectionArgs);
            if (rowId > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return rowId;
            } else {
                throw new SQLException("Failed to delete row " + uri);
            }
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
