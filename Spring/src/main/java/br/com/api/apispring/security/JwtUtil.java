package br.com.api.apispring.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
	
	@Value("${auth.jwt-secret}")
	private String jwtSecret;
	
	@Value("${auth.jwt-expiration-milliseg}")
	private Long jwtExpirationMilliseg;
	
	@Autowired
	private UserDetailService userDetailService;

	public String generateToken(String username) {
		UserDetails user = userDetailService.loadUserByUsername(username);
		String role = user.getAuthorities().stream().findFirst().get().getAuthority();
		return Jwts.builder().setSubject(username)
				.claim("role", role)
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMilliseg))
				.signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
				.compact();
	}

	public boolean isValidToken(String token) {
		Claims claims = getClaims(token);
		if (claims == null)
			return false;
		String userName = claims.getSubject();
		Date expirationDate = claims.getExpiration();
		Date now = new Date(System.currentTimeMillis());
		if (userName != null && now.before(expirationDate)) {
			return true;
		}
		return false;

	}

	public String getUserName(String token) {
		Claims claims = getClaims(token);
		if (claims!=null) {
			return claims.getSubject();
		}
		return null;
	}

	private Claims getClaims(String token) {

		try {
			Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
			return claims;
		} catch (Exception e) {
			return null;
		}

	}

}
