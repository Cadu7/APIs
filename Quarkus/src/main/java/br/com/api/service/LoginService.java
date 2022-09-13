package br.com.api.service;

import br.com.api.domain.interfaces.ILoginService;
import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.model.LoginDto;
import br.com.api.domain.model.User;
import br.com.api.web.exception.ApiError;
import br.com.api.web.exception.InvalidAuthenticationException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Base64;
import java.util.Base64.Decoder;

@ApplicationScoped
public class LoginService implements ILoginService {
    
    private static final String ERROR_MESSAGE = "Autenticação inválida";
    private static final String ERROR_DETAIL = "O usuario ou a senha estão incorretos";
    private static final String ISSUER = ConfigProvider.getConfig().getValue("app.issuer", String.class);
    
    @Inject
    IUserRepository userRepository;
    
    @Override
    public String authenticate(String token) {
        String userName = getUserName(token);
        String password = getPassword(token);
    
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new InvalidAuthenticationException(new ApiError(ERROR_MESSAGE, ERROR_DETAIL)));
        if (!BcryptUtil.matches(password, user.getPassword())) {
            throw new InvalidAuthenticationException(new ApiError(ERROR_MESSAGE, ERROR_DETAIL));
        }
        
        return Jwt
                .upn(user.getUserName())
                .issuer(ISSUER)
                .groups(user.getRoleString())
                .sign();
    }
    
    private String getPassword(String token) {
        return new String(Base64.getDecoder().decode(token.substring(token.indexOf(" ") + 1))).split(":")[1];
    }
    
    private String getUserName(String token) {
        return new String(Base64.getDecoder().decode(token.substring(token.indexOf(" ") + 1))).split(":")[0];
    }
    
}
