package utils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionUtils {
    /**
     * Retrieves the user's role from the session.
     * If the session is invalid or the role is missing, redirects to the login page.
     *
     * @param request  The HTTP servlet request.
     * @param response The HTTP servlet response.
     * @return The user's role as a string, or null if the user is redirected.
     * @throws IOException If an I/O error occurs during redirection.
     */
    String role;
    public static String getUserRoleOrRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("index.jsp");
            return null;
        }

        UserRole userRole = (UserRole) session.getAttribute("role");
        if (userRole == null) {
            response.sendRedirect("index.jsp");
            return null;
        }

        return userRole.name(); // Return the role as a string
    }

    public static void conditionalRouting(HttpServletRequest request, HttpServletResponse response,String role) throws ServletException, IOException {
        if ("LIBRARIAN".equals(role)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("librarian-dashboard.jsp");
            dispatcher.forward(request, response);
        } else if ("ADMIN".equals(role)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("admin-dashboard.jsp");
            dispatcher.forward(request, response);
        } else {
            response.getWriter().println("Unauthorized access.");
        }
    }
}
