package mobile.addons.cblock.ui.activity

import dagger.Component
import mobile.addons.cblock.app.ApplicationComponent
import mobile.addons.cblock.scope.ViewScope
import mobile.addons.cblock.ui.view.add.AddFragment
import mobile.addons.cblock.ui.view.main.MainFragment

/**
 * Provide Application and data-model dependencies for View-Models
 */
@ViewScope
@Component(dependencies = [(ApplicationComponent::class)])
interface MainComponent {

    fun inject(mainFragment: MainFragment)

    fun inject(addFragment: AddFragment)

}
