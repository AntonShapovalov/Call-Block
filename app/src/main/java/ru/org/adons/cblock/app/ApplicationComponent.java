package ru.org.adons.cblock.app;

import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Component;
import ru.org.adons.cblock.model.DaoSession;

/**
 * Provide application scope dependencies (Context, Preferences, DaoSession)
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Preferences preferences();

    BriteContentResolver resolver();

    DaoSession daoSession();

}
