package az.code.etaskifyapi.security;

import az.code.etaskifyapi.models.security.AuthToken;
import az.code.etaskifyapi.models.security.LoginUser;

public interface AuthenticateService {

    AuthToken getAuthToken(LoginUser loginUser);

}
