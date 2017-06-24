package ru.org.adons.cblock.app;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.org.adons.cblock.data.BlockListItem;
import ru.org.adons.cblock.data.CallLogItem;
import ru.org.adons.cblock.model.BlockListModel;
import ru.org.adons.cblock.service.BlockService;
import ru.org.adons.cblock.ui.view.main.MainFragment;
import ru.org.adons.cblock.utils.Logging;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Bus event to add phones in block list and notify all subscribers: {@link MainFragment} and {@link BlockService}
 */

@Singleton
public class BlockManager {

    private final PublishSubject<List<BlockListItem>> blockListSubject = PublishSubject.create();

    @Inject
    public BlockManager() {
    }

    /**
     * Add phones in block list and notify all subscribers
     */
    public void addPhones(BlockListModel blockListModel, List<CallLogItem> items) {
        Observable.from(items)
                .doOnSubscribe(Logging.subscribe(this.getClass(), "ADD_PHONES:"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "ADD_PHONES"))
                .filter(CallLogItem::isSelected)
                .map(item -> {
                    blockListModel.addNumber(item);
                    Logging.d(this.getClass().getSimpleName() + ":" + item.getPhoneNumber());
                    return item;
                })
                .toList()
                .flatMap(logItems -> blockListModel.getBlockList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(blockListSubject::onNext);
    }

    /**
     * Delete phone from block list and notify all subscribers
     */
    public void deletePhone(BlockListModel blockListModel, long itemId) {
        Observable.just(itemId)
                .doOnSubscribe(Logging.subscribe(this.getClass(), "DELETE_PHONE"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "DELETE_PHONE"))
                .map(id -> {
                    blockListModel.deleteNumber(id);
                    return id;
                })
                .flatMap(id -> blockListModel.getBlockList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(blockListSubject::onNext);
    }

    /**
     * Subscribe to block list update
     *
     * @return list of blocked phones
     */
    public Observable<List<BlockListItem>> getBlockListUpdate() {
        return blockListSubject;
    }

}
