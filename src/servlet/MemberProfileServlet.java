package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.MemberProfileDAO;
import library.model.User;

@WebServlet("/member/profile")
public class MemberProfileServlet extends HttpServlet {
    private final MemberProfileDAO dao = new MemberProfileDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            String code = req.getParameter("m");
            req.setAttribute("msg", message(code));
            req.setAttribute("msgOk", "saved".equals(code) || "pwchanged".equals(code));
            req.setAttribute("member", dao.getByUserId(user.getId()));
            req.getRequestDispatcher("/WEB-INF/views/member-profile.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        String code = "bad";
        try {
            if ("details".equals(action)) {
                String fullName = clean(req.getParameter("fullName"));
                String email = clean(req.getParameter("email"));
                String phone = clean(req.getParameter("phone"));
                if (validDetails(fullName, email, phone)) {
                    dao.updateDetails(user.getId(), fullName, email, phone);
                    code = "saved";
                } else {
                    code = "invalid";
                }
            } else if ("password".equals(action)) {
                String current = clean2(req.getParameter("current"));
                String next = clean2(req.getParameter("next"));
                String confirm = clean2(req.getParameter("confirm"));
                if (next.length() < 6 || next.length() > 100) code = "pwshort";
                else if (!next.equals(confirm)) code = "pwmismatch";
                else code = dao.changePassword(user.getId(), current, next) ? "pwchanged" : "wrongpw";
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
        res.sendRedirect(req.getContextPath() + "/member/profile?m=" + code);
    }

    private static boolean validDetails(String fullName, String email, String phone) {
        if (fullName.isEmpty() || fullName.length() > 100) return false;
        if (!email.isEmpty() && (email.length() > 100 || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+"))) return false;
        if (!phone.isEmpty() && !phone.matches("[0-9+\\- ]{5,20}")) return false;
        return true;
    }

    private static String clean(String s) { return s == null ? "" : s.trim(); }

    private static String clean2(String s) { return s == null ? "" : s; }

    private static String message(String code) {
        if (code == null) return null;
        switch (code) {
            case "saved": return "Your details were saved.";
            case "invalid": return "Please check your details: the name is required, and the email and phone must look right.";
            case "pwchanged": return "Your password was changed.";
            case "wrongpw": return "Your current password is wrong.";
            case "pwmismatch": return "The new passwords do not match.";
            case "pwshort": return "The new password must be 6 to 100 characters.";
            default: return "Something went wrong.";
        }
    }
}
