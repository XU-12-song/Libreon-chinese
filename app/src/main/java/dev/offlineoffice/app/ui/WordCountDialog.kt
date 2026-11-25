// SPDX-License-Identifier: GPL-3.0-or-later
package dev.offlineoffice.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.offlineoffice.app.session.Count
import dev.offlineoffice.app.session.DocumentTools
import dev.offlineoffice.app.session.WordCount
import java.text.NumberFormat

/** Asks [tools] for the counts of the document called [displayName]. */
class WordCountRequest(val displayName: String, val tools: DocumentTools)

@Composable
fun WordCountDialog(request: WordCountRequest, onDismiss: () -> Unit) {
    val result by produceState<Result<WordCount>?>(null, request) {
        value = runCatching { request.tools.wordCount() }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("字数统计") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(request.displayName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                val r = result
                when {
                    r == null -> CircularProgressIndicator(Modifier.padding(8.dp))
                    r.isFailure -> Text("无法统计：${r.exceptionOrNull()?.message}")
                    else -> {
                        val count = r.getOrThrow()
                        CountRows("整篇文档", count.document)
                        CountRows("所选内容", count.selection, empty = "未选择任何内容")
                        count.note?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("关闭") } },
    )
}

@Composable
private fun CountRows(label: String, count: Count?, empty: String = "不可用") {
    val nf = remember { NumberFormat.getIntegerInstance() }
    Column {
        Text(label, style = MaterialTheme.typography.titleSmall)
        if (count == null) {
            Text(empty, style = MaterialTheme.typography.bodyMedium)
        } else {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("字数")
                Text(nf.format(count.words))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("字符数")
                Text(nf.format(count.chars))
            }
        }
    }
}
