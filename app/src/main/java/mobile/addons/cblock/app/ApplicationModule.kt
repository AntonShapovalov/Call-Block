package mobile.addons.cblock.app

import android.content.Context
import com.squareup.sqlbrite.BriteContentResolver
import com.squareup.sqlbrite.SqlBrite
import dagger.Module
import dagger.Provides
import mobile.addons.cblock.data.DaoMaster
import mobile.addons.cblock.data.DaoSession
import rx.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Provide application scope dependencies (Context, DaoSession, Preferences)
 */
@Module
internal class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideContentResolver(sqlBrite: SqlBrite): BriteContentResolver = sqlBrite
            .wrapContentProvider(context.contentResolver, Schedulers.immediate())

    @Singleton
    @Provides
    fun provideSqlBrite(): SqlBrite = SqlBrite.Builder().build()

    @Singleton
    @Provides
    fun provideDaoSession(): DaoSession {
        val helper = DaoMaster.DevOpenHelper(context, "cblock-db")
        val db = helper.writableDb
        return DaoMaster(db).newSession()
    }

}
