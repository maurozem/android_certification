package br.com.programadordeelite.gdc.codelab.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import br.com.programadordeelite.gdc.R
import kotlinx.android.synthetic.main.fragment_notification.*

private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETED_NOTIFICATIONS"

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var notificationManager: NotificationManager
    private val notificationReceiver = NotificationReceiver()
    private val dynamicReceiver = DynamicReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setButtonStates(true, false, false)
        createNotificationChannel()
        registerNotificationReceiver()
        registerDynamicReceiver(dynamicReceiver)
    }

    private fun createNotificationChannel() {
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificalChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificalChannel.enableVibration(true)
            notificalChannel.description = "Notification from Mascot"
            notificalChannel.enableLights(true)
            notificalChannel.lightColor = Color.RED
            notificationManager.createNotificationChannel(notificalChannel)
        }
    }

    private fun setButtonStates(
        enableNoticfy: Boolean,
        enableUpdate: Boolean,
        enableCancel: Boolean
    ) {
        notify.isEnabled = enableNoticfy
        update.isEnabled = enableUpdate
        cancel.isEnabled = enableCancel
    }

    private fun setupListeners() {
        notify.setOnClickListener { sendNotification() }
        update.setOnClickListener { updateNotification() }
        cancel.setOnClickListener { cancelNotification() }
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setButtonStates(true, false, false)
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.ic_notification)
        val notification = getNotificationBuilder()
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Notificação atualizada")
            )

        notificationManager.notify(NOTIFICATION_ID, notification.build())
        setButtonStates(false, false, true)
    }

    private fun sendNotification() {
        val builder = getNotificationBuilder()

        createNotificationAction(builder, NOTIFICATION_ID, ACTION_UPDATE, "Atualize")
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_CANCEL, "Remover")

        val deleteAllAction = Intent(ACTION_DELETE_ALL)
        val deleteAction = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            deleteAllAction,
            PendingIntent.FLAG_ONE_SHOT
        )
        builder.setDeleteIntent(deleteAction)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        setButtonStates(false, true, true)
    }

    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter  = Intent(actionId)
        val updateAction = PendingIntent.getBroadcast(
            requireContext(),
            notificationId,
            updateActionFilter,
            PendingIntent.FLAG_ONE_SHOT
        )
        builder.addAction(
            R.drawable.ic_android,
            actionTitle,
            updateAction
        )
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), NotificationFragment::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("Vc recebeu uma notificação")
            .setContentText("parabéns por estar estudando notifications")
            .setSmallIcon(R.drawable.ic_notification_update)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)
    }

    private fun registerNotificationReceiver() {
        val notificationActionFilters = IntentFilter()
        notificationActionFilters.addAction(ACTION_UPDATE)
        notificationActionFilters.addAction(ACTION_DELETE_ALL)
        notificationActionFilters.addAction(ACTION_CANCEL)
        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                ACTION_UPDATE -> updateNotification()
                ACTION_CANCEL -> {
                    notificationManager.cancel(NOTIFICATION_ID)
                    setButtonStates(true, false, false)
                }
                ACTION_DELETE_ALL -> setButtonStates(
                    true, false, false
                )
            }
        }

    }

    private fun registerDynamicReceiver(dynamicReceiver: BroadcastReceiver) {
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            requireActivity().registerReceiver(dynamicReceiver, it)
        }
    }

    private fun unregisterDynamicReceiver() {
        requireActivity().unregisterReceiver(dynamicReceiver)
    }

    override fun onStop() {
        super.onStop()
        unregisterDynamicReceiver()
    }
}












