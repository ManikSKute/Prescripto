package prescripto.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {

        // Retrieve headers based on client type
        String token = request.getHeader("token");
        if (token == null) {
            token = request.getHeader("aToken");
        }
        if (token == null) {
            token = request.getHeader("atoken");
        }
        if (token == null) {
            token = request.getHeader("dToken");
        }
        if (token == null) {
            token = request.getHeader("dtoken");
        }

        if (token != null && jwtUtils.validateToken(token)) {
            String subject = jwtUtils.extractSubject(token);
            String role = jwtUtils.extractRole(token);
            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Attach extracted ID/Email as context identity
                request.setAttribute("authenticatedSubject", subject);

                List<GrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        subject, null, authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}