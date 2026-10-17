package servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.ReportDAO;

@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {
    private final ReportDAO dao = new ReportDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setAttribute("overdue", dao.overdue());
            req.setAttribute("popular", dao.popular());
            req.getRequestDispatcher("/WEB-INF/views/admin-reports.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
