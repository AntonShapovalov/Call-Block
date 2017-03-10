package ru.org.adons.cblock;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
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
        HashSet<Integer> set = new HashSet<Integer>() {{
            add(1);
            add(2);
            add(4);
        }};
        Integer[] join = new Integer[]{1, 2};

        Observable<Integer> list = Observable.from(arr);
        final HashSet<Integer> filter = new HashSet<>();
        TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();

        Observable.just(set)
                .map(filter::addAll)
                .flatMap(s -> list)
                .filter(filter::contains)
                .toList().subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(Arrays.asList(join));
    }

}