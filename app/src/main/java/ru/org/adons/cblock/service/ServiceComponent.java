package ru.org.adons.cblock.service;

import dagger.Component;
import ru.org.adons.cblock.app.ApplicationComponent;
import ru.org.adons.cblock.scope.ViewScope;

/**
 * Provide Application and data-model dependencies for Service
 */
@SuppressWarnings("WeakerAccess")
@ViewScope
@Component(dependencies = ApplicationComponent.class)
public interface ServiceComponent {

    void inject(BlockService service);

}
