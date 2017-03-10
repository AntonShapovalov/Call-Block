package ru.org.adons.cblock.ui.viewmodel;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.data.BlockListModel;
import ru.org.adons.cblock.data.CallLogModel;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.ui.view.AddFragment;
import ru.org.adons.cblock.utils.Logging;
import rx.Observable;

/**
 * Add Model View, provide data for {@link AddFragment}
 */

public class AddViewModel {

    @Inject CallLogModel callLogModel;
    @Inject BlockListModel blockListModel;

    public Observable<List<CallLogItem>> getCallLogList() {
        return callLogModel.getCallLogList()
                .doOnSubscribe(Logging.subscribe(this.getClass(), "getCallLogList"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "getCallLogList"));
    }

    public Observable<List<CallLogItem>> setBlockedItems(List<CallLogItem> items) {
        final HashSet<String> filter = new HashSet<>();
        return blockListModel.getBlockedPhones()
                .doOnSubscribe(Logging.subscribe(this.getClass(), "setBlockedItems"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "setBlockedItems"))
                .map(filter::addAll)
                .flatMap(phones -> Observable.from(items))
                .map(item -> {
                    if (filter.contains(item.phoneNumber())) {
                        item.isBlocked = true;
                    }
                    return item;
                })
                .toList();
    }

}
