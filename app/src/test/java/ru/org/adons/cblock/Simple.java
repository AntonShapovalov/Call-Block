package ru.org.adons.cblock;

import org.junit.Test;

import rx.Observable;

/**
 * Simple test to try something
 */
public class Simple {

    @Test
    public void test() {
        Observable.just(true).filter(bool -> true).subscribe(System.out::print);
    }

}