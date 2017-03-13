package ru.org.adons.cblock.utils;

import rx.Subscription;

/**
 * Helper for {@link rx.Subscription}
 */
public class SubscriptionUtils {

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
