package mobile.addons.cblock.ui.activity

import android.content.Context
import android.util.Log
import com.trello.rxlifecycle.components.RxFragment
import mobile.addons.cblock.ext.Type
import mobile.addons.cblock.ext.log

/**
 * @param <T> - activity, must implement Fragment Interaction Listener
 */
abstract class BaseFragment<T : IMainListener> : RxFragment() {

    protected var listener: T? = null

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    /**
     * Set Activity as listener on fragment's events

     * @param context Host Activity
     * *
     * @param tClass  listener class, which host activity must implement
     */
    protected fun <V : Context> setListener(context: V, tClass: Class<T>) {
        listener = tClass.cast(context)
    }

    /**
     * Default handling of error, child fragment can override this to provide own error handling

     * @param t throwable
     */
    protected fun onError(t: Throwable) {
        log(Log.getStackTraceString(t), Type.ERROR)
        listener?.showError(t.localizedMessage)
    }

}
