<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,library.model.Book,library.util.Html" %>
<% String ctx = request.getContextPath();
   Book e = (Book) request.getAttribute("editBook");
   List<Book> books = (List<Book>) request.getAttribute("books");
   String msg = (String) request.getAttribute("msg");
   boolean ok = Boolean.TRUE.equals(request.getAttribute("msgOk")); %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Manage books - Library System</title>
<link href="<%= ctx %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%= ctx %>/css/style.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-lib px-3">
  <span class="navbar-brand mb-0 h1">Library System</span>
  <a href="<%= ctx %>/pages/admin.html" class="btn btn-gold btn-sm">Back</a>
</nav>
<div class="container py-4" style="max-width:720px">
  <h2 class="mb-3">Manage books</h2>
  <% if (msg != null) { %><div class="alert <%= ok ? "alert-success" : "alert-danger" %>"><%= msg %></div><% } %>
  <div class="card card-lib p-3 mb-4">
    <h3 class="h5 mb-3"><%= e == null ? "Add a book" : "Edit book" %></h3>
    <form action="<%= ctx %>/admin/books" method="post">
      <input type="hidden" name="action" value="<%= e == null ? "add" : "update" %>">
      <% if (e != null) { %><input type="hidden" name="id" value="<%= e.getId() %>"><% } %>
      <div class="mb-2"><input class="form-control" name="isbn" placeholder="ISBN" required maxlength="20" value="<%= e == null ? "" : Html.esc(e.getIsbn()) %>"></div>
      <div class="mb-2"><input class="form-control" name="title" placeholder="Title" required maxlength="200" value="<%= e == null ? "" : Html.esc(e.getTitle()) %>"></div>
      <div class="mb-2"><input class="form-control" name="author" placeholder="Author" required maxlength="100" value="<%= e == null ? "" : Html.esc(e.getAuthor()) %>"></div>
      <div class="mb-2"><input class="form-control" name="category" placeholder="Category (optional)" maxlength="50" value="<%= e == null ? "" : Html.esc(e.getCategory()) %>"></div>
      <div class="mb-3"><input class="form-control" type="number" name="copies" placeholder="Total copies" required min="0" max="1000" value="<%= e == null ? "" : e.getCopiesTotal() %>"></div>
      <button class="btn btn-lib w-100" type="submit"><%= e == null ? "Add book" : "Save changes" %></button>
      <% if (e != null) { %><a href="<%= ctx %>/admin/books" class="btn btn-outline-secondary w-100 mt-2">Cancel</a><% } %>
    </form>
  </div>
  <div class="card card-lib">
    <ul class="list-group list-group-flush">
<% for (Book b : books) { %>
      <li class="list-group-item"><div class="d-flex justify-content-between"><div><strong><%= Html.esc(b.getTitle()) %></strong><br><small><%= Html.esc(b.getAuthor()) %> &middot; <%= Html.esc(b.getIsbn()) %> &middot; <%= b.getCopiesAvailable() %>/<%= b.getCopiesTotal() %> available</small></div>
      <div class="d-flex gap-1 align-items-start"><a class="btn btn-sm btn-gold" href="<%= ctx %>/admin/books?edit=<%= b.getId() %>">Edit</a>
      <form action="<%= ctx %>/admin/books" method="post" onsubmit="return confirm('Delete this book?');"><input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="<%= b.getId() %>"><button class="btn btn-sm btn-outline-danger" type="submit">Delete</button></form></div></div></li>
<% } %>
    </ul>
  </div>
</div>
</body>
</html>
