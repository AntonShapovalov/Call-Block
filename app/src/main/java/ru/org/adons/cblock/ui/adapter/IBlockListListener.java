package ru.org.adons.cblock.ui.adapter;

import ru.org.adons.cblock.ui.view.MainFragment;

/**
 * Listen event from {@link BlockListAdapter} and notify {@link MainFragment}
 */
public interface IBlockListListener {

    void deletePhone(long itemId);

}
