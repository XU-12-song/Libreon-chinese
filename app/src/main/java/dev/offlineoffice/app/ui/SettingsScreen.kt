// SPDX-License-Identifier: GPL-3.0-or-later
package dev.offlineoffice.app.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import dev.offlineoffice.app.R
import dev.offlineoffice.app.data.Settings

/** Full-screen settings: theme, the tab bar, and about. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settings: Settings, onLicenses: () -> Unit, onDismiss: () -> Unit) {
    val theme by settings.theme.collectAsState()
    val tabBar by settings.tabBar.collectAsState()
    val context = LocalContext.current
    val version = remember {
        runCatching { context.packageManager.getPackageInfo(context.packageName, 0).versionName }.getOrNull().orEmpty()
    }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        // Drawn edge to edge: give the status and navigation bar icons the theme's contrast.
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        val dark = LocalDarkTheme.current
        SideEffect {
            window?.let { WindowCompat.getInsetsController(it, it.decorView) }?.apply {
                isAppearanceLightStatusBars = !dark
                isAppearanceLightNavigationBars = !dark
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("设置") },
                    navigationIcon = { IconButton(onClick = onDismiss) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "返回") } },
                )
            },
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                SectionTitle("外观")
                Column(Modifier.selectableGroup()) {
                    Settings.ThemeMode.entries.forEach { mode ->
                        ListItem(
                            modifier = Modifier.selectable(selected = theme == mode, role = Role.RadioButton) { settings.setTheme(mode) },
                            headlineContent = { Text(mode.label) },
                            supportingContent = if (mode == Settings.ThemeMode.SYSTEM) ({ Text("浅色或深色，与这台设备的设置一致") }) else null,
                            leadingContent = { RadioButton(selected = theme == mode, onClick = null) },
                        )
                    }
                }
                Text(
                    "Office 文档会跟随应用主题。如果想在应用处于深色时让某份文档使用浅色页面，请在它的菜单中使用“浅色页面”。",
                    Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                SectionTitle("标签页")
                ListItem(
                    modifier = Modifier.toggleable(value = tabBar, role = Role.Switch) { settings.setTabBar(it) },
                    headlineContent = { Text("宽屏上的标签栏") },
                    supportingContent = { Text("在横屏和平板上显示。顶栏中的箭头按钮也可以隐藏它。") },
                    trailingContent = { Switch(checked = tabBar, onCheckedChange = null) },
                )
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                SectionTitle("关于")
                ListItem(
                    headlineContent = { Text("${stringResource(R.string.app_name)} $version") },
                    supportingContent = { Text("${stringResource(R.string.app_tagline)}。完全离线：无联网权限、无账号、无分析统计。") },
                )
                ListItem(
                    modifier = Modifier.selectable(selected = false, role = Role.Button, onClick = onLicenses),
                    headlineContent = { Text("开源许可证") },
                )
                ListItem(
                    modifier = Modifier.selectable(selected = false, role = Role.Button) { openInBrowser(context, ISSUES_URL) },
                    headlineContent = { Text("报告问题或建议功能") },
                    supportingContent = { Text("在浏览器中打开 Libreon 的 GitHub 页面") },
                    leadingContent = { Icon(Icons.Outlined.BugReport, null) },
                )
                CoffeeBanner(onClick = { openInBrowser(context, COFFEE_URL) })
                Image(
                    painterResource(R.drawable.libreon_logo),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp, bottom = 32.dp).size(120.dp),
                )
            }
        }
    }
}

private const val ISSUES_URL = "https://github.com/jainiakhil/Libreon/issues"
private const val COFFEE_URL = "https://buymeacoffee.com/jainiakhil"

/**
 * Hands a web link to the browser. Libreon has no internet permission and never loads
 * anything itself; these links only open when the user taps them.
 */
private fun openInBrowser(context: Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addCategory(Intent.CATEGORY_BROWSABLE))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "找不到可用于打开 $url 的浏览器", Toast.LENGTH_LONG).show()
    }
}

/** The one place the app asks for support: a tip jar at the very end of Settings. */
@Composable
private fun CoffeeBanner(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = MaterialTheme.shapes.large,
        color = Color(0xFFFFDD00),
        contentColor = Color(0xFF1E1E1E),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.LocalCafe, null, Modifier.size(36.dp))
            Column(Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text("喜欢 Libreon 吗？", style = MaterialTheme.typography.titleMedium)
                Text(
                    "它免费，没有广告也没有订阅。如果它帮到了你，可以请我喝杯咖啡。",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Icon(Icons.AutoMirrored.Outlined.OpenInNew, "在浏览器中打开", Modifier.size(20.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        Modifier.padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
    )
}
