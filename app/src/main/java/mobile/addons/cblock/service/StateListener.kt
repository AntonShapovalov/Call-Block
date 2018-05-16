package mobile.addons.cblock.service

import android.content.Context
import android.media.AudioManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.android.internal.telephony.ITelephony
import mobile.addons.cblock.ext.log
import java.util.*

/**
 * Phone State Listener, used in [BlockService]
 */
class StateListener(context: Context, manager: TelephonyManager, private val phones: HashSet<String>) : PhoneStateListener() {

    private val audio: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private var telephony: ITelephony? = try {
        val c = TelephonyManager::class.java
        val m = c.getDeclaredMethod("getITelephony")
        m.isAccessible = true
        m.invoke(manager) as ITelephony
    } catch (e: Exception) {
        log(e.message ?: "getITelephony exception")
        null
    }

    override fun onCallStateChanged(state: Int, incomingNumber: String) {
        super.onCallStateChanged(state, incomingNumber)
        when (state) {
            TelephonyManager.CALL_STATE_RINGING ->
                if (phones.contains(incomingNumber)) {
                    // mute
                    val ringState = audio.ringerMode
                    val isMuted = mute(ringState)
                    // try to end call
                    endCall()
                    log("blocked number:$incomingNumber")
                    // un-mute
                    if (isMuted) audio.ringerMode = ringState
                }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
            }
            TelephonyManager.CALL_STATE_IDLE -> {
            }
            else -> {
            }
        }
    }

    private fun endCall() {
        if (telephony != null) {
            try {
                telephony?.endCall()
            } catch (e: Exception) {
                log(e.message ?: "endCall exception")
            }
        }
    }

    private fun mute(ringState: Int): Boolean = if (ringState != AudioManager.RINGER_MODE_SILENT) {
        try {
            audio.ringerMode = AudioManager.RINGER_MODE_SILENT
            true
        } catch (e: Exception) {
            log(e.message ?: "mute exception")
            false
        }
    } else {
        false
    }

}