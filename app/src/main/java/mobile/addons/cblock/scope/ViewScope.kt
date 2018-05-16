package mobile.addons.cblock.scope

import mobile.addons.cblock.app.ApplicationComponent
import javax.inject.Scope

/**
 * Scope for all sub-component of [ApplicationComponent]
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ViewScope
