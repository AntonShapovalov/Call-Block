package ru.org.adons.cblock.data;

import android.text.format.DateUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import javax.inject.Inject;

import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.utils.Logging;
import rx.Observable;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Android test for {@link BlockListModel}
 */

public class BlockListModelTest extends BaseModelTest {

    @Inject BlockListModel blockListModel;
    private final String phoneNumber1 = "123456789";
    private final String phoneNumber2 = "987654321";

    @Before
    public void setUp() {
        // provide dependencies
        testComponent().inject(this);
        // clear all local data before test
        blockListModel.clearBlockList();
    }

    @Test
    public void addNumberToBlockList() {
        Logging.d("check number added to block list");
        addNumber(phoneNumber1);
        //
        Logging.d("check not duplicated");
        addNumber(phoneNumber1);
        //
        Logging.d("check add other number");
        addOtherNumber();
        //
        Logging.d("check not duplicated");
        addSameTwoNumbers();
    }

    private void addNumber(String phoneNumber) {
        TestSubscriber<BlockListItem> testSubscriber = new TestSubscriber<>();
        blockListModel.addNumber(getLogItem(phoneNumber));
        blockListModel.getBlockList()
                .flatMap(Observable::from)
                .map(listItem -> {
                    assertNotNull(listItem);
                    assertEquals(listItem.getPhoneNumber(), phoneNumber);
                    return printItemDetails(listItem);
                })
                .subscribe(testSubscriber);
        testSubscriber.assertValueCount(1);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertUnsubscribed();
    }

    private void addOtherNumber() {
        TestSubscriber<BlockListItem> testSubscriber = new TestSubscriber<>();
        blockListModel.addNumber(getLogItem(phoneNumber2));
        blockListModel.getBlockList()
                .map(blockList -> {
                    BlockListItem listItem = blockList.get(0);
                    BlockListItem listItem1 = blockList.get(1);
                    assertFalse(listItem.getPhoneNumber().equals(listItem1.getPhoneNumber()));
                    return blockList;
                })
                .flatMap(Observable::from)
                .map(this::printItemDetails)
                .subscribe(testSubscriber);
        testSubscriber.assertValueCount(2);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertUnsubscribed();
    }

    private void addSameTwoNumbers() {
        TestSubscriber<BlockListItem> testSubscriber = new TestSubscriber<>();
        blockListModel.addNumber(getLogItem(phoneNumber1));
        blockListModel.addNumber(getLogItem(phoneNumber2));
        blockListModel.getBlockList()
                .flatMap(Observable::from)
                .map(this::printItemDetails)
                .subscribe(testSubscriber);
        testSubscriber.assertValueCount(2);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertUnsubscribed();
    }

    private CallLogItem getLogItem(String phoneNumber) {
        return CallLogItem.builder()
                .setId(1L)
                .setPhoneNumber(phoneNumber)
                .setDate(new Date().getTime())
                .build();
    }

    private BlockListItem printItemDetails(BlockListItem item) {
        Logging.d("id=" + item.getId().toString());
        Logging.d("phone=" + item.getPhoneNumber());
        Logging.d("date=" + DateUtils.getRelativeTimeSpanString(item.getDate()).toString());
        Logging.d("name=" + item.getName());
        return item;
    }

}
