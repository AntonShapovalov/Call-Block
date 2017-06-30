package ru.org.adons.cblock.ext

import rx.Subscription

/**
 * Extensions for [Subscription]
 */

fun Subscription.safeUnsubscribe() {
    if (!isUnsubscribed) unsubscribe()
}