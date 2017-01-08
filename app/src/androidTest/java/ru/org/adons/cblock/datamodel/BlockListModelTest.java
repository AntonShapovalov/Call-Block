package ru.org.adons.cblock.datamodel;

import android.text.format.DateUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.model.BlockListItem;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.utils.Logging;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Android test for {@link BlockListModel}
 */

public class BlockListModelTest extends BaseModelTest {

    @Inject BlockListModel blockListModel;
    private final List<BlockListItem> blockList = new ArrayList<>();

    @Before
    public void setUp() {
        // provide dependencies
        testComponent().inject(this);
        // clear all local data before test
        blockListModel.clearBlockList();
    }

    @Test
    public void addNumberToBlockList() {
        String phoneNumber1 = "123456789";
        String phoneNumber2 = "987654321";

        // check number added to block list
        addNumber(phoneNumber1);
        assertEquals(blockList.size(), 1);
        BlockListItem listItem = blockList.get(0);
        assertNotNull(listItem);
        assertEquals(listItem.getPhoneNumber(), phoneNumber1);

        // check not duplicated
        addNumber(phoneNumber1);
        assertEquals(blockList.size(), 1);
        listItem = blockList.get(0);
        assertNotNull(listItem);
        assertEquals(listItem.getPhoneNumber(), phoneNumber1);

        // check add other number
        addNumber(phoneNumber2);
        assertEquals(blockList.size(), 2);
        listItem = blockList.get(0);
        BlockListItem listItem1 = blockList.get(1);
        assertFalse(listItem.getPhoneNumber().equals(listItem1.getPhoneNumber()));

        // check not duplicated
        addNumber(phoneNumber1);
        assertEquals(blockList.size(), 2);
        addNumber(phoneNumber2);
        assertEquals(blockList.size(), 2);
    }

    private void addNumber(String phoneNumber) {
        TestSubscriber<BlockListItem> itemSubscriber = new TestSubscriber<>();
        TestSubscriber<List<BlockListItem>> listSubscriber = new TestSubscriber<>();
        CallLogItem logItem = getLogItem(phoneNumber);
        blockListModel.addNumber(logItem)
                .doOnSubscribe(Logging.actionSubscribe(this.getClass(), "addNumber"))
                .doOnUnsubscribe(Logging.actionUnsubscribe(this.getClass(), "addNumber"))
                .subscribe(itemSubscriber);
        itemSubscriber.assertNoErrors();
        itemSubscriber.assertCompleted();
        itemSubscriber.assertUnsubscribed();

        blockListModel.getBlockList()
                .doOnSubscribe(Logging.actionSubscribe(this.getClass(), "getBlockList"))
                .doOnUnsubscribe(Logging.actionUnsubscribe(this.getClass(), "getBlockList"))
                .doOnNext(this::setBlockList)
                .subscribe(listSubscriber);
        listSubscriber.assertNoErrors();
        listSubscriber.assertCompleted();
        listSubscriber.assertUnsubscribed();
    }

    private CallLogItem getLogItem(String phoneNumber) {
        return CallLogItem.builder()
                .setId(1L)
                .setPhoneNumber(phoneNumber)
                .setDate(new Date().getTime())
                .build();
    }

    private void setBlockList(List<BlockListItem> items) {
        blockList.clear();
        blockList.addAll(items);
        // print details
        for (BlockListItem item : items) {
            Logging.d("id=" + item.getId().toString());
            Logging.d("phone=" + item.getPhoneNumber());
            Logging.d("date=" + DateUtils.getRelativeTimeSpanString(item.getDate()).toString());
            Logging.d("name=" + item.getName());
        }
    }

}
