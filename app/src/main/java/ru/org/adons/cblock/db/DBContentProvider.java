package ru.org.adons.cblock.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBContentProvider extends ContentProvider {

    public static final String AUTHORITY = "ru.org.adons.cblock.db";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PhonesTable.TABLE_NAME);
    public static final Uri CONTENT_ID_URI = Uri.parse("content://" + AUTHORITY + "/" + PhonesTable.TABLE_NAME + "/");

    public static final int PHONES = 1;
    private UriMatcher uriMatcher;
    private DBHelper dbHelper;

    public DBContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PhonesTable.TABLE_NAME, PHONES);
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
            long rowId = db.insert(PhonesTable.TABLE_NAME, null, values);
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
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
