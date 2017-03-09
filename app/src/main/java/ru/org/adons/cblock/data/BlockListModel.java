package ru.org.adons.cblock.data;

import java.util.List;

import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.model.BlockListItemDao;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.model.DaoSession;
import rx.Observable;

/**
 * Provide data for block list
 */
public class BlockListModel {

    private final DaoSession daoSession;
    private final BlockListItemDao blockListDao;

    public BlockListModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.blockListDao = daoSession.getBlockListItemDao();
    }

    /**
     * Add phone number (incoming or missed) to block list
     * If number already exists in list  - it will not added
     *
     * @param logItem incoming or missed call
     */
    void addNumber(CallLogItem logItem) {
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
     * @return all blocked numbers
     */
    public Observable<List<BlockListItem>> getBlockList() {
        return Observable.fromCallable(blockListDao::loadAll);
    }

    /**
     * remove all numbers from block list
     */
    void clearBlockList() {
        blockListDao.deleteAll();
    }

}
