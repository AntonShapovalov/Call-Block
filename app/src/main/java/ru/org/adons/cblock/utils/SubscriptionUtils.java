package ru.org.adons.cblock.utils;

import rx.Subscription;

/**
 * Helper for {@link rx.Subscription}
 */

public class SubscriptionUtils {

    /**
     * Unsubscribe and set subscription to null
     *
     * @param subscription subscription
     * @return null
     */
    public static Subscription release(Subscription subscription) {
        unsubscribe(subscription);
        return null;
    }

    /**
     * Safe unsubscribe
     *
     * @param subscription subscription
     */
    public static void unsubscribe(Subscription subscription) {
        if (isSubscribed(subscription)) {
            subscription.unsubscribe();
        }
    }

    /**
     * Check if subscription active
     *
     * @param subscription subscription
     * @return true, if active
     */
    public static boolean isSubscribed(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }

}
