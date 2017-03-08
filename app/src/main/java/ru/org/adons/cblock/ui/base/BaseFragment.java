package ru.org.adons.cblock.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle.components.RxFragment;

import ru.org.adons.cblock.utils.Logging;

/**
 * @param <T> - activity, must implement Fragment Interaction Listener
 */
public abstract class BaseFragment<T extends IBaseFragmentListener> extends RxFragment {

    private static final String LOG_TAG = "FRAGMENT_LIFECYCLE";
    protected T listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Logging.d(LOG_TAG, this.getClass(), "onAttach activity");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logging.d(LOG_TAG, this.getClass(), "onAttach context");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logging.d(LOG_TAG, this.getClass(), "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logging.d(LOG_TAG, this.getClass(), "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Logging.d(LOG_TAG, this.getClass(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logging.d(LOG_TAG, this.getClass(), "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logging.d(LOG_TAG, this.getClass(), "onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logging.d(LOG_TAG, this.getClass(), "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logging.d(LOG_TAG, this.getClass(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logging.d(LOG_TAG, this.getClass(), "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logging.d(LOG_TAG, this.getClass(), "onDestroy");
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
        Logging.d(LOG_TAG, this.getClass(), "onDetach");
    }

    protected <V extends Context> void setListener(V context, Class<T> tClass) {
        listener = tClass.cast(context);
    }

}
