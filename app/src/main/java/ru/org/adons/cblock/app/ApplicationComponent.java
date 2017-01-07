package ru.org.adons.cblock.app;

import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Component;
import ru.org.adons.cblock.ui.main.MainActivity;

/**
 * Provide application scope dependencies (Context, Preferences, DaoSession)
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity activity); // TODO: move this into MainComponent

    BriteContentResolver resolver();

}
