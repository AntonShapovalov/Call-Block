package mobile.addons.cblock.ui.view.add

import mobile.addons.cblock.app.BlockManager
import mobile.addons.cblock.data.CallLogItem
import mobile.addons.cblock.ext.logSubscribe
import mobile.addons.cblock.ext.logUnsubscribe
import mobile.addons.cblock.model.BlockListModel
import mobile.addons.cblock.model.CallLogModel
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
    fun getCallLogList(): Observable<List<CallLogItem>> = callLogModel
            .getCallLogList()
            .doOnSubscribe { logSubscribe("getCallLogList") }
            .doOnUnsubscribe { logUnsubscribe("getCallLogList") }
            .doOnNext { setBlockedNumbers(it) }

    fun addPhones(items: List<CallLogItem>) = blockManager
            .addPhones(blockListModel, items)

    // set isBlocked flag if phone already blocked, checkbox will disabled in UI
    private fun setBlockedNumbers(items: List<CallLogItem>): List<CallLogItem> {
        val blockList = with(blockListModel) { getBlockedPhones(getNumbersList()) }
        items.forEach { if (blockList.contains(it.phoneNumber)) it.isBlocked = true }
        return items
    }

}
