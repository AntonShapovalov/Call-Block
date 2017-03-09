package ru.org.adons.cblock.ui.main;

import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.app.Preferences;
import ru.org.adons.cblock.data.BlockListModel;
import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.utils.Logging;
import rx.Observable;

/**
 * Main Model View, provide data for {@link MainFragment}
 */
public class MainViewModel {

    @Inject Preferences pref;
    @Inject BlockListModel blockListModel;

    Observable<List<BlockListItem>> getBlockList() {
        return blockListModel.getBlockList()
                .doOnSubscribe(Logging.subscribe(this.getClass(), "getBlockList"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "getBlockList"));
    }

//    private boolean isServiceEnabled;

//    isServiceEnabled = pref.getBoolean(getString(R.string.main_pref_key_switch));

//    public void startService(MenuItem item) {
//        if (isServiceEnabled) {
//            isServiceEnabled = false;
//            BlockService.enable(this);
//        } else {
//            isServiceEnabled = true;
//            BlockService.disable(this);
//        }
//        pref.putBoolean(getString(R.string.main_pref_key_switch), isServiceEnabled);
//    }

}
