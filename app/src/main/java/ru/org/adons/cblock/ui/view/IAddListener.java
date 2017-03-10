package ru.org.adons.cblock.ui.view;

import ru.org.adons.cblock.ui.base.IBaseFragmentListener;

/**
 * Listen {@link AddFragment} and notify {@link MainActivity}
 */
interface IAddListener extends IBaseFragmentListener {

    void onBackPressed();

}
