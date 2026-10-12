<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,library.model.Book" %>
<% String ctx = request.getContextPath(); String q = (String) request.getAttribute("q");
   String qs = q.replace("&","&amp;").replace("<","&lt;").replace("\"","&quot;"); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Books - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-lib px-3">
  <span class="navbar-brand mb-0 h1">Library System</span>
  <a href="<%= ctx %>/pages/login.html" class="btn btn-gold btn-sm">Log out</a>
</nav>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-3">Books</h2>
  <form action="<%= ctx %>/books" method="get" class="mb-3">
    <input type="text" name="q" value="<%= qs %>" class="form-control" placeholder="Search title, author or ISBN, then press Enter">
  </form>
  <div class="card card-lib">
    <ul class="list-group list-group-flush">
<%
List<Book> books = (List<Book>) request.getAttribute("books");
if (books.isEmpty()) { %>
      <li class="list-group-item">No books found.</li>
<% }
for (Book b : books) {
  int n = b.getCopiesAvailable();
  String badge = n == 0 ? "bg-danger" : (n == 1 ? "bg-warning text-dark" : "bg-success"); %>
      <li class="list-group-item d-flex justify-content-between align-items-center"><div><strong><%= b.getTitle() %></strong><br><small><%= b.getAuthor() %> &middot; <%= b.getIsbn() %></small></div><span class="badge <%= badge %>"><%= n %> left</span></li>
<% } %>
    </ul>
  </div>
</div>
</body>
</html>
