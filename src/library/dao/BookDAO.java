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

    public Book getById(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM books WHERE book_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void add(String isbn, String title, String author, String category, int copies) throws SQLException {
        String sql = "INSERT INTO books (isbn, title, author, category, copies_total, copies_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setString(4, category.isEmpty() ? null : category);
            ps.setInt(5, copies);
            ps.setInt(6, copies);
            ps.executeUpdate();
        }
    }

    public void update(int id, String isbn, String title, String author, String category, int copies) throws SQLException {
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, category = ?, "
                   + "copies_available = GREATEST(0, copies_available + (? - copies_total)), "
                   + "copies_total = ? WHERE book_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.setString(4, category.isEmpty() ? null : category);
            ps.setInt(5, copies);
            ps.setInt(6, copies);
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM books WHERE book_id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
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
                while (rs.next()) books.add(map(rs));
            }
        }
        return books;
    }

    private Book map(ResultSet rs) throws SQLException {
        return new Book(rs.getInt("book_id"), rs.getString("isbn"), rs.getString("title"),
            rs.getString("author"), rs.getString("category"),
            rs.getInt("copies_total"), rs.getInt("copies_available"));
    }
}
