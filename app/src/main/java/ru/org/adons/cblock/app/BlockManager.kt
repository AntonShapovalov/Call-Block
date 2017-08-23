package ru.org.adons.cblock.app

import ru.org.adons.cblock.data.BlockListItem
import ru.org.adons.cblock.data.CallLogItem
import ru.org.adons.cblock.ext.logSubscribe
import ru.org.adons.cblock.ext.logUnsubscribe
import ru.org.adons.cblock.model.BlockListModel
import ru.org.adons.cblock.service.BlockService
import ru.org.adons.cblock.ui.view.main.MainFragment
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bus event to add phones in block list and notify all subscribers: [MainFragment] and [BlockService]
 */

@Singleton
class BlockManager @Inject constructor() {

    private val blockListSubject = PublishSubject.create<List<BlockListItem>>()

    /**
     * Add phones in block list and notify all subscribers
     */
    fun addPhones(blockListModel: BlockListModel, items: List<CallLogItem>) {
        Observable.from(items)
                .doOnSubscribe { logSubscribe("ADD_PHONES") }
                .doOnUnsubscribe { logUnsubscribe("ADD_PHONES") }
                .filter { it.isSelected }
                .doOnNext { blockListModel.addNumber(it) }
                .toList()
                .flatMap { blockListModel.getBlockList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { blockListSubject.onNext(it) }
    }

    /**
     * Delete phone from block list and notify all subscribers
     */
    fun deletePhone(blockListModel: BlockListModel, itemId: Long) {
        Observable.just(itemId)
                .doOnSubscribe { logSubscribe("DELETE_PHONE") }
                .doOnUnsubscribe { logUnsubscribe("DELETE_PHONE") }
                .doOnNext { blockListModel.deleteNumber(it) }
                .flatMap { blockListModel.getBlockList() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { blockListSubject.onNext(it) }
    }

    /**
     * Subscribe to block list update

     * @return list of blocked phones
     */
    fun getBlockListUpdate(): Observable<List<BlockListItem>> = blockListSubject

}
