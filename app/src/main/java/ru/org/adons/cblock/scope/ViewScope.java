package ru.org.adons.cblock.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import ru.org.adons.cblock.app.ApplicationComponent;

/**
 * Scope for all sub-component of {@link ApplicationComponent}
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewScope {
}
