package ru.org.adons.cblock.data;

import android.net.Uri;
import android.provider.CallLog;

import com.squareup.sqlbrite.BriteContentResolver;

import java.util.List;

import javax.inject.Inject;

import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.scope.ViewScope;
import rx.Observable;

/**
 * Provide data from {@link CallLog} for incoming or missed calls list
 */

@ViewScope
public class CallLogModel {

    @Inject BriteContentResolver resolver;

    @Inject
    public CallLogModel() {
    }

    /**
     * @return list of incoming or missed calls
     */
    public Observable<List<CallLogItem>> getCallLogList() {
        return resolver.createQuery(CALLS_URI, CALLS_SUMMARY_PROJECTION,
                CALL_SELECT, null, CallLog.Calls.DEFAULT_SORT_ORDER, false)
                .mapToList(cursor -> CallLogItem.builder()
                        .setId(cursor.getLong(COLUMN_ID_INDEX))
                        .setPhoneNumber(cursor.getString(COLUMN_NUMBER_INDEX))
                        .setDate(cursor.getLong(COLUMN_DATE_INDEX))
                        .setName(cursor.getString(COLUMN_NAME_INDEX))
                        .build());
    }

    private static final Uri CALLS_URI = CallLog.Calls.CONTENT_URI;
    private static final String[] CALLS_SUMMARY_PROJECTION = new String[]{
            CallLog.Calls._ID,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.CACHED_NAME
    };
    private static final int COLUMN_ID_INDEX = 0;
    private static final int COLUMN_NUMBER_INDEX = 1;
    private static final int COLUMN_DATE_INDEX = 2;
    private static final int COLUMN_NAME_INDEX = 3;

    private static final String CALL_SELECT = "("
            + CallLog.Calls.TYPE + " = " + CallLog.Calls.INCOMING_TYPE
            + " OR " + CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE
            + ")";

}
