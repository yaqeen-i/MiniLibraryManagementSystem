package utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")// Apply the filetr to all pages
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String uri = httpRequest.getRequestURI();

        // Public pages (accessible without login)
        boolean isLoginPage = uri.endsWith("index.jsp") || uri.endsWith("login-servlet");
        boolean isRegisterPage = uri.endsWith("register-servlet") || uri.endsWith("register.jsp");
        // Allow static resources like CSS, images, etc.
        boolean isStaticResource = uri.contains("/Styles/") || uri.contains("/js/") || uri.contains("/resources/");

        if (isLoginPage || isStaticResource|| isRegisterPage) {
            chain.doFilter(request, response); // Allow access
            return;
        }

        // here i check session and role
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect("index.jsp"); // Redirect to my landing page if not authenticated
            return;
        }

        UserRole role = (UserRole) session.getAttribute("role"); // Cast it to enum so i can compare

        // i restrict access based on role
        if (role == null) {
            httpResponse.sendRedirect("index.jsp");
            return;
        }

        if (uri.endsWith("admin-dashboard.jsp") && role != UserRole.ADMIN) {
            httpResponse.sendRedirect("unauthorized.jsp");
            return;
        }
        if (uri.endsWith("patron-dashboard.jsp") && role != UserRole.PATRON) {
            httpResponse.sendRedirect("unauthorized.jsp");
            return;
        }
        if (uri.endsWith("librarian-dashboard.jsp") && role != UserRole.LIBRARIAN) {
            httpResponse.sendRedirect("unauthorized.jsp");
            return;
        }

        chain.doFilter(request, response); // Allow access i f authenticated
    }
}
