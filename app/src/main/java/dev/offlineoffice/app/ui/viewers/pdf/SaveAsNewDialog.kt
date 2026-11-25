// SPDX-License-Identifier: GPL-3.0-or-later
package dev.offlineoffice.app.ui.viewers.pdf

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Shown by Save when the session advises a new file (e.g. a signed PDF): saving a copy is
 * the default and keeps the original untouched; overwriting stays available.
 */
@Composable
fun SaveAsNewDialog(
    displayName: String,
    suggestedName: String,
    onSaveAsNew: () -> Unit,
    onOverwrite: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("保存已签名的文档") },
        text = {
            Column {
                Text("将签名后的副本保存为新文件（“$suggestedName”），并保持“$displayName”不变？")
            }
        },
        confirmButton = { TextButton(onClick = onSaveAsNew) { Text("另存为新文件") } },
        dismissButton = {
            Column {
                TextButton(onClick = onOverwrite) { Text("覆盖原文件") }
                TextButton(onClick = onDismiss) { Text("取消") }
            }
        },
    )
}
