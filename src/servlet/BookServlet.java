package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.BookDAO;
import library.model.Book;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private final BookDAO dao = new BookDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String q = req.getParameter("q");
        q = (q == null) ? "" : q.trim();
        try {
            List<Book> books = q.isEmpty() ? dao.getAll() : dao.search(q);
            req.setAttribute("books", books);
            req.setAttribute("q", q);
            req.getRequestDispatcher("/pages/books.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
