package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.MemberDAO;

@WebServlet("/admin/members")
public class AdminMemberServlet extends HttpServlet {
    private final MemberDAO dao = new MemberDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String code = req.getParameter("m");
            req.setAttribute("msg", message(code));
            req.setAttribute("msgOk", "added".equals(code));
            req.setAttribute("members", dao.getAll());
            req.getRequestDispatcher("/WEB-INF/views/admin-members.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = clean(req.getParameter("username"));
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String fullName = clean(req.getParameter("fullName"));
        String email = clean(req.getParameter("email"));
        String phone = clean(req.getParameter("phone"));
        String code;
        if (!valid(username, password, fullName, email, phone)) {
            code = "invalid";
        } else {
            try {
                dao.register(username, password, fullName, email, phone);
                code = "added";
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) code = "dup";
                else throw new ServletException("Database error", e);
            }
        }
        res.sendRedirect(req.getContextPath() + "/admin/members?m=" + code);
    }

    private static boolean valid(String username, String password, String fullName, String email, String phone) {
        if (!username.matches("[A-Za-z0-9_]{3,30}")) return false;
        if (password.length() < 6 || password.length() > 100) return false;
        if (fullName.isEmpty() || fullName.length() > 100) return false;
        if (!email.isEmpty() && (email.length() > 100 || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+"))) return false;
        if (!phone.isEmpty() && !phone.matches("[0-9+\\- ]{5,20}")) return false;
        return true;
    }

    private static String clean(String s) { return s == null ? "" : s.trim(); }

    private static String message(String code) {
        if (code == null) return null;
        switch (code) {
            case "added": return "Member registered.";
            case "dup": return "That username is already taken.";
            case "invalid": return "Please check the fields: username 3 to 30 letters, numbers or _, password at least 6 characters, full name required.";
            default: return "Something went wrong.";
        }
    }
}
