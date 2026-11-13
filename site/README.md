# Libreon website

A static one-page site: `index.html`, `styles.css`, `main.js` and `assets/`. It loads nothing from other servers, so it works on GitHub Pages as is.

## Editing

`index.html` is generated. Edit `_src/index.template.html`, then rebuild from the repo root:

```
python site/_src/build.py "path/to/repo"
```

The build inlines the fox artwork from `art/Libreon_Artwork_*.svg` wherever the template says `{{fox:Name}}`. GitHub Pages doesn't serve the `_src/` folder.

## Adding screenshots and video

The "See it" section has seven placeholder slots, each marked with a `<!-- SLOT ... -->` comment in the template. To fill a slot, replace its one placeholder line:

```html
<div class="ph ph--doc" role="img" aria-label="Screenshot coming soon"></div>
```

with an image:

```html
<img src="assets/screens/office.png" alt="An Office document open for editing in Libreon" loading="lazy">
```

For the video slot (the first one), use a video instead:

```html
<video src="assets/video/promo.mp4" poster="assets/video/promo.jpg" controls playsinline preload="none"></video>
```

- The frames are phone-shaped (9 : 19.5). Images and video are cropped to fill the frame (`object-fit: cover`), so portrait phone screenshots fit best.
- For a landscape video, add `shot--wide` to that slot's `<figure>` (`<figure class="shot shot--video shot--wide">`). The frame becomes 16 : 9.
- Edit the `<figcaption>` under each frame to change its caption. Delete a whole `<figure>` to drop a slot.
- Keep files small (about 1080 px tall WebP or PNG for screenshots, and a short compressed MP4 for video), and rebuild afterwards.
