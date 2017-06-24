package ru.org.adons.cblock.data

import android.provider.CallLog
import ru.org.adons.cblock.model.CallLogModel

/**
 * Data holder for [CallLog.Calls] item, used in [CallLogModel]
 */
class CallLogItem(val id: Long,
                  val phoneNumber: String,
                  val date: Long,
                  val name: String? = null,
                  var isBlocked: Boolean = false,
                  var isSelected: Boolean = false)