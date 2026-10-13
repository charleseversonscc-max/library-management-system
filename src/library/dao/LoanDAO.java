package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import library.model.LoanInfo;
import library.util.DBConnection;

public class LoanDAO {
    public static final int MAX_LOANS = 3;
    public static final int LOAN_DAYS = 14;
    public static final double FINE_PER_DAY = 5.00;

    public List<LoanInfo> getActive() throws SQLException {
        String sql = "SELECT l.loan_id, m.full_name, b.title, l.borrow_date, l.due_date, "
                   + "GREATEST(0, DATEDIFF(CURDATE(), l.due_date)) AS overdue "
                   + "FROM loans l JOIN members m ON m.member_id = l.member_id "
                   + "JOIN books b ON b.book_id = l.book_id "
                   + "WHERE l.return_date IS NULL ORDER BY l.due_date";
        List<LoanInfo> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new LoanInfo(rs.getInt("loan_id"), rs.getString("full_name"), rs.getString("title"),
                    rs.getString("borrow_date"), rs.getString("due_date"), rs.getInt("overdue")));
            }
        }
        return list;
    }

    public String issue(int memberId, int bookId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                String result = doIssue(c, memberId, bookId);
                if ("issued".equals(result)) c.commit(); else c.rollback();
                return result;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private String doIssue(Connection c, int memberId, int bookId) throws SQLException {
        int copies = queryInt(c, "SELECT copies_available FROM books WHERE book_id = ? FOR UPDATE", -1, bookId);
        if (copies < 0) return "bad";
        if (copies == 0) return "nocopies";
        if (queryInt(c, "SELECT COUNT(*) FROM members WHERE member_id = ?", 0, memberId) == 0) return "bad";
        if (queryInt(c, "SELECT COUNT(*) FROM loans WHERE member_id = ? AND return_date IS NULL", 0, memberId) >= MAX_LOANS) return "limit";
        if (queryInt(c, "SELECT COUNT(*) FROM loans WHERE member_id = ? AND book_id = ? AND return_date IS NULL", 0, memberId, bookId) > 0) return "already";
        String ins = "INSERT INTO loans (book_id, member_id, borrow_date, due_date) "
                   + "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL " + LOAN_DAYS + " DAY))";
        try (PreparedStatement ps = c.prepareStatement(ins)) {
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = c.prepareStatement("UPDATE books SET copies_available = copies_available - 1 WHERE book_id = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
        return "issued";
    }

    public String giveBack(int loanId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                String result = doReturn(c, loanId);
                if ("bad".equals(result)) c.rollback(); else c.commit();
                return result;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    private String doReturn(Connection c, int loanId) throws SQLException {
        int bookId;
        int overdue;
        String sql = "SELECT book_id, GREATEST(0, DATEDIFF(CURDATE(), due_date)) AS overdue "
                   + "FROM loans WHERE loan_id = ? AND return_date IS NULL FOR UPDATE";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return "bad";
                bookId = rs.getInt("book_id");
                overdue = rs.getInt("overdue");
            }
        }
        try (PreparedStatement ps = c.prepareStatement("UPDATE loans SET return_date = CURDATE() WHERE loan_id = ?")) {
            ps.setInt(1, loanId);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = c.prepareStatement("UPDATE books SET copies_available = copies_available + 1 WHERE book_id = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
        if (overdue > 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO fines (loan_id, amount) VALUES (?, ?)")) {
                ps.setInt(1, loanId);
                ps.setDouble(2, overdue * FINE_PER_DAY);
                ps.executeUpdate();
            }
        }
        return overdue > 0 ? "returnedfine" : "returned";
    }

    private int queryInt(Connection c, String sql, int fallback, int... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setInt(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : fallback;
            }
        }
    }
}
