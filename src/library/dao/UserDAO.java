package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import library.model.Admin;
import library.model.Member;
import library.model.User;
import library.util.DBConnection;
import library.util.PasswordUtil;

public class UserDAO {
    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT user_id, password_hash, role FROM users WHERE username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.verify(password, rs.getString("password_hash"))) {
                    int id = rs.getInt("user_id");
                    return "ADMIN".equals(rs.getString("role")) ? new Admin(id, username) : new Member(id, username);
                }
            }
        }
        return null;
    }
}
