package com.api.config;

import static com.api.config.JwtTokenUtil.HEADER_STRING;
import static com.api.config.JwtTokenUtil.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.model.JwtUserDetails;
import com.api.model.User;
import com.api.repositories.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	
	@Autowired
	private JwtTokenUtil util;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String header = request.getHeader(HEADER_STRING);
		String token = null;
		String username = null;
		util = new JwtTokenUtil();

		if (header != null && header.startsWith(TOKEN_PREFIX)) {
            token = header.replace(TOKEN_PREFIX,"");
            try {
                username = util.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.error("The token is not in a valid form", e);
            } catch (ExpiredJwtException e) {
                logger.warn("The token is expired and not valid anymore", e);
            } catch(SignatureException e){
                logger.error("Authentication Failed. Wrong signature");
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			JwtUserDetails jwtUserDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			if (util.validateToken(token, jwtUserDetails)) {
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
			
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
        }
		filterChain.doFilter(request, response);
	}

}
