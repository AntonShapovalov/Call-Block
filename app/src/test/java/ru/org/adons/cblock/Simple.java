package ru.org.adons.cblock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Simple test to try something
 */
public class Simple {

    @Test
    public void test() {
        String[] list = new String[]{"1"};
        TestSubscriber<List<String>> testSubscriber = new TestSubscriber<>();
        Observable.from(list)
                .map(s -> {
                    System.out.print(s);
                    return s;
                })
                .filter(s -> false)
                .toList()
                .flatMap(Observable::from)
                .toList()
                .map(l -> {
                    System.out.print(l.size());
                    return l;
                })
                .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
    }

}