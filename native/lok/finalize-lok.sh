#!/usr/bin/env bash
# Post-processes third_party/lok/ after its libraries and assets were put in place, whether
# they came from a local LibreOffice build (package-lok.sh) or from a distributed APK
# (import-from-apk.sh). Safe to run more than once.
set -euo pipefail

OUT="${1:?usage: finalize-lok.sh <third_party/lok>}"
[ -d "$OUT/assets" ] || { echo "missing $OUT/assets" >&2; exit 1; }

# Upstream bug: a few dark-theme icons (the slideshow button among them) keep the light
# theme's dark grey, so on a dark toolbar they look disabled. Give them the dark theme's ink.
# Newer builds already ship the corrected icons, in which case grep matches nothing and
# would fail the script under `set -e`.
{ grep -l 'fill="#3a3a38"' "$OUT/assets/dist/images/dark/"*.svg 2>/dev/null || true; } |
  xargs -r sed -i 's/fill="#3a3a38"/fill="#fafafa"/g'

# The page links branding.css for host-specific styles; upstream ships none, so ours goes there.
#  - Message dialogs (e.g. "Font Disallows Editing") show their text as an inline label: the
#    label's top border is drawn on every wrapped line, its margin only on the first, and the
#    message's own line breaks are lost. Lay it out as a block that keeps the breaks.
cat > "$OUT/assets/dist/branding.css" <<'CSS'
.mobile-wizard-content > .mobile-wizard.static-label.ui-text {
  display: block !important;
  white-space: pre-line !important;
  line-height: 1.45 !important;
  margin: 0 !important;
  padding: 16px 4% !important;
  border: none !important;
}
.mobile-wizard-content > .mobile-wizard.static-label.ui-text:empty { display: none !important; }
/* The top-left toolbar button: a tick in edit mode (back to reading, kept), but in view mode it
   closes the document, which in our app leaves a blank page. The app's own Home button and back
   gesture replace it there. */
#toolbar-mobile-back.editmode-off { display: none !important; }
/* Zoom buttons the app adds to the page's toolbar (OfficeDocument.ZOOM_BUTTONS_JS). */
#libreon-zoom { display: flex !important; align-items: center; gap: 2px; padding-left: 4px; }
#libreon-zoom .libreon-zoom-btn {
  width: 36px !important; height: 34px !important; padding: 5px !important; margin: 0 !important;
  border: none !important; background: transparent !important; box-shadow: none !important; border-radius: 6px;
}
#libreon-zoom .libreon-zoom-btn:active { background: rgba(127, 127, 127, 0.25) !important; }
#libreon-zoom .libreon-zoom-btn img { width: 24px; height: 24px; }
#libreon-zoom .libreon-zoom-level {
  min-width: 52px !important; height: 34px !important; padding: 0 6px !important; margin: 0 !important;
  border: none !important; background: transparent !important; box-shadow: none !important; border-radius: 6px;
  color: var(--color-main-text) !important; font-size: 14px !important; font-weight: 600;
}
#libreon-zoom .libreon-zoom-level:active { background: rgba(127, 127, 127, 0.25) !important; }
#libreon-zoom-menu {
  position: fixed; z-index: 3000; min-width: 120px; padding: 4px 0;
  background: var(--color-background-lighter); color: var(--color-main-text);
  border: 1px solid var(--color-border); border-radius: 8px; box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3);
}
#libreon-zoom-menu[hidden] { display: none !important; }
#libreon-zoom-menu button {
  display: block !important; width: 100% !important; margin: 0 !important; padding: 10px 16px !important;
  border: none !important; background: transparent !important; color: inherit !important;
  text-align: left; font-size: 15px !important; box-shadow: none !important;
}
#libreon-zoom-menu button:active { background: rgba(127, 127, 127, 0.25) !important; }
/* PowerPoint's slide strip (portrait): in a presentation with sections, each section's title bar
   sits in the strip as a full-width block and wraps the thumbnails onto rows below the visible
   strip. Keep one row, with a compact section label before each section's slides. */
#slide-sorter.portrait { white-space: nowrap !important; }
#slide-sorter.portrait .slide-section-header {
  display: inline-flex !important; vertical-align: top; align-items: center;
  width: auto !important; height: 60px !important; max-width: 96px;
  margin: 0 6px 0 0 !important; white-space: nowrap;
}
#slide-sorter.portrait .slide-section-name {
  height: auto !important; max-width: 72px; overflow: hidden; text-overflow: ellipsis; font-size: 12px;
}
CSS

echo "finalized $OUT"
