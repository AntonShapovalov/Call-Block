package mobile.addons.cblock.app

import dagger.Component
import mobile.addons.cblock.data.BlockListModelTest
import mobile.addons.cblock.data.CallLogModelTest
import mobile.addons.cblock.scope.ViewScope

/**
 * Provide Application and data-model dependencies for test
 */
@ViewScope
@Component(dependencies = [(ApplicationComponent::class)])
interface TestComponent {

    fun inject(test: CallLogModelTest)

    fun inject(test: BlockListModelTest)

}
