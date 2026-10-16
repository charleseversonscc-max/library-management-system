<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,library.model.*,library.util.Html" %>
<% String ctx = request.getContextPath();
   List<MemberInfo> members = (List<MemberInfo>) request.getAttribute("members");
   List<Book> books = (List<Book>) request.getAttribute("books");
   List<LoanInfo> loans = (List<LoanInfo>) request.getAttribute("loans");
   String msg = (String) request.getAttribute("msg");
   boolean ok = Boolean.TRUE.equals(request.getAttribute("msgOk")); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Issue and return - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<div id="app-nav"></div>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-3">Issue and return</h2>
  <% if (msg != null) { %><div class="alert <%= ok ? "alert-success" : "alert-danger" %>"><%= msg %></div><% } %>
  <div class="card card-lib p-3 mb-4">
    <h3 class="h5 mb-3">Issue a book</h3>
    <form action="<%= ctx %>/admin/loans" method="post">
      <input type="hidden" name="action" value="issue">
      <select class="form-select mb-2" name="memberId" required>
        <option value="">Choose a member</option>
<% for (MemberInfo m : members) { %>
        <option value="<%= m.getId() %>"><%= Html.esc(m.getFullName()) %> (@<%= Html.esc(m.getUsername()) %>)</option>
<% } %>
      </select>
      <select class="form-select mb-3" name="bookId" required>
        <option value="">Choose a book</option>
<% for (Book b : books) { if (b.getCopiesAvailable() > 0) { %>
        <option value="<%= b.getId() %>"><%= Html.esc(b.getTitle()) %> (<%= b.getCopiesAvailable() %> left)</option>
<% } } %>
      </select>
      <button class="btn btn-lib w-100" type="submit">Issue book</button>
    </form>
  </div>
  <h3 class="h5 mb-2">Borrowed books</h3>
  <p class="small text-muted">Fine: 5.00 per overdue day.</p>
  <div class="card card-lib">
    <ul class="list-group list-group-flush">
<% if (loans.isEmpty()) { %><li class="list-group-item">No books are borrowed right now.</li><% } %>
<% for (LoanInfo l : loans) { %>
      <li class="list-group-item"><div class="d-flex justify-content-between"><div><strong><%= Html.esc(l.getTitle()) %></strong><br><small><%= Html.esc(l.getMemberName()) %> &middot; due <%= Html.esc(l.getDueDate()) %></small><br><% if (l.getOverdueDays() > 0) { %><span class="badge bg-danger">Overdue <%= l.getOverdueDays() %> day(s)</span><% } else { %><span class="badge bg-success">On time</span><% } %></div>
      <form action="<%= ctx %>/admin/loans" method="post"><input type="hidden" name="action" value="return"><input type="hidden" name="id" value="<%= l.getId() %>"><button class="btn btn-sm btn-gold" type="submit">Return</button></form></div></li>
<% } %>
    </ul>
  </div>
</div>
<div id="app-footer"></div>
<script src="<%= ctx %>/js/layout.js"></script>
</body>
</html>
