package ru.org.adons.cblock.ui.dialog;

import android.content.Context;
import android.support.v4.app.DialogFragment;

import ru.org.adons.cblock.utils.Logging;

/**
 * Base Fragment for all Dialogs, field {@link BaseDialogFragment#listener} is Activity, which hold fragment
 */
public class BaseDialogFragment<T extends IBaseDialogListener> extends DialogFragment {

    protected T listener;

    public <V extends Context> void setListener(V context, Class<T> tClass) {
        try {
            listener = tClass.cast(context);
        } catch (ClassCastException e) {
            Logging.d(context.getClass().getSimpleName() + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

}
