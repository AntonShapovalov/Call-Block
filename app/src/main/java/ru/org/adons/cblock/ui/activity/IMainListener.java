package ru.org.adons.cblock.ui.activity;

import ru.org.adons.cblock.ui.fragment.IBaseFragmentListener;
import ru.org.adons.cblock.ui.view.add.AddFragment;
import ru.org.adons.cblock.ui.view.main.MainFragment;

/**
 * Listen {@link MainFragment}, {@link AddFragment} and notify {@link MainActivity}
 */
public interface IMainListener extends IBaseFragmentListener {

    void showAddFragment();

    void onBackPressed();

}
