package ru.org.adons.cblock.ui.fragment;

import android.content.Context;
import android.util.Log;

import com.trello.rxlifecycle.components.RxFragment;

import ru.org.adons.cblock.utils.Logging;

/**
 * @param <T> - activity, must implement Fragment Interaction Listener
 */
public abstract class BaseFragment<T extends IBaseFragmentListener> extends RxFragment {

    protected T listener;

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    /**
     * Set Activity as listener on fragment's events
     *
     * @param context Host Activity
     * @param tClass  listener class, which host activity must implement
     */
    protected <V extends Context> void setListener(V context, Class<T> tClass) {
        listener = tClass.cast(context);
    }

    /**
     * Default handling of error, child fragment can override this to provide own error handling
     *
     * @param t throwable
     */
    protected void onError(Throwable t) {
        Logging.e(Log.getStackTraceString(t));
        listener.showError(t.getLocalizedMessage());
    }

}
