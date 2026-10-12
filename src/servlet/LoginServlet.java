package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import library.dao.UserDAO;
import library.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.sendRedirect(req.getContextPath() + "/pages/login.html");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            User user = (username == null || password == null) ? null : dao.authenticate(username.trim(), password);
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/pages/login.html?error=1");
                return;
            }
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            req.getSession(true).setAttribute("user", user);
            res.sendRedirect(req.getContextPath() + user.getHomePage());
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
