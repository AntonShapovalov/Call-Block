package ru.org.adons.cblock.datamodel;

import java.util.List;

import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.model.BlockListItemDao;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.model.DaoSession;
import rx.Observable;

/**
 * Provide data for list of blocking phones
 */

public class BlockListModel {

    private final BlockListItemDao blockListDao;

    public BlockListModel(DaoSession daoSession) {
        this.blockListDao = daoSession.getBlockListItemDao();
    }

    /**
     * Add phone number (incoming or missed) to block list
     * If number already exists in list  - it will not added
     *
     * @param logItem incoming or missed call
     * @return added block list item
     */
    Observable<BlockListItem> addNumber(CallLogItem logItem) {
        return Observable.fromCallable(() -> {
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
            return dbItem;
        });
    }

    /**
     * @return all blocked numbers
     */
    Observable<List<BlockListItem>> getBlockList() {
        return Observable.fromCallable(blockListDao::loadAll);
    }

    /**
     * remove all numbers from block list
     */
    void clearBlockList() {
        blockListDao.deleteAll();
    }

}
