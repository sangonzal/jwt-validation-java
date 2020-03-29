import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

public class SigningKeyResolver extends SigningKeyResolverAdapter {

    private AADKeySet keySet;

    SigningKeyResolver(String authority) throws Exception {
        keySet = getSigningKeys(authority);
    }

    @Override
    public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {

        String tokenKeyId = jwsHeader.getKeyId();
        for(JsonWebKey key: keySet.getKeys()){
            if(key.getKid().equalsIgnoreCase(tokenKeyId)){
                return generatePublicKey(key);
            }
        }

        throw new JwtValidationException("Signature validation failed: Could not find a key with matching kid");
    }

    private AADKeySet getSigningKeys(String authority)throws Exception {
        OpenIdConnectConfiguration openIdConfig = getOpenIdConfiguration(authority);
        return getKeysFromJwkUri(openIdConfig.getJwksUri());
    }

    private OpenIdConnectConfiguration getOpenIdConfiguration(String authority) throws Exception {

        String openIdConnectDiscoveryEndpoint = authority + "/.well-known/openid-configuration";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(openIdConnectDiscoveryEndpoint)
                .build();

        Response response = client.newCall(request).execute();
        String responseJson =  response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseJson, OpenIdConnectConfiguration.class);
    }

    private AADKeySet getKeysFromJwkUri(String jwksUri) throws Exception {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(jwksUri)
                .build();

        Response response = client.newCall(request).execute();
        String responseJson =  response.body().string();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseJson, AADKeySet.class);
    }

    private PublicKey generatePublicKey(JsonWebKey key) {
        try {
            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(key.getN()));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(key.getE()));

            RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(publicSpec);
        } catch(Exception e){
            throw new JwtValidationException("Key generation failed", e);
        }
    }
}
