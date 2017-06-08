package ru.org.adons.cblock.ui.activity;

import dagger.Component;
import ru.org.adons.cblock.app.ApplicationComponent;
import ru.org.adons.cblock.scope.ViewScope;
import ru.org.adons.cblock.ui.view.add.AddFragment;
import ru.org.adons.cblock.ui.view.main.MainFragment;

/**
 * Provide Application and data-model dependencies for View-Models
 */
@SuppressWarnings("WeakerAccess")
@ViewScope
@Component(dependencies = ApplicationComponent.class)
public interface MainComponent {

    void inject(MainFragment mainFragment);

    void inject(AddFragment addFragment);

}
