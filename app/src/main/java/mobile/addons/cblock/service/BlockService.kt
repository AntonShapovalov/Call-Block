package mobile.addons.cblock.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import mobile.addons.cblock.app.BlockManager
import mobile.addons.cblock.app.CBlockApplication
import mobile.addons.cblock.ext.hideNotification
import mobile.addons.cblock.ext.safeUnsubscribe
import mobile.addons.cblock.ext.showNotification
import mobile.addons.cblock.model.BlockListModel
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

class BlockService : Service() {

    companion object {
        @Volatile private var isEnabled = false

        // Start Block Service in foreground mode
        @Synchronized fun start(context: Context) {
            if (!isEnabled) {
                isEnabled = true
                context.startService(Intent(context, BlockService::class.java))
            }
        }

        // Stop Block Service
        @Synchronized fun stop(context: Context) {
            isEnabled = false
            context.stopService(Intent(context, BlockService::class.java))
        }
    }

    private val subscription = CompositeSubscription()
    private val phones = HashSet<String>()

    private lateinit var listener: StateListener
    private lateinit var manager: TelephonyManager

    @Inject lateinit var blockListModel: BlockListModel
    @Inject lateinit var blockManager: BlockManager

    override fun onCreate() {
        super.onCreate()
        initComponents()
        getData()
        getDataUpdate()
        setPhoneListener()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        showNotification()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        subscription.safeUnsubscribe()
        manager.listen(listener, PhoneStateListener.LISTEN_NONE)
        hideNotification()
        synchronized(this) { isEnabled = false }
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun setPhoneListener() {
        manager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        listener = StateListener(applicationContext, manager, phones)
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun initComponents() {
        val application = application as CBlockApplication
        val serviceComponent = DaggerServiceComponent.builder()
                .applicationComponent(application.applicationComponent)
                .build()
        serviceComponent.inject(this)
    }

    // get blocked phones list and
    private fun getData() {
        val dataSubscription = blockListModel.getBlockedPhones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setPhones)
        subscription.add(dataSubscription)
    }

    // subscribe to block list update from BlockManager
    private fun getDataUpdate() {
        val updateSubscription = blockManager.getBlockListUpdate()
                .map { blockListModel.getBlockedPhones(it) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setPhones)
        subscription.add(updateSubscription)
    }

    private fun setPhones(values: Set<String>) {
        phones.clear()
        phones.addAll(values)
    }

}
