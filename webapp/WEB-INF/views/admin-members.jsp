<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,library.model.MemberInfo,library.util.Html" %>
<% String ctx = request.getContextPath();
   List<MemberInfo> members = (List<MemberInfo>) request.getAttribute("members");
   String msg = (String) request.getAttribute("msg");
   boolean ok = Boolean.TRUE.equals(request.getAttribute("msgOk")); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Manage members - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-lib px-3">
  <span class="navbar-brand mb-0 h1">Library System</span>
  <a href="<%= ctx %>/pages/admin.html" class="btn btn-gold btn-sm">Back</a>
</nav>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-3">Manage members</h2>
  <% if (msg != null) { %><div class="alert <%= ok ? "alert-success" : "alert-danger" %>"><%= msg %></div><% } %>
  <div class="card card-lib p-3 mb-4">
    <h3 class="h5 mb-3">Register a member</h3>
    <form action="<%= ctx %>/admin/members" method="post" autocomplete="off">
      <div class="mb-2"><input class="form-control" name="username" placeholder="Username" required minlength="3" maxlength="30" pattern="[A-Za-z0-9_]+"></div>
      <div class="mb-2"><input class="form-control" type="password" name="password" placeholder="Password (at least 6 characters)" required minlength="6" maxlength="100" autocomplete="new-password"></div>
      <div class="mb-2"><input class="form-control" name="fullName" placeholder="Full name" required maxlength="100"></div>
      <div class="mb-2"><input class="form-control" type="email" name="email" placeholder="Email (optional)" maxlength="100"></div>
      <div class="mb-3"><input class="form-control" name="phone" placeholder="Phone (optional)" maxlength="20"></div>
      <button class="btn btn-lib w-100" type="submit">Register member</button>
    </form>
  </div>
  <div class="card card-lib">
    <ul class="list-group list-group-flush">
<% for (MemberInfo m : members) { %>
      <li class="list-group-item"><strong><%= Html.esc(m.getFullName()) %></strong><br><small>@<%= Html.esc(m.getUsername()) %> &middot; <%= Html.esc(m.getEmail()) %> &middot; <%= Html.esc(m.getPhone()) %> &middot; joined <%= Html.esc(m.getJoined()) %></small></li>
<% } %>
    </ul>
  </div>
</div>
</body>
</html>
