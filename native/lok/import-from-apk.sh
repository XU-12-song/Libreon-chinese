#!/usr/bin/env bash
# Fills third_party/lok/ from a distributed Libreon APK instead of a local LibreOffice build.
# The Office engine ships inside the app's APK (lib/<abi>/*.so plus the assets/ tree), so a
# build machine without the multi-hour LibreOffice cross-compile can reuse the engine from an
# official release. Used by .github/workflows/build.yml.
#   bash native/lok/import-from-apk.sh Libreon-0.9.5-beta.5-arm64-v8a.apk arm64-v8a
set -euo pipefail

APK="${1:?usage: import-from-apk.sh <apk> <arm64-v8a|x86_64>}"
ABI="${2:?usage: import-from-apk.sh <apk> <arm64-v8a|x86_64>}"
REPO="$(cd "$(dirname "$0")/../.." && pwd)"
OUT="$REPO/third_party/lok"

TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
# Extract the whole APK: an "assets/*" filter only matches the top level on some unzip
# builds, which silently drops the engine's asset tree.
unzip -q "$APK" -d "$TMP"
[ -f "$TMP/lib/$ABI/libandroidapp.so" ] || { echo "no Office engine in $APK for $ABI" >&2; exit 1; }

# Native libraries are per ABI; assets are the same for every ABI.
rm -rf "$OUT/jniLibs/$ABI" "$OUT/assets"
mkdir -p "$OUT/jniLibs/$ABI"
cp "$TMP/lib/$ABI"/*.so "$OUT/jniLibs/$ABI/"

# These belong to other modules of this app: PDFium and its JNI wrapper are built here, and
# androidx.graphics.path comes from the Compose dependency.
rm -f "$OUT/jniLibs/$ABI/liboopdf.so" "$OUT/jniLibs/$ABI/libpdfium.so" \
      "$OUT/jniLibs/$ABI/libandroidx.graphics.path.so"

mv "$TMP/assets" "$OUT/assets"
# Baseline profiles are produced by this build; the copy in the APK describes the old dex.
rm -rf "$OUT/assets/dexopt"

bash "$(dirname "$0")/finalize-lok.sh" "$OUT"

echo "imported the Office engine for $ABI from $APK"
