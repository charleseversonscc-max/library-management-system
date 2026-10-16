package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import library.dao.MemberLoanDAO;
import library.model.LoanInfo;
import library.model.User;

@WebServlet("/member/dashboard")
public class MemberDashboardServlet extends HttpServlet {
    private final MemberLoanDAO dao = new MemberLoanDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            int memberId = dao.findMemberId(user.getId());
            List<LoanInfo> loans = memberId < 0 ? new ArrayList<LoanInfo>() : dao.getActive(memberId);
            double fines = memberId < 0 ? 0 : dao.unpaidFines(memberId);
            req.setAttribute("loans", loans);
            req.setAttribute("fines", fines);
            req.setAttribute("username", user.getUsername());
            req.getRequestDispatcher("/WEB-INF/views/member-dashboard.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
