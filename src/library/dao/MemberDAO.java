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
                int userId = insertUser(c, username, password);
                insertMember(c, userId, fullName, email, phone);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private int insertUser(Connection c, String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'MEMBER')";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hash(password));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    private void insertMember(Connection c, int userId, String fullName, String email, String phone) throws SQLException {
        String sql = "INSERT INTO members (user_id, full_name, email, phone, joined_date) VALUES (?, ?, ?, ?, CURDATE())";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, email.isEmpty() ? null : email);
            ps.setString(4, phone.isEmpty() ? null : phone);
            ps.executeUpdate();
        }
    }
}
