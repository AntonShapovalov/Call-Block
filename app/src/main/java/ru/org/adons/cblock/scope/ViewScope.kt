package ru.org.adons.cblock.scope

import ru.org.adons.cblock.app.ApplicationComponent
import javax.inject.Scope

/**
 * Scope for all sub-component of [ApplicationComponent]
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ViewScope
