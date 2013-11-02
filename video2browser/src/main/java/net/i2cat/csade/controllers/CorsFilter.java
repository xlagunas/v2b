package net.i2cat.csade.controllers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class CorsFilter extends OncePerRequestFilter {
	private final Logger log = LoggerFactory.getLogger(CorsFilter.class); 
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	log.info("Interceptor activat");
    	response.addHeader("Access-Control-Allow-Origin", "*");        
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())); {
            // CORS "pre-flight" request
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");        
            response.addHeader("Access-Control-Max-Age", "1728000");
        }
        filterChain.doFilter(request, response);
    }
}
