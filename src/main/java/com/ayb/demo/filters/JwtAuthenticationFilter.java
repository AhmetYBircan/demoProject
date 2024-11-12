package com.ayb.demo.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ayb.demo.services.JwtService;
import com.ayb.demo.services.UserService;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
     throws ServletException, IOException {
       try{
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
               throw new Exception("Authorization Bearer not found");
            }

        String jwt = bearerToken.substring(7);
        Claims claims = jwtService.getTokenClaims(jwt);

            if (claims == null ) {
                throw new Exception ("Token is not valid");
            }
            
            String email = claims.getSubject();
            var userDetails = userService.loadUserByUsername(email);


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,null);

            SecurityContextHolder.getContext().setAuthentication(authentication);
       }catch (Exception e) {
          System.out.println("Error of auth the user: " + e.getMessage());
         }

        filterChain.doFilter(request, response);
    }
}
