package ru.org.adons.cblock.ui.main;

import ru.org.adons.cblock.ui.dialog.IBaseDialogListener;

/**
 * Listener interface for {@link DeleteDialogFragment} -> {@link MainActivity} interaction
 */

interface IDeleteDialogListener extends IBaseDialogListener {

    void removeNumberFromList(long itemId);

}
