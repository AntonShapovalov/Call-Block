package ru.org.adons.cblock.app;

import android.content.Context;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.org.adons.cblock.model.DaoMaster;
import ru.org.adons.cblock.model.DaoSession;
import rx.schedulers.Schedulers;

/**
 * Provide application scope dependencies (Context, DaoSession, Preferences)
 */
@Module
class ApplicationModule {

    private final Context context;

    ApplicationModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    Preferences providePreferences() {
        return new Preferences(context);
    }

    @Singleton
    @Provides
    BriteContentResolver provideContentResolver(SqlBrite sqlBrite) {
        return sqlBrite.wrapContentProvider(context.getContentResolver(), Schedulers.immediate());
    }

    @Singleton
    @Provides
    SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder().build();
    }

    @Singleton
    @Provides
    DaoSession provideDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "cblock-db");
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

}
