package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.BookDAO;

@WebServlet("/admin/books")
public class AdminBookServlet extends HttpServlet {
    private final BookDAO dao = new BookDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            int editId = toInt(req.getParameter("edit"));
            if (editId > 0) req.setAttribute("editBook", dao.getById(editId));
            String code = req.getParameter("m");
            req.setAttribute("msg", message(code));
            req.setAttribute("msgOk", "added".equals(code) || "updated".equals(code) || "deleted".equals(code));
            req.setAttribute("books", dao.getAll());
            req.getRequestDispatcher("/WEB-INF/views/admin-books.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String code = "bad";
        try {
            if ("delete".equals(action)) {
                code = dao.delete(toInt(req.getParameter("id"))) ? "deleted" : "bad";
            } else if ("add".equals(action) || "update".equals(action)) {
                String isbn = clean(req.getParameter("isbn"));
                String title = clean(req.getParameter("title"));
                String author = clean(req.getParameter("author"));
                String category = clean(req.getParameter("category"));
                int copies = toInt(req.getParameter("copies"));
                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || copies < 0 || copies > 1000) {
                    code = "invalid";
                } else if ("add".equals(action)) {
                    dao.add(isbn, title, author, category, copies);
                    code = "added";
                } else {
                    dao.update(toInt(req.getParameter("id")), isbn, title, author, category, copies);
                    code = "updated";
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) code = "dup";
            else if (e.getErrorCode() == 1451) code = "inuse";
            else throw new ServletException("Database error", e);
        }
        res.sendRedirect(req.getContextPath() + "/admin/books?m=" + code);
    }

    private static String clean(String s) { return s == null ? "" : s.trim(); }

    private static int toInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return -1; }
    }

    private static String message(String code) {
        if (code == null) return null;
        switch (code) {
            case "added": return "Book added.";
            case "updated": return "Book updated.";
            case "deleted": return "Book deleted.";
            case "dup": return "That ISBN already exists.";
            case "inuse": return "This book has loan records, so it cannot be deleted.";
            case "invalid": return "Please fill in the fields correctly (copies 0 to 1000).";
            default: return "Something went wrong.";
        }
    }
}
