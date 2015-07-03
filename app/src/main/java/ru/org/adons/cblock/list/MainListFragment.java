package ru.org.adons.cblock.list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import ru.org.adons.cblock.MainActivity;
import ru.org.adons.cblock.R;

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
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(MainActivity.LOG_TAG, "Item clicked: " + id);
                DialogFragment newFragment = DeleteAlertDialog.newInstance(id);
                newFragment.show(getFragmentManager(), "dialog");
                return true;
            }
        });
    }

    /**
     * Handle Delete item
     */
    public static class DeleteAlertDialog extends DialogFragment {

        public static DeleteAlertDialog newInstance(long rowID) {
            DeleteAlertDialog frag = new DeleteAlertDialog();
            Bundle args = new Bundle();
            args.putLong("rowID", rowID);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final long rowID = getArguments().getLong("rowID");
            return new AlertDialog.Builder(getActivity())
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setTitle(R.string.list_alert_dialog_delete_title)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((MainActivity) getActivity()).deletePhone(rowID);
                                }
                            }
                    )
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // do nothing
                                }
                            }
                    )
                    .create();
        }
    }

}
