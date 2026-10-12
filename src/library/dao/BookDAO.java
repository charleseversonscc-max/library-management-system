package library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import library.model.Book;
import library.util.DBConnection;

public class BookDAO {

    public List<Book> getAll() throws SQLException {
        return query("SELECT * FROM books ORDER BY title", null);
    }

    public List<Book> search(String keyword) throws SQLException {
        return query("SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ? ORDER BY title", keyword);
    }

    private List<Book> query(String sql, String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (keyword != null) {
                String like = "%" + keyword + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(rs.getInt("book_id"), rs.getString("isbn"),
                        rs.getString("title"), rs.getString("author"),
                        rs.getInt("copies_available")));
                }
            }
        }
        return books;
    }
}
