package ru.org.adons.cblock.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

import ru.org.adons.cblock.BuildConfig;

/**
 * Provide application scope dependencies (Context, DaoSession, Preferences)
 */
public class CBlockApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public ApplicationComponent applicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(getApplicationContext()))
                    .build();
        }
        return applicationComponent;
    }

}
