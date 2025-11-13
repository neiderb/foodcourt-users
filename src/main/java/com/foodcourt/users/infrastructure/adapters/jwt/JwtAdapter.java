package com.foodcourt.users.infrastructure.adapters.jwt;

import com.foodcourt.users.domain.exception.InvalidTokenException;
import com.foodcourt.users.domain.gateways.TokenServiceGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserClaims;
import com.foodcourt.users.domain.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import static com.foodcourt.users.domain.constants.ErrorMessage.INVALID_TOKEN;
import static com.foodcourt.users.domain.model.AuthClaim.*;

@Service
public class JwtAdapter implements TokenServiceGateway {
	
	private final Key key;
	private final long expirationTimeInMillis;
	
	public JwtAdapter(
		@Value("${jwt.secret}") String secret,
		@Value("${jwt.expiration}") long expirationTimeInMillis
	) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.expirationTimeInMillis = expirationTimeInMillis;
	}
	
	@Override
	public String generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationTimeInMillis);
		
		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim(USER_ID.value, user.getId())
			.claim(ROLE.value, user.getRole().name())
			.claim(RESTAURANT_ID.value, user.getIdRestaurant())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
	
	@Override
	public UserClaims parseToken(String token) {
		try {
			Claims claims = getClaimsFromToken(token);
			return new UserClaims(
				claims.get(USER_ID.value, Long.class),
				claims.getSubject(),
				UserRole.getRoleof(claims.get(ROLE.value, String.class)),
				claims.get(RESTAURANT_ID.value, Long.class)
			);
		} catch (Exception e) {
			throw new InvalidTokenException(INVALID_TOKEN);
		}
	}
	
	private Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
	
}
