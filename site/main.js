// Libreon site: scroll reveal, and pausing fox animations while they are off screen.
// No network, no storage, no cookies.
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
