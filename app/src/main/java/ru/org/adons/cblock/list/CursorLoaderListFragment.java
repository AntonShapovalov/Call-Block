package ru.org.adons.cblock.list;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import ru.org.adons.cblock.MainActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class CursorLoaderListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No phone numbers");
        setListShown(false);

        IncomingCallAdapter adapter = new IncomingCallAdapter(getActivity());
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, new IncomingCallLoader(getActivity(), adapter));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(MainActivity.LOG_TAG, "Item clicked: " + id);
    }
}
