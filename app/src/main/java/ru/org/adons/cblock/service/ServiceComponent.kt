package ru.org.adons.cblock.service

import dagger.Component
import ru.org.adons.cblock.app.ApplicationComponent
import ru.org.adons.cblock.scope.ViewScope

/**
 * Provide Application and data-model dependencies for Service
 */
@ViewScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface ServiceComponent {

    fun inject(service: BlockService)

}
