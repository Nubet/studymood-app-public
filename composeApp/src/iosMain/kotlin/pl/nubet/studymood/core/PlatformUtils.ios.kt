package pl.nubet.studymood.core

import pl.nubet.studymood.core.logging.Log
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(url: String) {
    try {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    } catch (e: Exception) {
        Log.e("Error opening URL: ${e.message}", e, tag = "PlatformUtils")
    }
}
