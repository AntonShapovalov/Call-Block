package ru.org.adons.cblock.app;

import android.app.Application;

/**
 * Provide application scope dependencies (Context, DaoSession, Preferences)
 */

public class CBlockApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }

    public ApplicationComponent applicationComponent() {
        return applicationComponent;
    }

}
