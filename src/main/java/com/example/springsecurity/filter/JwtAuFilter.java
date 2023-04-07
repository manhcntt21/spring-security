package com.example.springsecurity.filter;

import com.example.springsecurity.config.CustomUserDetailService;
import com.example.springsecurity.config.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author manhdt14
 * created in 1/5/2023 10:26 PM
 */
@Component
@Slf4j
public class JwtAuFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtService jwtService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    /**
     * validate jwt token with current user
     * every request all pass through this filter
     * thông tin role of account không tham gia trực tiếp vào việc sinh token, nó được lấy từ database sau khi validate token
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String name;
        log.info("jwt au filter");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("validate jwt token with current user");
        jwtToken = authHeader.substring(7);

        try {
            // todo extract the useremail from JWT Token
            name = jwtService.extractUsername(jwtToken);
            if( name != null && (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(name);
                if(jwtService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (MalformedJwtException e) {
            log.error("invalid jwt");
        } catch (ExpiredJwtException e) {
            // handle expired jwt token
            log.info("expired jwt");
        }
        catch (Exception e) {
            log.error("undefined error");
//            e.printStackTrace();
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
