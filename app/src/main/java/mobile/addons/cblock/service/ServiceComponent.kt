package mobile.addons.cblock.service

import dagger.Component
import mobile.addons.cblock.app.ApplicationComponent
import mobile.addons.cblock.scope.ViewScope

/**
 * Provide Application and data-model dependencies for Service
 */
@ViewScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ServiceComponent {

    fun inject(service: BlockService)

}
