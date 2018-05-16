package mobile.addons.cblock.ui.view.main

import android.content.Context
import mobile.addons.cblock.R
import mobile.addons.cblock.app.BlockManager
import mobile.addons.cblock.app.Preferences
import mobile.addons.cblock.data.BlockListItem
import mobile.addons.cblock.ext.logSubscribe
import mobile.addons.cblock.ext.logUnsubscribe
import mobile.addons.cblock.model.BlockListModel
import mobile.addons.cblock.service.BlockService
import rx.Observable
import javax.inject.Inject

/**
 * Main Model View, provide data for [MainFragment]
 */

class MainViewModel @Inject constructor() {

    @Inject lateinit var context: Context
    @Inject lateinit var pref: Preferences
    @Inject lateinit var blockListModel: BlockListModel
    @Inject lateinit var blockManager: BlockManager

    fun getBlockList(): Observable<List<BlockListItem>> = blockListModel.getBlockList()
            .doOnSubscribe { logSubscribe("getBlockList") }
            .doOnUnsubscribe { logUnsubscribe("getBlockList") }

    fun getServiceState(): Observable<Boolean> = Observable.fromCallable { pref.serviceState }

    fun changeServiceState(isEnable: Boolean): Observable<String> = Observable
            .just(isEnable)
            .map {
                pref.serviceState = it
                if (it) {
                    BlockService.start(context)
                    context.getString(R.string.main_notification_text_enable)
                } else {
                    BlockService.stop(context)
                    context.getString(R.string.main_toast_text_disable)
                }
            }

    fun deletePhone(itemId: Long) = blockManager.deletePhone(blockListModel, itemId)

}
