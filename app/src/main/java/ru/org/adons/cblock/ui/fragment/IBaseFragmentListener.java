package ru.org.adons.cblock.ui.fragment;

import ru.org.adons.cblock.ui.activity.MainComponent;

/**
 * Base interface for Fragment -> Activity interaction
 * {@link BaseFragment#setListener} method used to set Activity as listener on Fragment events
 */
public interface IBaseFragmentListener {

    MainComponent mainComponent();

    void showProgress();

    void hideProgress();

    void showError(String message);

}
