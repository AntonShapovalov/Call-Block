package ru.org.adons.cblock.utils;

import android.text.TextUtils;
import android.util.Log;

import ru.org.adons.cblock.BuildConfig;
import rx.functions.Action0;

/**
 * Wrapper for {@link Log#d(String, String)}
 */

public class Logging implements Action0 {

    private String tag;
    private String message;

    private Logging(String message) {
        this.message = message;
    }

    private Logging(String tag, String message) {
        this.tag = tag;
        this.message = message;
    }

    /**
     * Base static method for logging
     *
     * @param message log message
     */
    public static void d(String message) {
        d("LOG", message);
    }

    public static void d(String tag, Class tClass, String message) {
        d(tag, tClass.getSimpleName() + ":" + message);
    }

    private static void d(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (!tag.startsWith("***")) {
                tag = "***" + tag;
            }
            Log.d(tag, message);
        }
    }

    /**
     * Logging for rx {@link Action0}
     */
    public static Logging subscribe(Class parent, String subscription) {
        return new Logging(parent.getSimpleName(), subscription + ":subscribe");
    }

    public static Logging unsubscribe(Class parent, String subscription) {
        return new Logging(parent.getSimpleName(), subscription + ":un-subscribe");
    }

    @Override
    public void call() {
        if (!TextUtils.isEmpty(tag)) {
            d(tag, message);
        } else {
            d(message);
        }
    }

}
