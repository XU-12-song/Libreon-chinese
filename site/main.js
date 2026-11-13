// Libreon site: scroll reveal, pausing fox animations while they are off screen, the
// light/dark switch and the "Get it" celebration.
// No network and no cookies. The only thing stored is the theme you pick, in this browser.
(function () {
  'use strict';
  var root = document.documentElement;
  var reveal = Array.prototype.slice.call(document.querySelectorAll('[data-reveal]'));
  var scenes = Array.prototype.slice.call(document.querySelectorAll('.scene'));

  // Stagger the stickers, so the strike-throughs land one after another.
  document.querySelectorAll('[data-reveal-group]').forEach(function (group) {
    Array.prototype.forEach.call(group.children, function (el, i) {
      el.style.setProperty('--delay', (i * 0.06).toFixed(2) + 's');
      el.style.setProperty('--strike-delay', (0.3 + i * 0.08).toFixed(2) + 's');
    });
  });

  // Light/dark switch. Without a choice the page follows the system.
  var toggle = document.querySelector('.theme-toggle');
  var darkQuery = window.matchMedia('(prefers-color-scheme: dark)');
  function isDark() { return root.dataset.theme ? root.dataset.theme === 'dark' : darkQuery.matches; }
  function labelToggle() {
    if (toggle) toggle.setAttribute('aria-label', isDark() ? 'Switch to light mode' : 'Switch to dark mode');
  }
  if (toggle) {
    labelToggle();
    toggle.addEventListener('click', function () {
      var next = isDark() ? 'light' : 'dark';
      root.dataset.theme = next;
      try { localStorage.setItem('libreon-theme', next); } catch (e) {}
      labelToggle();
      toggle.classList.remove('is-spinning');
      void toggle.offsetWidth;
      toggle.classList.add('is-spinning');
    });
    if (darkQuery.addEventListener) darkQuery.addEventListener('change', labelToggle);
  }

  // "Get it": scroll to the download button, which grows, wobbles, glows and throws confetti.
  var download = document.getElementById('download');
  var heroArt = document.querySelector('.hero__art');
  var reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)');
  var partyTimer = null;
  function confetti(target) {
    var r = target.getBoundingClientRect();
    var colors = ['#fc4e2b', '#ff8e1a', '#188bff', '#20c56e', '#ffdd00', '#8914de'];
    for (var i = 0; i < 46; i++) {
      var bit = document.createElement('span');
      bit.className = 'confetti-bit' + (i % 3 === 0 ? ' confetti-bit--round' : '');
      var angle = Math.random() * Math.PI * 2;
      var dist = 90 + Math.random() * 190;
      bit.style.left = (r.left + r.width / 2 + (Math.random() - .5) * r.width * .6) + 'px';
      bit.style.top = (r.top + r.height / 2) + 'px';
      bit.style.background = colors[i % colors.length];
      bit.style.setProperty('--dx', Math.round(Math.cos(angle) * dist) + 'px');
      bit.style.setProperty('--dy', Math.round(Math.sin(angle) * dist * .8 + 60) + 'px');
      bit.style.setProperty('--rot', Math.round((Math.random() - .5) * 900) + 'deg');
      bit.style.setProperty('--t', (1.1 + Math.random() * .7).toFixed(2) + 's');
      document.body.appendChild(bit);
      setTimeout(function (b) { return function () { b.remove(); }; }(bit), 2000);
    }
  }
  function party() {
    if (!download) return;
    clearTimeout(partyTimer);
    download.classList.remove('is-party', 'is-big');
    if (heroArt) heroArt.classList.remove('is-cheering');
    void download.offsetWidth;
    download.classList.add('is-party');
    if (heroArt) heroArt.classList.add('is-cheering');
    if (!reduceMotion.matches) setTimeout(function () { confetti(download); }, 180);
    partyTimer = setTimeout(function () {
      download.classList.remove('is-party');
      download.classList.add('is-big');
      if (heroArt) heroArt.classList.remove('is-cheering');
      partyTimer = setTimeout(function () { download.classList.remove('is-big'); }, 4000);
    }, 4700);
  }
  document.querySelectorAll('.js-get').forEach(function (link) {
    link.addEventListener('click', function (e) {
      if (!download) return;
      e.preventDefault();
      var r = download.getBoundingClientRect();
      var visible = r.top > 70 && r.bottom < window.innerHeight;
      if (visible) { party(); return; }
      download.scrollIntoView({ behavior: reduceMotion.matches ? 'auto' : 'smooth', block: 'center' });
      // Celebrate once the scroll has settled.
      var last = -1, still = 0;
      (function wait() {
        var y = window.scrollY;
        still = y === last ? still + 1 : 0;
        last = y;
        if (still > 3) party(); else requestAnimationFrame(wait);
      })();
      download.focus({ preventScroll: true });
    });
  });

  // A link to a collapsed <details> (e.g. "Install help") opens it.
  function openTarget() {
    var el = location.hash && document.getElementById(location.hash.slice(1));
    if (el && el.tagName === 'DETAILS') { el.open = true; el.classList.add('is-in'); }
  }
  window.addEventListener('hashchange', openTarget);
  openTarget();

  if (!('IntersectionObserver' in window)) {
    reveal.forEach(function (el) { el.classList.add('is-in'); });
    scenes.forEach(function (el) { el.classList.add('is-in', 'is-playing'); });
    return;
  }

  var revealer = new IntersectionObserver(function (entries) {
    entries.forEach(function (e) {
      if (e.isIntersecting) {
        e.target.classList.add('is-in');
        revealer.unobserve(e.target);
      }
    });
  }, { rootMargin: '0px 0px -8% 0px', threshold: 0.12 });
  reveal.forEach(function (el) { revealer.observe(el); });

  var player = new IntersectionObserver(function (entries) {
    entries.forEach(function (e) {
      e.target.classList.toggle('is-playing', e.isIntersecting);
      if (e.isIntersecting) e.target.classList.add('is-in');
    });
  }, { rootMargin: '60px 0px' });
  scenes.forEach(function (el) { player.observe(el); });

  // If the page is opened at an anchor, show everything above it straight away.
  window.addEventListener('load', function () {
    reveal.forEach(function (el) {
      if (el.getBoundingClientRect().bottom < 0) el.classList.add('is-in');
    });
  });
  root.classList.add('js-ready');
})();
