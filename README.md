<p align="center"><img src="art/libreon-store-icon-512.png" width="140" alt="Libreon：一只狐狸蜷绕在三块彩色方块周围"></p>

<h1 align="center">Libreon</h1>
<p align="center"><b>自由办公。</b><br>一款私密、完全离线的 Android 办公套件。无广告、无订阅、无追踪、无 AI。</p>

<p align="center">
  <a href="https://github.com/jainiakhil/Libreon/releases/latest"><img src="https://img.shields.io/badge/Download-Latest%20APK-2ea44f?style=for-the-badge&logo=android&logoColor=white" alt="下载最新 APK"></a>
  <a href="https://jainiakhil.github.io/Libreon/"><img src="https://img.shields.io/badge/Website-jainiakhil.github.io%2FLibreon-fc4e2b?style=for-the-badge&logo=googlechrome&logoColor=white" alt="访问 Libreon 网站"></a>
  <br><sub>在 <b>Assets</b> 下选择 <code>arm64-v8a</code> 的 APK：它适用于几乎所有手机。<a href="#install">安装帮助</a></sub>
</p>

<p align="center"><i>Beta 版：请做好遇到粗糙之处的准备，并请反馈问题。</i></p>

---

## 为什么会有它

你懂的。你只想在手机上打开一个 Word 文件。结果却先来一个全屏广告，一个“升级高级版！”弹窗，一次登录提示，又一个广告，一句“让我们的 AI 帮你总结一下”，最后终于看到文档——底部三分之一还压着一条横幅广告。

我受够了。每一款流行的 Android 办公应用似乎每两秒就想要我的钱、时时刻刻想要我的数据，或者考验我对没人要的 AI 功能的耐心。所以我做了点改变：我构建了一款干净重写的办公应用，能完成那些应用所做的事，只是去掉了那些乱七八糟的东西。

**无广告。无付费。无追踪。无 AI。无账号。甚至不需要联网。** 它能打开你的文件，让你编辑它们，从不丢失你未保存的改动，并且不碍你的事。你可以自由办公。

