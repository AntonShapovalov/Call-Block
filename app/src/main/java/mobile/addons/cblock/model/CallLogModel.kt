package mobile.addons.cblock.model

import android.provider.CallLog
import com.squareup.sqlbrite.BriteContentResolver
import mobile.addons.cblock.data.CallLogItem
import mobile.addons.cblock.scope.ViewScope
import rx.Observable
import javax.inject.Inject

/**
 * Provide data from [CallLog] for incoming or missed calls list
 */

@ViewScope
class CallLogModel @Inject constructor() {

    @Inject lateinit var resolver: BriteContentResolver

    /**
     * @return list of incoming or missed calls
     */
    fun getCallLogList(): Observable<List<CallLogItem>> = resolver
            .createQuery(CALLS_URI, CALLS_SUMMARY_PROJECTION, CALL_SELECT, null, CallLog.Calls.DEFAULT_SORT_ORDER, false)
            .mapToList {
                CallLogItem(id = it.getLong(COLUMN_ID_INDEX),
                        phoneNumber = it.getString(COLUMN_NUMBER_INDEX),
                        date = it.getLong(COLUMN_DATE_INDEX),
                        name = it.getString(COLUMN_NAME_INDEX))
            }

    companion object {
        private val CALLS_URI = CallLog.Calls.CONTENT_URI
        private val CALLS_SUMMARY_PROJECTION = arrayOf(CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.CACHED_NAME)
        private const val COLUMN_ID_INDEX = 0
        private const val COLUMN_NUMBER_INDEX = 1
        private const val COLUMN_DATE_INDEX = 2
        private const val COLUMN_NAME_INDEX = 3

        private const val CALL_SELECT = "(${CallLog.Calls.TYPE} = ${CallLog.Calls.INCOMING_TYPE}" +
                " OR ${CallLog.Calls.TYPE} = ${CallLog.Calls.MISSED_TYPE})"
    }

}
