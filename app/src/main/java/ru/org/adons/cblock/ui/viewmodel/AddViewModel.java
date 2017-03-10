package ru.org.adons.cblock.ui.viewmodel;

import java.util.List;

import javax.inject.Inject;

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

    public Observable<List<CallLogItem>> getCallLogList() {
        return callLogModel.getCallLogList()
                .doOnSubscribe(Logging.subscribe(this.getClass(), "getCallLogList"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "getCallLogList"));
    }

}
