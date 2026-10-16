package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import library.model.MemberInfo;
import library.util.DBConnection;
import library.util.PasswordUtil;

public class MemberProfileDAO {

    public MemberInfo getByUserId(int userId) throws SQLException {
        String sql = "SELECT m.member_id, u.username, m.full_name, m.email, m.phone, m.joined_date "
                   + "FROM members m JOIN users u ON u.user_id = m.user_id WHERE u.user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new MemberInfo(rs.getInt("member_id"), rs.getString("username"),
                    rs.getString("full_name"), rs.getString("email"),
                    rs.getString("phone"), rs.getString("joined_date"));
            }
        }
    }

    public void updateDetails(int userId, String fullName, String email, String phone) throws SQLException {
        String sql = "UPDATE members SET full_name = ?, email = ?, phone = ? WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, email.isEmpty() ? null : email);
            ps.setString(3, phone.isEmpty() ? null : phone);
            ps.setInt(4, userId);
            ps.executeUpdate();
        }
    }

    public boolean changePassword(int userId, String current, String next) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            String stored;
            try (PreparedStatement ps = c.prepareStatement("SELECT password_hash FROM users WHERE user_id = ?")) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                    stored = rs.getString(1);
                }
            }
            if (!PasswordUtil.verify(current, stored)) return false;
            try (PreparedStatement ps = c.prepareStatement("UPDATE users SET password_hash = ? WHERE user_id = ?")) {
                ps.setString(1, PasswordUtil.hash(next));
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
            return true;
        }
    }
}
