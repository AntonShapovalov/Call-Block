package ru.org.adons.cblock.ui.viewmodel;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.Preferences;
import ru.org.adons.cblock.data.BlockListModel;
import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.service.BlockService;
import ru.org.adons.cblock.ui.view.MainFragment;
import ru.org.adons.cblock.utils.Logging;
import rx.Observable;

/**
 * Main Model View, provide data for {@link MainFragment}
 */
public class MainViewModel {

    @Inject Context context;
    @Inject Preferences pref;
    @Inject BlockListModel blockListModel;

    public Observable<List<BlockListItem>> getBlockList() {
        return blockListModel.getBlockList()
                .doOnSubscribe(Logging.subscribe(this.getClass(), "getBlockList"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "getBlockList"));
    }

    public Observable<Boolean> getSwitchState() {
        return Observable.just(Preferences.SERVICE_SWITCH_KEY).map(pref::getBoolean);
    }

    public Observable<String> changeServiceState(boolean isEnable) {
        return Observable.just(isEnable).map(bool -> {
            pref.putBoolean(Preferences.SERVICE_SWITCH_KEY, isEnable);
            if (bool){
                BlockService.enable(context);
                return context.getString(R.string.main_notification_text_enable);
            } else {
                BlockService.disable(context);
                return context.getString(R.string.main_toast_text_disable);
            }
        });
    }

}
