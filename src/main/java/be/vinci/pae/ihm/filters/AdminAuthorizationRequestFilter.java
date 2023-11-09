package be.vinci.pae.ihm.filters;

import be.vinci.pae.biz.user.UserCaseController;
import be.vinci.pae.biz.user.UserDTO;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Singleton
@Provider
@AuthorizeAdmin

public class AdminAuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final JWTVerifier jwtVerifier =
      JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();

  @Inject
  private UserCaseController myUserCaseController;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String tokenAdmin = requestContext.getHeaderString("Authorization");
    if (tokenAdmin == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
          .entity("A token is needed to access this resource").build());
    } else {
      DecodedJWT decodedToken = null;
      try {
        decodedToken = this.jwtVerifier.verify(tokenAdmin);
      } catch (Exception e) {
        throw new TokenDecodingException(e);
      }
      UserDTO authenticatedAdmin =
          myUserCaseController.getOne(decodedToken.getClaim("user").asInt());
      if (authenticatedAdmin == null
          || !myUserCaseController.checkUserIsAdmin(authenticatedAdmin.getId())) {
        requestContext.abortWith(Response.status(Status.FORBIDDEN)
            .entity("You are forbidden to access this resource").build());
      }

      requestContext.setProperty("user",
          myUserCaseController.getOne(decodedToken.getClaim("user").asInt()));
    }
  }
}
