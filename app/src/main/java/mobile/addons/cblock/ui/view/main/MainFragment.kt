package mobile.addons.cblock.ui.view.main

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import mobile.addons.cblock.R
import mobile.addons.cblock.app.BlockManager
import mobile.addons.cblock.ext.initList
import mobile.addons.cblock.ext.toastBottom
import mobile.addons.cblock.ui.activity.BaseFragment
import mobile.addons.cblock.ui.activity.IMainListener
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Main View, show list of blocking numbers
 */
class MainFragment : BaseFragment<IMainListener>() {

    companion object {
        const val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
        private const val SWITCH_KEY = "SWITCH_KEY"
    }

    private var isSwitchChecked = false

    private val adapter = BlockListAdapter { mainViewModel.deletePhone(it) }

    @Inject lateinit var blockManager: BlockManager
    @Inject lateinit var mainViewModel: MainViewModel

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        activity?.let { setListener(activity, IMainListener::class.java) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener?.mainComponent?.inject(this)
        savedInstanceState?.let { isSwitchChecked = savedInstanceState.getBoolean(SWITCH_KEY) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater
            .inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listBlockItems.initList(adapter)
        fabAdd.setOnClickListener { listener?.showAddFragment() }
        getServiceState()
        switchService.setOnCheckedChangeListener { _, isChecked -> changeServiceState(isChecked) }
    }

    override fun onResume() {
        super.onResume()
        getBlockList()
        getBlockListUpdate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(SWITCH_KEY, isSwitchChecked)
        super.onSaveInstanceState(outState)
    }

    // get Service State from Preferences
    private fun getServiceState() {
        mainViewModel.getServiceState()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe({ switchService.isChecked = it }, this::onError)
    }

    // change and save Service state on Switch click
    private fun changeServiceState(isChecked: Boolean) {
        Observable.just(isChecked)
                .filter { isSwitchChecked != it }
                .doOnNext { isSwitchChecked = it }
                .flatMap { mainViewModel.changeServiceState(it) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(this::toastBottom, this::onError)
    }

    // get block list from local DB
    private fun getBlockList() {
        mainViewModel.getBlockList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnSubscribe { listener?.showProgress() }
                .doOnUnsubscribe { listener?.hideProgress() }
                .subscribe(adapter::setItems, this::onError)
    }

    // get update event from BlockManager
    private fun getBlockListUpdate() {
        blockManager.getBlockListUpdate()
                .compose(bindToLifecycle())
                .subscribe(adapter::setItems)
    }

}
