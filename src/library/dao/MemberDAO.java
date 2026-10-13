package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import library.model.MemberInfo;
import library.util.DBConnection;
import library.util.PasswordUtil;

public class MemberDAO {

    public List<MemberInfo> getAll() throws SQLException {
        String sql = "SELECT m.member_id, u.username, m.full_name, m.email, m.phone, m.joined_date "
                   + "FROM members m JOIN users u ON u.user_id = m.user_id ORDER BY m.full_name";
        List<MemberInfo> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new MemberInfo(rs.getInt("member_id"), rs.getString("username"),
                    rs.getString("full_name"), rs.getString("email"),
                    rs.getString("phone"), rs.getString("joined_date")));
            }
        }
        return list;
    }

    public void register(String username, String password, String fullName, String email, String phone) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int userId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO users (username, password_hash, role) VA

cat > src/servlet/AdminMemberServlet.java << 'EOF'
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
        if (!username.matches("[A-Za-z0-9_]{3,30}")
                || password.length() < 6 || password.length() > 100
                || fullName.isEmpty() || fullName.length() > 100
                || (!email.isEmpty() && (email.length() > 100 || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")))
                || (!phone.isEmpty() && !phone.matches("[0-9+\\- ]{5,20}"))) {
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
