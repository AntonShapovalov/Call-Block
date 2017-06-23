package ru.org.adons.cblock.app;

import android.content.Context;

import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Component;
import ru.org.adons.cblock.data.DaoSession;

/**
 * Provide application scope dependencies (Context, Preferences, DaoSession)
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Context context();

    Preferences preferences();

    BriteContentResolver resolver();

    DaoSession daoSession();

    BlockManager blockManager();

}
