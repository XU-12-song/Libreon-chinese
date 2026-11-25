// SPDX-License-Identifier: GPL-3.0-or-later
package dev.offlineoffice.app.ui.viewers.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.offlineoffice.app.text.LineEnding
import dev.offlineoffice.app.text.TextFormat

@Composable
fun GoToLineDialog(lineCount: Int, onGo: (Int) -> Unit, onDismiss: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val line = input.toIntOrNull()?.takeIf { it in 1..lineCount }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("跳转到行") },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { v -> input = v.filter(Char::isDigit).take(10) },
                label = { Text("行（1–$lineCount）") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        },
        confirmButton = { TextButton(onClick = { line?.let(onGo) }, enabled = line != null) { Text("跳转") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}

private val charsetChoices = listOf(
    "UTF-8" to false, "UTF-8" to true, "UTF-16LE" to true, "UTF-16BE" to true, "ISO-8859-1" to false, "windows-1252" to false,
)

/** Shows the encoding and line endings; when [editable], lets the user convert them for the next save. */
@Composable
fun EncodingDialog(current: TextFormat, editable: Boolean, onApply: (TextFormat) -> Unit, onDismiss: () -> Unit) {
    var charset by remember { mutableStateOf(current.charset to current.bom) }
    var ending by remember { mutableStateOf(current.lineEnding) }
    val charsets = (listOf(current.charset to current.bom) + charsetChoices).distinct()
    val endings = (listOf(LineEnding.LF, LineEnding.CRLF, LineEnding.CR) + current.lineEnding).distinct()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("编码和换行符") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    if (editable) "保存时会保持文件的原始格式。只有想转换文件时，才在此处更改。"
                    else "这个大文件是只读的。",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text("编码", Modifier.padding(top = 12.dp), style = MaterialTheme.typography.titleSmall)
                charsets.forEach { choice ->
                    ChoiceRow(choice.first + if (choice.second) "（带 BOM）" else "", charset == choice, editable) { charset = choice }
                }
                Text("换行符", Modifier.padding(top = 12.dp), style = MaterialTheme.typography.titleSmall)
                endings.forEach { e ->
                    val label = when (e) {
                        LineEnding.LF -> "LF（Unix、macOS）"
                        LineEnding.CRLF -> "CRLF（Windows）"
                        LineEnding.CR -> "CR（经典 Mac）"
                        LineEnding.MIXED -> "混合（逐行保留）"
                    }
                    ChoiceRow(label, ending == e, editable) { ending = e }
                }
            }
        },
        confirmButton = {
            if (editable) {
                TextButton(onClick = { onApply(TextFormat(charset.first, charset.second, ending)) }) { Text("应用") }
            } else {
                TextButton(onClick = onDismiss) { Text("关闭") }
            }
        },
        dismissButton = { if (editable) TextButton(onClick = onDismiss) { Text("取消") } },
    )
}

@Composable
private fun ChoiceRow(label: String, selected: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick).padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null, enabled = enabled)
        Text(label, Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodyMedium)
    }
}

/** Links in the preview are never opened (the app has no network); offer to copy the address. */
@Suppress("DEPRECATION") // LocalClipboardManager: the replacement LocalClipboard needs ClipData plumbing for a plain string
@Composable
fun LinkDialog(url: String, onDismiss: () -> Unit) {
    val clipboard = LocalClipboardManager.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("链接") },
        text = {
            Column {
                Text("Libreon 不会打开链接。", style = MaterialTheme.typography.bodyMedium)
                Text(url, Modifier.padding(top = 8.dp), style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            TextButton(onClick = { clipboard.setText(AnnotatedString(url)); onDismiss() }) { Text("复制链接") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("关闭") } },
    )
}

@Composable
fun JsonErrorDialog(message: String, line: Int, column: Int, onShow: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("无效的 JSON") },
        text = {
            Column {
                Text(message, style = MaterialTheme.typography.bodyMedium)
                Text("第 $line 行，第 $column 列", Modifier.padding(top = 8.dp), style = MaterialTheme.typography.titleSmall)
            }
        },
        confirmButton = { TextButton(onClick = onShow) { Text("显示") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("关闭") } },
    )
}
