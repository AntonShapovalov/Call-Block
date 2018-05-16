package mobile.addons.cblock.app

import android.app.Application

import com.facebook.stetho.Stetho

import mobile.addons.cblock.BuildConfig

/**
 * Provide application scope dependencies (Context, DaoSession, Preferences)
 */
class CBlockApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy { buildApplicationComponent() }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun buildApplicationComponent(): ApplicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(applicationContext))
            .build()

}
