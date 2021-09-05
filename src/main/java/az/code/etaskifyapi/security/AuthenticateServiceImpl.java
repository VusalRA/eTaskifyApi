package az.code.etaskifyapi.security;

import az.code.etaskifyapi.models.security.AuthToken;
import az.code.etaskifyapi.models.security.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateServiceImpl implements AuthenticateService {

    private TokenProvider jwtTokenUtil;
    private AuthenticationManager authenticationManager;

    public AuthenticateServiceImpl(TokenProvider jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthToken getAuthToken(LoginUser loginUser) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return new AuthToken(token);
    }

}
