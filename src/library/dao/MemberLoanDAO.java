package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import library.model.LoanInfo;
import library.util.DBConnection;

public class MemberLoanDAO {

    public int findMemberId(int userId) throws SQLException {
        String sql = "SELECT member_id FROM members WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public double unpaidFines(int memberId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(f.amount), 0) FROM fines f "
                   + "JOIN loans l ON l.loan_id = f.loan_id WHERE l.member_id = ? AND f.paid = FALSE";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        }
    }

    public List<LoanInfo> getActive(int memberId) throws SQLException {
        String sql = "SELECT l.loan_id, m.full_name, b.title, l.borrow_date, l.due_date, "
                   + "GREATEST(0, DATEDIFF(CURDATE(), l.due_date)) AS overdue "
                   + "FROM loans l JOIN members m ON m.member_id = l.member_id "
                   + "JOIN books b ON b.book_id = l.book_id "
                   + "WHERE l.member_id = ? AND l.return_date IS NULL ORDER BY l.due_date";
        List<LoanInfo> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new LoanInfo(rs.getInt("loan_id"), rs.getString("full_name"), rs.getString("title"),
                        rs.getString("borrow_date"), rs.getString("due_date"), rs.getInt("overdue")));
                }
            }
        }
        return list;
    }
}
