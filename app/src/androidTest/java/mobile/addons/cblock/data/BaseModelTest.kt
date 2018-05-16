package mobile.addons.cblock.data

import android.support.test.InstrumentationRegistry

import mobile.addons.cblock.app.CBlockApplication
import mobile.addons.cblock.app.DaggerTestComponent
import mobile.addons.cblock.app.TestComponent

/**
 * Base class for data model testing
 */
abstract class BaseModelTest {

    fun testComponent(): TestComponent {
        val application = InstrumentationRegistry.getTargetContext().applicationContext as CBlockApplication
        return DaggerTestComponent.builder()
                .applicationComponent(application.applicationComponent)
                .build()
    }

}
