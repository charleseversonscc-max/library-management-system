<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,library.model.LoanInfo,library.model.PopularBook,library.dao.LoanDAO,library.util.Html" %>
<% String ctx = request.getContextPath();
   List<LoanInfo> overdue = (List<LoanInfo>) request.getAttribute("overdue");
   List<PopularBook> popular = (List<PopularBook>) request.getAttribute("popular"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Reports - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<div id="app-nav"></div>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-3">Reports</h2>
  <h3 class="h5 mb-2">Overdue books</h3>
  <div class="card card-lib mb-4">
    <ul class="list-group list-group-flush">
<% if (overdue.isEmpty()) { %><li class="list-group-item">No overdue books.</li><% } %>
<% for (LoanInfo l : overdue) { %>
      <li class="list-group-item d-flex justify-content-between"><div><strong><%= Html.esc(l.getTitle()) %></strong><br><small><%= Html.esc(l.getMemberName()) %> &middot; due <%= Html.esc(l.getDueDate()) %></small></div>
      <div class="text-end"><span class="badge bg-danger"><%= l.getOverdueDays() %> day(s)</span><br><small>Fine so far: <%= String.format("%.2f", l.getOverdueDays() * LoanDAO.FINE_PER_DAY) %></small></div></li>
<% } %>
    </ul>
  </div>
  <h3 class="h5 mb-2">Most borrowed books</h3>
  <div class="card card-lib mb-4">
    <ul class="list-group list-group-flush">
<% int rank = 1; for (PopularBook p : popular) { %>
      <li class="list-group-item d-flex justify-content-between"><div><strong><%= rank++ %>. <%= Html.esc(p.getTitle()) %></strong><br><small><%= Html.esc(p.getAuthor()) %></small></div>
      <span class="badge bg-success align-self-center"><%= p.getTimes() %> time(s)</span></li>
<% } %>
    </ul>
  </div>
  <a href="<%= ctx %>/pages/admin.html" class="btn btn-lib w-100">Back to the admin panel</a>
</div>
<div id="app-footer"></div>
<script src="<%= ctx %>/js/layout.js"></script>
</body>
</html>
