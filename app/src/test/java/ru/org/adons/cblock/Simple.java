package ru.org.adons.cblock;

import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Simple test to try something
 */
public class Simple {

    @Test
    public void test() {
        Integer[] arr = new Integer[]{1, 2, 3};
        TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();
        Observable.from(arr)
                .doOnNext(System.out::print)
                .toList()
                .doOnNext(list -> System.out.print(list.size()))
                .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

}