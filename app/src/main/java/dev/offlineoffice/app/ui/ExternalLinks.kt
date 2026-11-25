// SPDX-License-Identifier: GPL-3.0-or-later
package dev.offlineoffice.app.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Hands a link from a document (web page, e-mail address or phone number) to whichever app
 * handles it, usually the browser. Libreon itself has no internet access and never opens it.
 */
fun openExternalLink(context: Context, uri: String) {
    val mail = uri.startsWith("mailto:", ignoreCase = true)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).addCategory(Intent.CATEGORY_BROWSABLE)
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, if (mail) "未安装电子邮件应用" else "没有应用可以打开此链接", Toast.LENGTH_SHORT).show()
    } catch (_: SecurityException) {
        Toast.makeText(context, "没有应用可以打开此链接", Toast.LENGTH_SHORT).show()
    }
}
