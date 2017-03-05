package ru.org.adons.cblock.ui.main;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;

public class MainListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No phone numbers");
        setListShown(false);

        ListDataAdapter adapter = new ListDataAdapter(getActivity());
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, new ListLoader(getActivity(), adapter));

        // handle delete item
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener((parent, view, position, id) -> {
            DialogFragment newFragment = DeleteDialogFragment.newInstance(id);
            newFragment.show(getFragmentManager(), "dialog");
            return true;
        });
    }

}
