package ru.org.adons.cblock.app

import dagger.Component
import ru.org.adons.cblock.data.BlockListModelTest
import ru.org.adons.cblock.data.CallLogModelTest
import ru.org.adons.cblock.scope.ViewScope

/**
 * Provide Application and data-model dependencies for test
 */
@ViewScope
@Component(dependencies = arrayOf(ApplicationComponent::class))
interface TestComponent {

    fun inject(test: CallLogModelTest)

    fun inject(test: BlockListModelTest)

}
