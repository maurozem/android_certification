package br.com.programadordeelite.gdc.codelab.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DynamicReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Toast.makeText(context, "app reinicio com sucesso", Toast.LENGTH_LONG).show()
        }
        if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            Toast.makeText(context, "modo aviao alterado", Toast.LENGTH_LONG).show()
        }
    }
}