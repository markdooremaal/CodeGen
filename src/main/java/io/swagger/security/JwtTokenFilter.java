package io.swagger.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        System.out.println("JwtTokenFIlter(): " + token);
        try{
            if(token != null && jwtTokenProvider.validateToken(token)){
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch(ResponseStatusException ex){
            SecurityContextHolder.clearContext(); //Logout user for this request
            //httpServletResponse.sendError(ex.getRawStatusCode(), ex.getMessage()); //TODO: Show error to the client
            httpServletResponse.sendError(201, ex.getMessage()); //Show error to the client

            //Return here so we dont go down the filter chain
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