**关于 AI：** 这个 Android 应用由我借助两个 AI 编程助手 **Claude Code（Opus 5.5）** 和 **Google Antigravity（Gemini 3.8 Flash）** 构建，同时由我负责设计、审查代码并在真实手机上测试。打开、编辑和保存文档的部分是 [LibreOffice](https://www.libreoffice.org/) 和 [PDFium](https://pdfium.googlesource.com/pdfium/)，它们是由人们历经数十年打磨的成熟开源引擎。是的，一个内部没有 AI 的应用由 AI 构建，确实有点讽刺：AI 只是写了代码，并没有搬进应用里。而且它没有联网权限、每一行代码都公开，所以上面这些你都不必只听我的一面之词。

“干净重写”（clean room）意味着没有从任何现有应用获取代码、资源或素材。现有应用只被用来了解用户期望哪些功能。这里的一切要么从头编写，要么来自许可合规的开源项目（见下文）。

## 它能做什么

**Office 文档**
- 打开、编辑并保存 Word、Excel 和 PowerPoint 文件：`.docx` `.xlsx` `.pptx`，以及旧版 `.doc` `.xls` `.ppt`。
- 支持 OpenDocument（`.odt` `.ods` `.odp`，以及扁平格式 `.fodt` `.fods` `.fodp`）和 `.rtf`。
- Apple **Pages、Numbers 和 Keynote** 文件以只读方式打开。
- 文档默认以阅读方式打开，误触不会改动内容：点按页面右下角的铅笔按钮即可开始编辑。
- 在文档内查找（显示匹配数量）、缩放按钮和缩放级别（适应宽度、50%–400%），Word 文档还提供实时字数统计。
- **导出或分享为 PDF**，可直接打印（Android 的分享面板包含“打印”）。
- 与其他应用之间复制粘贴。
- 点按链接即可在你的浏览器或邮件应用中打开。

**PDF**
- 流畅查看，双指缩放最高 1600%（或缩小以同时看到多页）、预设缩放级别、缩略图和搜索。
- 填写 PDF 表单，在扁平表单上添加文字、对勾和叉号，并使用已保存的签名**签署**。
- 长按并拖动即可**高亮、加下划线或删除线**，还可**批注**：自由绘制、矩形、椭圆和便签。这些都是标准 PDF 注释，其他 PDF 应用也能看到。
- **页面工具：**旋转、移动、删除和提取页面，或插入另一份 PDF 的页面。
- 点按链接可在浏览器或邮件应用中打开，或跳转到文档的另一页。
- 复制选中的文字并统计其字数。

**文本**
- 纯文本、Markdown（带实时预览）、CSV（带表格视图）和 JSON（带格式化），另支持多种代码和配置文件类型。
- 可处理超大文件（几百 MB）而不会卡死。
- 查找替换、跳转到行，状态栏中还显示字数统计。

**处处皆是**
- **标签页：**可同时打开大量文档。
- **未保存的工作永不丢失。** Libreon 会为你正在编辑的每一份文档保留一份私密的恢复副本，在你停止输入几秒后更新。即使应用被关闭、崩溃或手机重启，所有标签页都会连同每一处未保存的改动一起回来，你准备好时再保存。恢复副本留在你的手机内，存于应用之中。
- 按名称**搜索**你打开过、最近使用和最近关闭的文件。
- **分享**任何打开的文档（包括未保存的编辑），并可**全屏**阅读。
- 对任何文档及所选内容进行**字数统计**。
- 浅色、深色或跟随系统的主题（在设置中切换），应用处于深色时可让单份文档使用“浅色页面”，以及随壁纸变化的主题图标。
- 对屏幕阅读器友好的控件。

## 隐私

Libreon **完全没有联网权限**。Android 本身就会阻止它上网。没有账号、没有分析统计、没有崩溃上报、没有任何“云”功能。你的文档从不离开你的设备。

应用中仅有的网页链接位于设置页面底部（GitHub 页面和下方的请喝咖啡横幅）。它们只在你点按时才在浏览器中打开；Libreon 本身始终不会上网。

## 安装

1. 从[最新版本](https://github.com/jainiakhil/Libreon/releases/latest)（**Assets** 下）下载 APK：
   - `arm64-v8a` 适用于几乎所有手机和平板；
   - `x86_64` 用于模拟器和部分 Chromebook。
2. 打开它，当 Android 询问时允许从浏览器或文件管理器安装。

需要 **Android 12 或更新版本**。下载包约 115 MB，因为里面装着一整套办公引擎。Office 文档还需要较新的 **Android System WebView**（98 或更新版本，2022 年起）；如果你的版本太旧，Libreon 会提示，从应用商店更新即可解决。

## 工作原理

Libreon 是一款原生 Android 应用（Kotlin 和 Jetpack Compose），内含三个引擎：

| 部件 | 使用什么 |
|---|---|
| **Office 文档** | [LibreOffice](https://www.libreoffice.org/) 核心，通过 LibreOfficeKit 接入，[Collabora Online](https://www.collaboraonline.com/) 网页前端在本地 WebView 中运行。每份 Office 文档都在独立进程中运行，并通过共享画面显示在应用里，因此一份重型文档不会把整个应用拖垮。 |
| **PDF** | [PDFium](https://pdfium.googlesource.com/pdfium/)（Chrome 内置的 PDF 引擎），通过一个小型 JNI 桥接，用于渲染、表单、注释和保存。 |
| **文本** | 基于 Compose 文本框构建的自定义编辑器，针对超大文件采用流式读取和行索引，[commonmark-java](https://github.com/commonmark/commonmark-java) 处理 Markdown，并自行实现 CSV 和 JSON 处理。 |

其他细节：
- **标签页与内存：**文档根据手机内存状况处于“热”状态（引擎已加载）或“停靠”状态（保存到恢复副本后卸载），因此你可以保留许多标签页而不会内存不足。
- **保存：**文件通过 Android 存储框架写回原始位置。如果期间其他应用改动了该文件，Libreon 会发现并在覆盖前征求你的意见。
- **签名：**发布版本使用一个不在本仓库中的私钥签名。

## 从源码构建

你需要带 WSL 2 的 Windows（或 Linux）、Android SDK 和 NDK，以及大量磁盘空间和耐心。Office 引擎需要数小时才能构建完成。

1. **Office 引擎：**在 WSL 发行版中先运行一次 `native/lok/provision.sh`，然后运行 `native/lok/build-all.sh arm64-v8a`（和/或 `x86_64`）。这会为 Android 构建 LibreOffice 核心和 Collabora 前端，并把结果复制到 `third_party/lok/`。
2. **PDFium：**运行 `scripts/fetch-pdfium.sh` 将预构建库下载到 `third_party/pdfium/`。
3. **应用：**`./gradlew assembleRelease`。如果 `signing/` 中没有签名密钥，发布构建将使用调试密钥签名。

图标各图层由 `art/make_icons.py` 从 `art/` 中的美术素材生成。

更多细节见：[架构](docs/architecture.md) 和[里程碑与测试结果](docs/milestones.md)。

## 状态

这是一个 **Beta** 版本。它已在 **Samsung Galaxy S24 Ultra**（Android 17、One UI 9）、Android 模拟器（包括 Android 12）和 BlueStacks 上测试过。更多真机测试仍在进行中，非常欢迎[提交 bug 报告](../../issues/new?template=bug_report.yml)。已知的不足：
- Office 文档目前只能导出为 PDF（尚未提供其他格式）。打印通过“分享为 PDF”完成。
- 扫描件 PDF 尚不支持文字识别（OCR），并且 PDF 中已有的文字无法编辑。
- Apple iWork 文件只能查看。

## 支持 Libreon

Libreon 免费，而且永远免费：无广告、无订阅、没有“高级版”、没有骚扰。如果它为你省下了一些钱或减少了一些烦恼，你想表示感谢，可以[请我喝杯咖啡](https://buymeacoffee.com/jainiakhil)。

这是它**唯一**会出现的地方：设置页面（⋮ → 设置）最底部的一个小横幅。没有弹窗、没有提醒、不扣留任何功能。完全可以选择忽略。

## 参与贡献

Bug 报告、想法和修复都欢迎，而且你不需要是开发者也能帮忙。

- **发现了 bug？**请[提交 bug 报告](../../issues/new?template=bug_report.yml)。你也可以在应用里找到入口：⋮ → 设置 → *报告问题或建议功能*。
- **有想法？**请[建议新功能](../../issues/new?template=feature_request.yml)，或给现有的建议点个 👍。
- **想自己动手修复？**Fork 本仓库，在分支上完成修改，然后发起 pull request。[CONTRIBUTING.md](CONTRIBUTING.md) 会一步步带你完成，同时说明每次改动都要遵守的几条规则（离线、无广告或追踪器、干净重写）。

## 许可证

Libreon 是自由软件，基于 [GNU 通用公共许可证第 3 版或更新版本](LICENSE) 发布。

它的 Office 引擎基于 The Document Foundation 的 LibreOffice 和 Collabora Online 前端（两者均为 MPL-2.0）。Libreon 与 The Document Foundation 或 Collabora 没有关联。每一个第三方组件及其许可证都列在 [docs/licenses.md](docs/licenses.md) 和应用的“开源许可证”页面中。
