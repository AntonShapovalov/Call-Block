package ru.org.adons.cblock.data;

import android.app.UiAutomation;
import android.os.Build;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.utils.Logging;
import ru.org.adons.cblock.utils.SubscriptionUtils;
import rx.Observable;
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
public class CallLogModelTest extends BaseModelTest {

    @Inject CallLogModel callLogModel;

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
        testComponent().inject(this);
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
                .doOnSubscribe(Logging.subscribe(this.getClass(), "getCallLogList"))
                .doOnUnsubscribe(Logging.unsubscribe(this.getClass(), "getCallLogList"))
                .flatMap(Observable::from)
                .map(this::printItemDetails)
                .toList()
                .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertNotCompleted();
        assertTrue(SubscriptionUtils.isSubscribed(subscription));

        // check unsubscribe (no update from call log will received)
        SubscriptionUtils.unsubscribe(subscription);
        testSubscriber.assertUnsubscribed();
        assertFalse(SubscriptionUtils.isSubscribed(subscription));
    }

    private CallLogItem printItemDetails(CallLogItem item) {
        Logging.d("id=" + item.id());
        Logging.d("phoneNumber = " + item.phoneNumber());
        Logging.d("date = " + item.date());
        Logging.d("name = " + item.name());
        return item;
    }

}
