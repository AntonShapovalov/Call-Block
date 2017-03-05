package ru.org.adons.cblock.app;

import com.squareup.sqlbrite.BriteContentResolver;

import dagger.Module;
import dagger.Provides;
import ru.org.adons.cblock.data.BlockListModel;
import ru.org.adons.cblock.data.CallLogModel;
import ru.org.adons.cblock.model.DaoSession;
import ru.org.adons.cblock.ui.base.ViewScope;

/**
 * Provide Application and data-model dependencies for test
 */

@Module
public class TestModule {

    @ViewScope
    @Provides
    CallLogModel provideCallLogModel(BriteContentResolver resolver) {
        return new CallLogModel(resolver);
    }

    @ViewScope
    @Provides
    BlockListModel provideBlockListModel(DaoSession daoSession) {
        return new BlockListModel(daoSession);
    }

}
