package ru.org.adons.cblock.data

import android.text.format.DateUtils
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import ru.org.adons.cblock.ext.log
import ru.org.adons.cblock.model.BlockListModel
import rx.Observable
import rx.observers.TestSubscriber
import java.util.*
import javax.inject.Inject

/**
 * Android test for [BlockListModel]
 */
class BlockListModelTest : BaseModelTest() {

    private val phoneNumber1 = "+380123456789"
    private val phoneNumber2 = "+380987654321"
    private val name1 = "Test Name"

    @Inject lateinit var blockListModel: BlockListModel

    @Before
    fun setUp() {
        // provide dependencies
        testComponent().inject(this)
        // clear all local data before test
        blockListModel.clearBlockList()
    }

    @Test
    fun addNumberToBlockList() {
        log("check number added to block list")
        addNumber(phoneNumber1)
        //
        log("check not duplicated")
        addNumber(phoneNumber1)
        //
        log("check add other number")
        addOtherNumber()
        //
        log("check not duplicated")
        addSameTwoNumbers()
    }

    private fun addNumber(phoneNumber: String) {
        val testSubscriber = TestSubscriber<BlockListItem>()
        blockListModel.addNumber(getLogItem(phoneNumber, name1))
        blockListModel.getBlockList()
                .flatMap { Observable.from(it) }
                .doOnNext {
                    assertNotNull(it)
                    assertEquals(it.phoneNumber, phoneNumber)
                    printItemDetails(it)
                }
                .subscribe(testSubscriber)
        testSubscriber.assertValueCount(1)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
        testSubscriber.assertUnsubscribed()
    }

    private fun addOtherNumber() {
        val testSubscriber = TestSubscriber<BlockListItem>()
        blockListModel.addNumber(getLogItem(phoneNumber2, null))
        blockListModel.getBlockList()
                .doOnNext {
                    val listItem = it[0]
                    val listItem1 = it[1]
                    assertFalse(listItem.phoneNumber == listItem1.phoneNumber)
                }
                .flatMap { Observable.from(it) }
                .doOnNext { this.printItemDetails(it) }
                .subscribe(testSubscriber)
        testSubscriber.assertValueCount(2)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
        testSubscriber.assertUnsubscribed()
    }

    private fun addSameTwoNumbers() {
        val testSubscriber = TestSubscriber<BlockListItem>()
        blockListModel.addNumber(getLogItem(phoneNumber1, name1))
        blockListModel.addNumber(getLogItem(phoneNumber2, null))
        blockListModel.getBlockList()
                .flatMap { Observable.from(it) }
                .doOnNext { this.printItemDetails(it) }
                .subscribe(testSubscriber)
        testSubscriber.assertValueCount(2)
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
        testSubscriber.assertUnsubscribed()
    }

    private fun getLogItem(phoneNumber: String, name: String?): CallLogItem = CallLogItem(
            1L, phoneNumber, Date().time, name, false, false)

    private fun printItemDetails(item: BlockListItem): BlockListItem = with(item) {
        log("id=" + id.toString())
        log("phone=" + phoneNumber)
        log("date=" + DateUtils.getRelativeTimeSpanString(date))
        log("name=" + name)
        item
    }

}
