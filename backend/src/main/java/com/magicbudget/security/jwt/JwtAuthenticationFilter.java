package com.magicbudget.security.jwt;


import com.magicbudget.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final  JwtImplementationService jwtImplementationService;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthenticationFilter(JwtImplementationService jwtImplementationService, UserDetailsServiceImpl userDetailsService) {
    this.jwtImplementationService = jwtImplementationService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader("Authorization");

    if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
      filterChain.doFilter(request,response);
      return;
    }
    final String jwtToken = authorizationHeader.substring(7);
    final String username = jwtImplementationService.extractUsername(jwtToken);
    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if(jwtImplementationService.validateToken(jwtToken,userDetails)){
        UsernamePasswordAuthenticationToken authenticationToken = new
            UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }
    }
    filterChain.doFilter(request,response);
  }
}

