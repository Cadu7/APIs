package br.com.api.config;

import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.model.User;
import br.com.api.web.exception.AuthException;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.scheduler.Scheduled;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.util.Base64;
import java.util.concurrent.LinkedBlockingQueue;

import static io.quarkus.scheduler.Scheduled.ConcurrentExecution.SKIP;

@Provider
public class SecurityConfig implements ContainerRequestFilter {
    
    @Inject
    IUserRepository userRepository;
    
    private static final LinkedBlockingQueue<User> usersCache = new LinkedBlockingQueue<>();
    
    @Override
    public void filter(ContainerRequestContext context) throws AuthException {
        String path = context.getUriInfo().getPath();
        if ("/login".equals(path)) {
            String token = getTokenHeader(context);
            String user = getUserName(token);
            String password = getPassword(token);
            User userAuthenticate = authenticate(user, password);
            verifyRole(userAuthenticate);
        }
    }
    
    private User authenticate(String userName, String password) throws AuthException {
        User user = usersCache
                .parallelStream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElseGet(() -> {
                    User userDB = userRepository.findByUserName(userName).orElseThrow(() -> new AuthException(401));
                    usersCache.add(userDB);
                    return userDB;
                });
        verifyPassword(password, user.getPassword());
        return user;
    }
    
    private void verifyRole(User user) {
    
    }
    
    private void verifyPassword(String plainText, String passwordHash) {
        if (!BcryptUtil.matches(plainText, passwordHash)) {
            throw new AuthException(401);
        }
    }
    
    @Scheduled(every = "600s", concurrentExecution = SKIP)
    public void clearCache() {
        usersCache.clear();
    }
    
    private String getTokenHeader(ContainerRequestContext context) {
        String fullToken = context.getHeaderString("Authorization");
        return fullToken.substring(fullToken.indexOf(" ") + 1);
    }
    
    private String getPassword(String token) {
        return new String(Base64.getDecoder().decode(token)).split(":")[1];
    }
    
    private String getUserName(String token) {
        return new String(Base64.getDecoder().decode(token)).split(":")[0];
    }
}
