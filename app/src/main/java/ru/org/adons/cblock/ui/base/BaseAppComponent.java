package ru.org.adons.cblock.ui.base;

import dagger.Component;
import ru.org.adons.cblock.app.ApplicationComponent;
import ru.org.adons.cblock.ui.viewmodel.AddViewModel;
import ru.org.adons.cblock.ui.viewmodel.MainViewModel;

/**
 * Provide Application and data-model dependencies for View-Models
 */
@SuppressWarnings("WeakerAccess")
@ViewScope
@Component(dependencies = ApplicationComponent.class, modules = BaseAppModule.class)
public interface BaseAppComponent {

    void inject(MainViewModel mainViewModel);

    void inject(AddViewModel addViewModel);

}
