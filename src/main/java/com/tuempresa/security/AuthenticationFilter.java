package com.tuempresa.security;


import com.tuempresa.dao.UserDAO;
import com.tuempresa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDAO userDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        
        if (HttpMethod.POST.matches(method) &&
                (path.equals("/trainee/create-trainee") || path.equals("/trainer/create-trainer"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = request.getHeader("username");
        String password = request.getHeader("password");

        if (username == null || password == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Credentials");
            return;
        }

        Optional<User> user = userDAO.findByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Credentials");
            return;
        }

        
        filterChain.doFilter(request, response);
    }
}