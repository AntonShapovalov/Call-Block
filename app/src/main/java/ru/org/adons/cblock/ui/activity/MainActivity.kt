package ru.org.adons.cblock.ui.activity

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.org.adons.cblock.R
import ru.org.adons.cblock.app.CBlockApplication
import ru.org.adons.cblock.ext.*
import ru.org.adons.cblock.service.BlockService
import ru.org.adons.cblock.ui.view.add.AddFragment
import ru.org.adons.cblock.ui.view.main.MainFragment
import ru.org.adons.cblock.ui.view.perm.PermFragment
import ru.org.adons.cblock.ui.view.perm.PermViewModel
import rx.Completable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity(), IMainListener {

    private val PERMISSIONS_REQUEST = 311

    private val permViewModel = PermViewModel()

    override val mainComponent: MainComponent by lazy { buildMainComponent() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        hideProgress()
        requestPermissions()
    }

    private fun requestPermissions() {
        val perm = permViewModel.getPermissionsRequest(this)
        when {
            perm.isShowWarning -> showPermFragment()
            perm.request.isNotEmpty() -> ActivityCompat.requestPermissions(this, perm.request, PERMISSIONS_REQUEST)
            else -> initComponents()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST) {
            val granted = permViewModel.isPermissionsGranted(permissions, grantResults)
            if (granted) initComponents() else showPermFragment()
        }
    }

    /**
     * Init all required Dagger components in background thread
     */
    private fun initComponents() {
        logThis("initComponents")
        Completable.fromAction { mainComponent }
                .doOnSubscribe { showProgress() }
                .doOnUnsubscribe { hideProgress() }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onComponentsReady() }
    }

    private fun onComponentsReady() {
        logThis("onComponentsReady")
        var fragment = getFragment(MainFragment.MAIN_FRAGMENT_TAG)
        if (fragment == null) {
            clearFragmentBackStack()
            fragment = MainFragment()
            replaceFragment(R.id.fragment_container, fragment, MainFragment.MAIN_FRAGMENT_TAG)
        }
    }

    private fun buildMainComponent(): MainComponent {
        val application = application as CBlockApplication
        return DaggerMainComponent.builder()
                .applicationComponent(application.applicationComponent)
                .build()
    }

    override fun showProgress() = runOnUiThread { progress.visibility = View.VISIBLE }

    override fun hideProgress() = runOnUiThread { progress.visibility = View.INVISIBLE }

    override fun showError(message: String) = runOnUiThread { toast(message) }

    override fun showAddFragment() {
        var fragment = getFragment(AddFragment.ADD_FRAGMENT_TAG)
        if (fragment == null) {
            fragment = AddFragment()
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, AddFragment.ADD_FRAGMENT_TAG)
        transaction.addToBackStack(MainFragment.MAIN_FRAGMENT_TAG)
        transaction.commit()
    }

    private fun showPermFragment() {
        BlockService.stop(this)
        clearFragmentBackStack()
        var fragment = getFragment(PermFragment.PERM_FRAGMENT_TAG)
        if (fragment == null) {
            fragment = PermFragment()
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, PermFragment.PERM_FRAGMENT_TAG).commit()
        }
    }

}
