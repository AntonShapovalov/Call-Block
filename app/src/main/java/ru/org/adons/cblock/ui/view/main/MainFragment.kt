package ru.org.adons.cblock.ui.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import ru.org.adons.cblock.R
import ru.org.adons.cblock.app.BlockManager
import ru.org.adons.cblock.ext.toastBottom
import ru.org.adons.cblock.ui.activity.IMainListener
import ru.org.adons.cblock.ui.fragment.BaseFragment
import ru.org.adons.cblock.utils.UiUtils
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Main View, show list of blocking numbers
 */
class MainFragment : BaseFragment<IMainListener>() {

    companion object {
        val MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG"
    }

    private val SWITCH_KEY = "SWITCH_KEY"
    private var isSwitchChecked = false

    private val adapter = BlockListAdapter { mainViewModel.deletePhone(it) }

    @Inject lateinit var blockManager: BlockManager
    @Inject lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setListener(activity, IMainListener::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener.mainComponent().inject(this)
        if (savedInstanceState != null) {
            isSwitchChecked = savedInstanceState.getBoolean(SWITCH_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UiUtils.initList(activity, listBlockItems, adapter)
        fabAdd.setOnClickListener { listener.showAddFragment() }
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
                .doOnSubscribe { listener.showProgress() }
                .doOnUnsubscribe { listener.hideProgress() }
                .subscribe(adapter::setItems, this::onError)
    }

    // get update event from BlockManager
    private fun getBlockListUpdate() {
        blockManager.getBlockListUpdate()
                .compose(bindToLifecycle())
                .subscribe(adapter::setItems)
    }

}
