import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureException;

import java.util.Collections;
import java.util.Set;

public class Validator {

    // Parameters for acquiring a token using MSAL4J
    private final static String CLIENT_ID = "";
    private final static String AUTHORITY = "";
    private final static Set<String> SCOPE = Collections.singleton("");
    private final static String USER_NAME = "";
    private final static String USER_PASSWORD = "";

    // Parameters for validating token
    private final static String API_APP_ID = "";
    private final static String ISSUER = "";
    private final static String SCOPE_VALIDATE = "";

    public static void main(String args[]) throws Exception {

        IAuthenticationResult result = acquireTokenUsernamePassword();
        Jws<Claims> claims = validateAccessToken(result.accessToken());

        System.out.println("Validated! Got a token with the following claims: ");
        System.out.println("Issuer: " + claims.getBody().getIssuer());
        System.out.println("Audience: " + claims.getBody().getAudience());
        System.out.println("Issued at: " + claims.getBody().getIssuedAt()) ;
        System.out.println("Expires on: " + claims.getBody().getExpiration());
    }

    private static Jws<Claims> validateAccessToken(String accessToken) throws Exception {

        SigningKeyResolver signingKeyResolver = new SigningKeyResolver(AUTHORITY);
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKeyResolver(signingKeyResolver)
                    .requireAudience(API_APP_ID)
                    .requireIssuer(ISSUER)
                    .require("scp", SCOPE_VALIDATE)
                    .parseClaimsJws(accessToken);

        } catch(SignatureException ex) {
            throw new JwtValidationException("Jwt validation failed: invalid signature", ex);
        } catch(ExpiredJwtException ex) {
            throw new JwtValidationException("Jwt validation failed: access token us expired", ex);
        } catch(MissingClaimException ex) {
            throw new JwtValidationException("Jwt validation failed: missing required claim", ex);
        } catch(IncorrectClaimException ex) {
            throw new JwtValidationException("Jwt validation failed: required claim has incorrect value", ex);
        }

        return claims;
    }

    private static IAuthenticationResult acquireTokenUsernamePassword() throws Exception {

        PublicClientApplication pca = PublicClientApplication.builder(CLIENT_ID)
                .authority(AUTHORITY)
                .build();

        UserNamePasswordParameters parameters =
                UserNamePasswordParameters
                        .builder(SCOPE, USER_NAME, USER_PASSWORD.toCharArray())
                        .build();

        return pca.acquireToken(parameters).join();
    }
}
