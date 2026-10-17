package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import library.util.DBConnection;

public class StatsDAO {

    private double number(String sql) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public String toJson() throws SQLException {
        int titles = (int) number("SELECT COUNT(*) FROM books");
        int copies = (int) number("SELECT COALESCE(SUM(copies_available), 0) FROM books");
        int members = (int) number("SELECT COUNT(*) FROM members");
        int borrowed = (int) number("SELECT COUNT(*) FROM loans WHERE return_date IS NULL");
        int overdue = (int) number("SELECT COUNT(*) FROM loans WHERE return_date IS NULL AND due_date < CURDATE()");
        double fines = number("SELECT COALESCE(SUM(amount), 0) FROM fines WHERE paid = FALSE");
        return "{\"titles\":" + titles + ",\"copies\":" + copies + ",\"members\":" + members
             + ",\"borrowed\":" + borrowed + ",\"overdue\":" + overdue
             + ",\"fines\":\"" + String.format(Locale.US, "%.2f", fines) + "\"}";
    }
}
