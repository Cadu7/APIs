package br.com.api.apispring.security;

import br.com.api.apispring.web.exception.ApiError;
import br.com.api.apispring.web.exception.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		super();
		this.authenticationManager=authenticationManager;
		this.jwtUtil=jwtUtil;
		setFilterProcessesUrl("/api/login");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			LoginDTO login = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getUser(), login.getPassword(), new ArrayList<>());
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new AuthException(new ApiError("Falha ao autenticar usuario","Não foi possível autenticar o usuario informado"));
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String email = ((UserDetail)authResult.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(email);
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
	}

}
