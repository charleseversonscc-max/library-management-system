(function () {
  var script = document.currentScript;
  var base = script.src.replace(/\/js\/layout\.js.*$/, '');

  function el(tag, cls, text) {
    var e = document.createElement(tag);
    if (cls) e.className = cls;
    if (text) e.textContent = text;
    return e;
  }

  function link(href, text, cls) {
    var a = el('a', cls, text);
    a.href = href;
    return a;
  }

  function buildNav(user) {
    var nav = el('nav', 'navbar navbar-lib px-3 flex-wrap');
    var home = '/pages/login.html';
    if (user) home = user.role === 'ADMIN' ? '/pages/admin.html' : '/member/dashboard';
    nav.appendChild(link(base + home, 'Library System', 'navbar-brand mb-0 h1'));
    if (!user) return nav;
    var btn = el('button', 'btn btn-gold btn-sm', 'Menu');
    btn.type = 'button';
    var menu = el('div', 'w-100 d-none pt-2 pb-1');
    var items = user.role === 'ADMIN'
      ? [['Admin panel', '/pages/admin.html'], ['Books', '/books'], ['Manage books', '/admin/books'], ['Members', '/admin/members'], ['Issue and return', '/admin/loans']]
      : [['My account', '/member/dashboard'], ['Books', '/books'], ['My profile', '/member/profile']];
    items.forEach(function (it) {
      var a = link(base + it[1], it[0], 'd-block py-1');
      a.style.color = '#fff';
      menu.appendChild(a);
    });
    var who = el('div', 'small py-1', 'Signed in as @' + user.username + ' (' + user.role.toLowerCase() + ')');
    who.style.color = '#d9e4de';
    menu.appendChild(who);
    menu.appendChild(link(base + '/logout', 'Log out', 'btn btn-gold btn-sm mt-1'));
    btn.addEventListener('click', function () { menu.classList.toggle('d-none'); });
    nav.appendChild(btn);
    nav.appendChild(menu);
    return nav;
  }

  function mount(user) {
    var slot = document.getElementById('app-nav');
    if (slot) slot.replaceChildren(buildNav(user));
  }

  var foot = document.getElementById('app-footer');
  if (foot) {
    foot.replaceChildren(el('footer', 'text-center small text-muted py-3 mt-4',
      'Library System \u00b7 Group project \u00b7 ' + new Date().getFullYear()));
  }
  mount(null);
  fetch(base + '/me', { credentials: 'same-origin' })
    .then(function (r) { return r.ok ? r.json() : null; })
    .then(function (u) { if (u && u.username) mount(u); })
    .catch(function () {});
})();
