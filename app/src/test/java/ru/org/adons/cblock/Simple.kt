package ru.org.adons.cblock

import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber

/**
 * Simple test to try something
 */
class Simple {

    @Test
    fun test() {
        val testSubscriber = TestSubscriber<String>()
        Observable.just(true).map {
            if (it) {
                System.out.print(it.toString())
                "true"
            } else {
                System.out.print(it.toString())
                "false"
            }
        }.subscribe(testSubscriber)

        testSubscriber.assertValue("true")
    }

}