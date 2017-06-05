package ru.org.adons.cblock.data;

import android.support.test.InstrumentationRegistry;

import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.app.DaggerTestComponent;
import ru.org.adons.cblock.app.TestComponent;

/**
 * Base class for data model testing
 */
abstract class BaseModelTest {

    TestComponent testComponent() {
        CBlockApplication application = (CBlockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        return DaggerTestComponent.builder()
                .applicationComponent(application.applicationComponent())
                .build();
    }

}
