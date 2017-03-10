package ru.org.adons.cblock.ui.view;

import ru.org.adons.cblock.ui.base.IBaseFragmentListener;

/**
 * Listen {@link MainFragment} and notify {@link MainActivity}
 */
interface IMainListener extends IBaseFragmentListener {

    void showAddFragment();

}
