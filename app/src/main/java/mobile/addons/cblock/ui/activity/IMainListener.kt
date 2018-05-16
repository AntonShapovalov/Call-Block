package mobile.addons.cblock.ui.activity

import mobile.addons.cblock.ui.view.add.AddFragment
import mobile.addons.cblock.ui.view.main.MainFragment

/**
 * Listen [MainFragment], [AddFragment] and notify [MainActivity]
 */
interface IMainListener {

    val mainComponent: MainComponent

    fun showAddFragment()

    fun showProgress()

    fun hideProgress()

    fun showError(message: String)

    fun onBackPressed()

}
