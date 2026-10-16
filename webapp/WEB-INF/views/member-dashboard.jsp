<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,library.model.LoanInfo,library.util.Html" %>
<% String ctx = request.getContextPath();
   List<LoanInfo> loans = (List<LoanInfo>) request.getAttribute("loans");
   double fines = (Double) request.getAttribute("fines");
   String username = (String) request.getAttribute("username"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>My account - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<div id="app-nav"></div>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-1">My account</h2>
  <p class="text-muted">Signed in as @<%= Html.esc(username) %></p>
  <div class="row g-3 mb-4">
    <div class="col-6"><div class="card card-lib p-3"><small>Borrowed books</small><h2 class="mb-0"><%= loans.size() %></h2></div></div>
    <div class="col-6"><div class="card card-lib p-3"><small>Unpaid fines</small><h2 class="mb-0"><%= String.format("%.2f", fines) %></h2></div></div>
  </div>
  <% if (fines > 0) { %><div class="alert alert-warning">You have unpaid fines. Please settle them at the library desk.</div><% } %>
  <h3 class="h5 mb-2">Due dates</h3>
  <div class="card card-lib mb-4">
    <ul class="list-group list-group-flush">
<% if (loans.isEmpty()) { %><li class="list-group-item">You have no borrowed books.</li><% } %>
<% for (LoanInfo l : loans) { %>
      <li class="list-group-item d-flex justify-content-between"><div><strong><%= Html.esc(l.getTitle()) %></strong><br><small>Borrowed <%= Html.esc(l.getBorrowDate()) %></small></div>
      <% if (l.getOverdueDays() > 0) { %><span class="badge bg-danger align-self-center">Overdue <%= l.getOverdueDays() %> day(s)</span><% } else { %><span class="badge bg-success align-self-center">Due <%= Html.esc(l.getDueDate()) %></span><% } %></li>
<% } %>
    </ul>
  </div>
  <a href="<%= ctx %>/books" class="btn btn-lib w-100">Browse books</a>
</div>
<div id="app-footer"></div>
<script src="<%= ctx %>/js/layout.js"></script>
</body>
</html>
