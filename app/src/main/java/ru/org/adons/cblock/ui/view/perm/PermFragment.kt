package ru.org.adons.cblock.ui.view.perm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_perm.*
import ru.org.adons.cblock.R
import ru.org.adons.cblock.ui.activity.BaseFragment
import ru.org.adons.cblock.ui.activity.IMainListener


/**
 * Permission View, show warning if required permissions were not granted
 */
class PermFragment : BaseFragment<IMainListener>() {

    companion object {
        val PERM_FRAGMENT_TAG = "PERM_FRAGMENT_TAG"
    }

    @Suppress("OverridingDeprecatedMember", "DEPRECATION")
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        activity?.let { setListener(activity, IMainListener::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater
            .inflate(R.layout.fragment_perm, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSettings.setOnClickListener { showSetting() }
    }

    private fun showSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.data = Uri.parse("package:" + activity.packageName)
        startActivity(intent)
    }

}
