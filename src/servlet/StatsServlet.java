package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.StatsDAO;

@WebServlet("/admin/stats")
public class StatsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            res.setContentType("application/json;charset=UTF-8");
            res.setHeader("Cache-Control", "no-store");
            res.getWriter().write(new StatsDAO().toJson());
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
