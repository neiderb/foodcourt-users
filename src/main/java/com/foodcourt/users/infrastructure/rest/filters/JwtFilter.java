package com.foodcourt.users.infrastructure.rest.filters;

import com.foodcourt.users.domain.gateways.TokenServiceGateway;
import com.foodcourt.users.domain.model.UserClaims;
import com.foodcourt.users.domain.model.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final TokenServiceGateway tokenServiceGateway;
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String ROLE_PREFIX = "ROLE_";
	
	@Override
	protected void doFilterInternal(
		@NonNull
		HttpServletRequest request,
		@NonNull
		HttpServletResponse response,
		@NonNull
		FilterChain filterChain) throws ServletException, IOException {
		
		String token = extractToken(request);
		
		if(StringUtils.hasText(token)) {
			UserClaims userClaims = tokenServiceGateway.parseToken(token);
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userClaims,
				token,
				mapRoleToAuthorities(userClaims.role())
			);
			
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (nonNull(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}
	
	private List<GrantedAuthority> mapRoleToAuthorities(UserRole role) {
		return List.of(
			new SimpleGrantedAuthority(ROLE_PREFIX.concat(role.name()))
		);
	}
	
}
