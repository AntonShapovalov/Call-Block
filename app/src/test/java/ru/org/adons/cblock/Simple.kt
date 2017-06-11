package ru.org.adons.cblock

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Simple test to try something
 */
class Simple {

    @Test
    fun test() {
        val list = listOf("1", "2", "2", "3")
        val set = list.toHashSet()
        assertEquals(4, list.size)
        assertEquals(3, set.size)
    }

}