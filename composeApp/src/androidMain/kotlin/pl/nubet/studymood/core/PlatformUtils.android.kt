package pl.nubet.studymood.core

import android.content.Intent
import android.net.Uri
import pl.nubet.studymood.appContext
import pl.nubet.studymood.core.logging.Log

actual fun openUrl(url: String) {
    try {
        val context = appContext
        if (context != null) {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            context.startActivity(intent)
        } else {
            Log.w("Application context not available", tag = "PlatformUtils")
        }
    } catch (e: Exception) {
        Log.e("Error opening URL: ${e.message}", e, tag = "PlatformUtils")
    }
}
