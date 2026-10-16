<%@ page contentType="text/html;charset=UTF-8" import="library.model.MemberInfo,library.util.Html" %>
<% String ctx = request.getContextPath();
   MemberInfo m = (MemberInfo) request.getAttribute("member");
   String msg = (String) request.getAttribute("msg");
   boolean ok = Boolean.TRUE.equals(request.getAttribute("msgOk")); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My profile - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<div id="app-nav"></div>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-3">My profile</h2>
  <% if (msg != null) { %><div class="alert <%= ok ? "alert-success" : "alert-danger" %>"><%= msg %></div><% } %>
<% if (m == null) { %>
  <div class="alert alert-warning">No member profile was found for this account.</div>
<% } else { %>
  <div class="card card-lib p-3 mb-4">
    <h3 class="h5 mb-3">My details</h3>
    <p class="small text-muted">Username: @<%= Html.esc(m.getUsername()) %> &middot; Joined <%= Html.esc(m.getJoined()) %></p>
    <form action="<%= ctx %>/member/profile" method="post">
      <input type="hidden" name="action" value="details">
      <div class="mb-2"><input class="form-control" name="fullName" placeholder="Full name" required maxlength="100" value="<%= Html.esc(m.getFullName()) %>"></div>
      <div class="mb-2"><input class="form-control" type="email" name="email" placeholder="Email (optional)" maxlength="100" value="<%= Html.esc(m.getEmail()) %>"></div>
      <div class="mb-3"><input class="form-control" name="phone" placeholder="Phone (optional)" maxlength="20" value="<%= Html.esc(m.getPhone()) %>"></div>
      <button class="btn btn-lib w-100" type="submit">Save details</button>
    </form>
  </div>
  <div class="card card-lib p-3">
    <h3 class="h5 mb-3">Change password</h3>
    <form action="<%= ctx %>/member/profile" method="post" autocomplete="off">
      <input type="hidden" name="action" value="password">
      <div class="mb-2"><input class="form-control" type="password" name="current" placeholder="Current password" required maxlength="100" autocomplete="current-password"></div>
      <div class="mb-2"><input class="form-control" type="password" name="next" placeholder="New password (at least 6 characters)" required minlength="6" maxlength="100" autocomplete="new-password"></div>
      <div class="mb-3"><input class="form-control" type="password" name="confirm" placeholder="Repeat the new password" required minlength="6" maxlength="100" autocomplete="new-password"></div>
      <button class="btn btn-lib w-100" type="submit">Change password</button>
    </form>
  </div>
<% } %>
</div>
<div id="app-footer"></div>
<script src="<%= ctx %>/js/layout.js"></script>
</body>
</html>
