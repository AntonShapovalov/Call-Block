package ru.org.adons.cblock.data;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.model.BlockListItemDao;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.model.DaoSession;
import ru.org.adons.cblock.service.BlockService;
import ru.org.adons.cblock.ui.view.add.CallLogAdapter;
import ru.org.adons.cblock.scope.ViewScope;
import rx.Observable;

/**
 * Provide data for block list
 */

@ViewScope
public class BlockListModel {

    private final DaoSession daoSession;
    private final BlockListItemDao blockListDao;

    @Inject
    public BlockListModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        blockListDao = daoSession.getBlockListItemDao();
    }

    /**
     * Add phone number (incoming or missed) to block list
     * If number already exists in list  - it will not added
     *
     * @param logItem incoming or missed call
     */
    public void addNumber(CallLogItem logItem) {
        daoSession.runInTx(() -> {
            BlockListItem dbItem = blockListDao.queryBuilder()
                    .where(BlockListItemDao.Properties.PhoneNumber.eq(logItem.phoneNumber()))
                    .limit(1)
                    .unique();
            if (dbItem == null) {
                dbItem = new BlockListItem();
                dbItem.setPhoneNumber(logItem.phoneNumber());
                dbItem.setDate(logItem.date());
                dbItem.setName(logItem.name());
                blockListDao.insert(dbItem);
            }
        });
    }

    /**
     * Delete phone number from block list
     *
     * @param itemId db item id
     */
    public void deleteNumber(long itemId) {
        blockListDao.deleteByKeyInTx(itemId);
    }

    /**
     * @return all blocked numbers list
     */
    public Observable<List<BlockListItem>> getBlockList() {
        return Observable.fromCallable(() ->
                blockListDao.queryBuilder().orderDesc(BlockListItemDao.Properties.Date).list());
    }

    /**
     * @return set of blocking phones, used in {@link CallLogAdapter} and {@link BlockService}
     */
    public Observable<HashSet<String>> getBlockedPhones() {
        return getBlockList().flatMap(this::getBlockedPhones);
    }

    /**
     * @return set of blocking phones, used in {@link BlockService}
     */
    public Observable<HashSet<String>> getBlockedPhones(List<BlockListItem> items) {
        final HashSet<String> phones = new HashSet<>();
        return Observable.from(items)
                .map(item -> phones.add(item.getPhoneNumber()))
                .toList()
                .flatMap(list -> Observable.just(phones));
    }

    /**
     * remove all numbers from block list
     */
    void clearBlockList() {
        blockListDao.deleteAll();
    }

}
