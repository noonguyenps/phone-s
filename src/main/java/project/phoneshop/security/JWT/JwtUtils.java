package project.phoneshop.security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import project.phoneshop.security.DTO.AppUserDetail;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${apps.security.secret}")
    private String jwtSecret;

    @Value("${apps.security.header-string}")
    private String headerString;
    @Value("${apps.security.token-prefix}")
    private String tokenPrefix;
    @Value("${apps.security.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${apps.security.header-string}")
    private String authorizationHeader;
    @Value("${apps.security.refreshJwtExpirationMs}")
    private int refreshJwtExpirationMs;
    public String generateJwtToken(AppUserDetail userPrincipal) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        String access_token = JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+ jwtExpirationMs))
                .withClaim("roleNames",userPrincipal.getRoles().stream().collect(Collectors.toList()))
                .withClaim("rolePermissions",userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;

    }
    public Collection<SimpleGrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        String tempRolesString = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody().get("rolePermissions").toString();
        String[] roles =tempRolesString.substring(1,tempRolesString.length()-1).replaceAll(" ","").split(",");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String role: roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }


    public boolean validateJwtToken(String authToken) throws ExpiredJwtException {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(authToken.replace(tokenPrefix, ""))
                    .getBody()
                    .getSubject();
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
    public String getUserNameFromJwtToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return  jwt.getSubject();
    }
    public String getIdFromJwtToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        Claim claim= jwt.getClaim("id");
        return claim.toString().replace("[","").replace("\"","").replace("]","");
    }
    public String generateRefreshJwtToken(AppUserDetail userPrincipal){
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+ refreshJwtExpirationMs))
                .withClaim("roleNames",userPrincipal.getRoles().stream().collect(Collectors.toList()))
                .withClaim("rolePermissions",userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public boolean validateExpiredToken(String authToken) {
        DecodedJWT jwt = JWT.decode(authToken);
        return  jwt.getExpiresAt().before(new Date());
    }
    public String generateEmailJwtToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        String email_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()+ 600000))
                .sign(algorithm);
        return email_token;
    }

    public String generateVerificationEmailJwtToken(String username, UUID id) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        String ids = String.valueOf(id);
        String email_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()+ 600000))
                .withClaim("id", Collections.singletonList(ids))
                .sign(algorithm);
        return email_token;
    }


}
