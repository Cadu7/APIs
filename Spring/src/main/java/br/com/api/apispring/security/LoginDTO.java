package br.com.api.apispring.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	
	private String user;
	private String password;
	
}
