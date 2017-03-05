package ru.org.adons.cblock.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import ru.org.adons.cblock.db.DBContentProvider;
import ru.org.adons.cblock.db.PhonesTable;

public class ListLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private CursorAdapter adapter;

    public ListLoader(Context context, CursorAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, DBContentProvider.CONTENT_URI,
                PhonesTable.PHONES_SUMMARY_PROJECTION, null, null, PhonesTable.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
