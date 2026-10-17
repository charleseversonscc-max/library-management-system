package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import library.model.LoanInfo;
import library.model.PopularBook;
import library.util.DBConnection;

public class ReportDAO {

    public List<LoanInfo> overdue() throws SQLException {
        String sql = "SELECT l.loan_id, m.full_name, b.title, l.borrow_date, l.due_date, "
                   + "DATEDIFF(CURDATE(), l.due_date) AS overdue "
                   + "FROM loans l JOIN members m ON m.member_id = l.member_id "
                   + "JOIN books b ON b.book_id = l.book_id "
                   + "WHERE l.return_date IS NULL AND l.due_date < CURDATE() ORDER BY overdue DESC";
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

    public List<PopularBook> popular() throws SQLException {
        String sql = "SELECT b.title, b.author, COUNT(l.loan_id) AS times FROM books b "
                   + "LEFT JOIN loans l ON l.book_id = b.book_id "
                   + "GROUP BY b.book_id, b.title, b.author ORDER BY times DESC, b.title LIMIT 10";
        List<PopularBook> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PopularBook(rs.getString("title"), rs.getString("author"), rs.getInt("times")));
            }
        }
        return list;
    }
}
