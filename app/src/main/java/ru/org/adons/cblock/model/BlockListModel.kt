package ru.org.adons.cblock.model

import ru.org.adons.cblock.data.BlockListItem
import ru.org.adons.cblock.data.BlockListItemDao
import ru.org.adons.cblock.data.CallLogItem
import ru.org.adons.cblock.data.DaoSession
import ru.org.adons.cblock.scope.ViewScope
import ru.org.adons.cblock.service.BlockService
import ru.org.adons.cblock.ui.view.add.CallLogAdapter
import rx.Observable
import javax.inject.Inject

/**
 * Provide data for block list
 */

@ViewScope
class BlockListModel @Inject constructor(private val daoSession: DaoSession) {

    private val blockListDao: BlockListItemDao = daoSession.blockListItemDao

    /**
     * Add phone number (incoming or missed) to block list
     * If number already exists in list  - it will not added

     * @param logItem incoming or missed call
     */
    fun addNumber(logItem: CallLogItem) {
        daoSession.runInTx {
            var dbItem = blockListDao.queryBuilder()
                    .where(BlockListItemDao.Properties.PhoneNumber.eq(logItem.phoneNumber))
                    .limit(1)
                    .unique()
            if (dbItem == null) {
                dbItem = BlockListItem()
                dbItem.phoneNumber = logItem.phoneNumber
                dbItem.date = logItem.date
                dbItem.name = logItem.name
                blockListDao.insert(dbItem)
            }
        }
    }

    /**
     * Delete phone number from block list

     * @param itemId db item id
     */
    fun deleteNumber(itemId: Long) {
        blockListDao.deleteByKeyInTx(itemId)
    }

    /**
     * @return all blocked numbers list
     */
    fun getBlockList(): Observable<List<BlockListItem>> {
        return Observable.fromCallable { blockListDao.queryBuilder().orderDesc(BlockListItemDao.Properties.Date).list() }
    }

    /**
     * @return set of blocking phones, used in [CallLogAdapter] and [BlockService]
     */
    fun getBlockedPhones(): Observable<HashSet<String>> {
        return getBlockList().map { getBlockedPhones(it) }
    }

    /**
     * @return set of blocking phones, used in [BlockService]
     */
    fun getBlockedPhones(items: List<BlockListItem>): HashSet<String> {
        return items.map { it.phoneNumber }.toHashSet()
    }

    /**
     * remove all numbers from block list
     */
    fun clearBlockList() {
        blockListDao.deleteAll()
    }

}
