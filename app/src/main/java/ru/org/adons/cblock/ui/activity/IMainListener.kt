package ru.org.adons.cblock.ui.activity

import ru.org.adons.cblock.ui.view.add.AddFragment
import ru.org.adons.cblock.ui.view.main.MainFragment

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
