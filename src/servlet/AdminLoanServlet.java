package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.BookDAO;
import library.dao.LoanDAO;
import library.dao.MemberDAO;

@WebServlet("/admin/loans")
public class AdminLoanServlet extends HttpServlet {
    private final LoanDAO loans = new LoanDAO();
    private final MemberDAO members = new MemberDAO();
    private final BookDAO books = new BookDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String code = req.getParameter("m");
            req.setAttribute("msg", message(code));
            req.setAttribute("msgOk", "issued".equals(code) || "returned".equals(code) || "returnedfine".equals(code));
            req.setAttribute("members", members.getAll());
            req.setAttribute("books", books.getAll());
            req.setAttribute("loans", loans.getActive());
            req.getRequestDispatcher("/WEB-INF/views/admin-loans.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        String code = "bad";
        try {
            if ("issue".equals(action)) {
                int memberId = toInt(req.getParameter("memberId"));
                int bookId = toInt(req.getParameter("bookId"));
                if (memberId > 0 && bookId > 0) code = loans.issue(memberId, bookId);
            } else if ("return".equals(action)) {
                int loanId = toInt(req.getParameter("id"));
                if (loanId > 0) code = loans.giveBack(loanId);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
        res.sendRedirect(req.getContextPath() + "/admin/loans?m=" + code);
    }

    private static int toInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return -1; }
    }

    private static String message(String code) {
        if (code == null) return null;
        switch (code) {
            case "issued": return "Book issued. It is due in 14 days.";
            case "returned": return "Book returned.";
            case "returnedfine": return "Book returned late. A fine was recorded.";
            case "nocopies": return "No copies of this book are available.";
            case "limit": return "This member already has 3 books.";
            case "already": return "This member already borrowed this book.";
            default: return "Something went wrong.";
        }
    }
}
