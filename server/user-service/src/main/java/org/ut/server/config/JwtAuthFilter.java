package org.ut.server.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.ut.server.dao.UserDao;
import org.ut.server.model.CustomUserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

//    private final UserDetailsService userDetailsService;

    private  HandlerExceptionResolver exceptionResolver;
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    public JwtAuthFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String userId ;
        final String jwtToken;

//        final String jwtToken = getJWTFromRequest(request);
//        if (StringUtils.hasText(jwtToken) && jwtUtils.isTokenValid(jwtToken)) {
//            final UserDetails userDetails = userDao.loadUserByUsername(jwtUtils.extractUsername(jwtToken));
//            final UsernamePasswordAuthenticationToken authToken =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }

        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userId = jwtUtils.extractUserID(jwtToken);


        try {
            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDao.loadUserByUserId(userId);

                if(jwtUtils.isTokenValid(jwtToken, (CustomUserDetails) userDetails)) {
                    UsernamePasswordAuthenticationToken authToken=
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken); // build secuAuth context and set authnToken
                }
            }
            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        final String PREFIX_AUTH = "Bearer ";
    	String bearerToken = request.getHeader(AUTHORIZATION);
    	if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX_AUTH)) {
    		return bearerToken.substring(PREFIX_AUTH.length(), bearerToken.length());
    	}
    	return null;
    }
}
