package ru.org.adons.cblock.utils;

import android.text.TextUtils;
import android.util.Log;

import ru.org.adons.cblock.BuildConfig;
import rx.functions.Action0;

/**
 * Wrapper for {@link Log}
 */
public class Logging implements Action0 {

    private enum Type {d, e}

    private final String tag;
    private final String message;

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
        log("LOG", message, Type.d);
    }

    public static void e(String message) {
        log("LOG", message, Type.e);
    }

    private static void log(String tag, String message, Type type) {
        if (BuildConfig.DEBUG) {
            if (!tag.startsWith("***")) {
                tag = "***" + tag;
            }
            switch (type) {
                case e:
                    Log.e(tag, message);
                    break;
                default:
                    Log.d(tag, message);
            }
        }
    }

    /**
     * Logging for {@link rx.Observable}
     *
     * @return Logging object and invoke call()
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
            log(tag, message, Type.d);
        } else {
            d(message);
        }
    }

}
