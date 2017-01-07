package ru.org.adons.cblock.datamodel;

import android.app.UiAutomation;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.app.DaggerTestComponent;
import ru.org.adons.cblock.app.TestComponent;
import ru.org.adons.cblock.app.TestModule;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.utils.Logging;
import ru.org.adons.cblock.utils.SubscriptionUtils;
import rx.Subscription;
import rx.observers.TestSubscriber;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Android test for {@link CallLogModel}
 */

@RunWith(AndroidJUnit4.class)
public class CallLogModelTest {

    @Inject CallLogModel callLogModel;
    private final List<CallLogItem> callLogItems = new ArrayList<>();

    @Before
    public void setUp() {
        // grant permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            UiAutomation ui = getInstrumentation().getUiAutomation();
            String grant = "pm grant " + getTargetContext().getPackageName();
            ui.executeShellCommand(grant + " android.permission.READ_CALL_LOG");
            ui.executeShellCommand(grant + " android.permission.WRITE_CALL_LOG");
        }

        // provide dependencies
        CBlockApplication application = (CBlockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        TestComponent component = DaggerTestComponent.builder()
                .applicationComponent(application.applicationComponent())
                .testModule(new TestModule())
                .build();
        component.inject(this);
    }

    /**
     * Subscribe to update from call(incoming and missed) log list
     * To make incoming call on emulator, click (...) "more" in bottom right corner and select "Phone"
     */
    @Test
    public void subscribeToCallLog() {
        TestSubscriber<List<CallLogItem>> testSubscriber = new TestSubscriber<>();

        // check subscription without errors and not completed (wait for update from call log)
        Subscription subscription = callLogModel.getCallLogList()
                .doOnNext(this::setCallLogItems)
                .doOnSubscribe(Logging.actionSubscribe(this.getClass(), "getCallLogList"))
                .doOnUnsubscribe(Logging.actionUnsubscribe(this.getClass(), "getCallLogList"))
                .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNotCompleted();
        assertTrue(SubscriptionUtils.isSubscribed(subscription));

        // check unsubscribe (no update from call log will received)
        SubscriptionUtils.unsubscribe(subscription);
        testSubscriber.assertUnsubscribed();
        assertFalse(SubscriptionUtils.isSubscribed(subscription));
    }

    private void setCallLogItems(List<CallLogItem> items) {
        callLogItems.clear();
        callLogItems.addAll(items);
        // print details
        for (CallLogItem item : callLogItems) {
            Logging.d("id=" + item.id());
            Logging.d("phoneNumber = " + item.phoneNumber());
            Logging.d("date = " + item.date());
            Logging.d("name = " + item.name());
        }
    }

}
