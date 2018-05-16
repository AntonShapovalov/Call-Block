package mobile.addons.cblock.data

import android.provider.CallLog
import mobile.addons.cblock.model.CallLogModel

/**
 * Data holder for [CallLog.Calls] item, used in [CallLogModel]
 */
class CallLogItem(val id: Long,
                  val phoneNumber: String,
                  val date: Long,
                  val name: String? = null,
                  var isBlocked: Boolean = false,
                  var isSelected: Boolean = false)