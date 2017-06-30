package ru.org.adons.cblock.ui.activity

import dagger.Component
import ru.org.adons.cblock.app.ApplicationComponent
import ru.org.adons.cblock.scope.ViewScope
import ru.org.adons.cblock.ui.view.add.AddFragment
import ru.org.adons.cblock.ui.view.main.MainFragment

/**
 * Provide Application and data-model dependencies for View-Models
 */
@ViewScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface MainComponent {

    fun inject(mainFragment: MainFragment)

    fun inject(addFragment: AddFragment)

}
