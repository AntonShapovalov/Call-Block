package ru.org.adons.cblock.ui.view.add

import ru.org.adons.cblock.app.BlockManager
import ru.org.adons.cblock.data.CallLogItem
import ru.org.adons.cblock.ext.logSubscribe
import ru.org.adons.cblock.ext.logUnsubscribe
import ru.org.adons.cblock.model.BlockListModel
import ru.org.adons.cblock.model.CallLogModel
import rx.Observable
import javax.inject.Inject

/**
 * Add Model View, provide data for [AddFragment]
 */

class AddViewModel @Inject internal constructor() {

    @Inject lateinit var callLogModel: CallLogModel
    @Inject lateinit var blockListModel: BlockListModel
    @Inject lateinit var blockManager: BlockManager

    /**
     * Get list of incoming or missed calls and set isBlocked flag
     */
    fun getCallLogList(): Observable<List<CallLogItem>> {
        return callLogModel.getCallLogList()
                .doOnSubscribe { logSubscribe("getCallLogList") }
                .doOnUnsubscribe { logUnsubscribe("getCallLogList") }
                .doOnNext { setBlockedNumbers(it) }
    }

    fun addPhones(items: List<CallLogItem>) {
        blockManager.addPhones(blockListModel, items)
    }

    // set isBlocked flag if phone already blocked, checkbox will disabled in UI
    private fun setBlockedNumbers(items: List<CallLogItem>): List<CallLogItem> {
        val blockList = with(blockListModel) { getBlockedPhones(getNumbersList()) }
        items.forEach { if (blockList.contains(it.phoneNumber)) it.isBlocked = true }
        return items
    }

}
