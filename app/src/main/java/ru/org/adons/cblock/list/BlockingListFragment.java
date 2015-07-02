package ru.org.adons.cblock.list;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import ru.org.adons.cblock.MainActivity;

public class BlockingListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No phone numbers");
        setListShown(false);

        BlockingListDataAdapter adapter = new BlockingListDataAdapter(getActivity());
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, new BlockingCallLoader(getActivity(), adapter));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(MainActivity.LOG_TAG, "Item clicked: " + id);
    }
}
