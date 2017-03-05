package ru.org.adons.cblock.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.ui.dialog.BaseDialogFragment;

/**
 * Delete number from block list confirmation dialog
 */

public class DeleteDialogFragment extends BaseDialogFragment<IDeleteDialogListener> {

    public static final String DELETE_DIALOG_FRAGMENT_TAG = "DELETE_DIALOG_FRAGMENT_TAG";
    private static final String ITEM_ID_KEY = "ITEM_ID_KEY";
    private long itemId;

    public static DeleteDialogFragment newInstance(long itemId) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ITEM_ID_KEY, itemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setListener(context, IDeleteDialogListener.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getLong(ITEM_ID_KEY);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.list_alert_dialog_delete_title)
                .setPositiveButton(android.R.string.ok,
                        (dialog, whichButton) -> {
                            listener.removeNumberFromList(itemId);
                        }
                )
                .setNegativeButton(android.R.string.cancel,
                        (dialog, whichButton) -> {
                            // do nothing, just close dialog
                        }
                )
                .create();
    }

}
