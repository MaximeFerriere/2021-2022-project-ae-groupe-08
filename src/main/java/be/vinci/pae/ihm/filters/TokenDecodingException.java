package be.vinci.pae.ihm.filters;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class TokenDecodingException extends WebApplicationException {
  public TokenDecodingException() {
    super(Response.status(Response.Status.UNAUTHORIZED)
        .build());
  }

  /**
   * generate a UNAUTHORIZED exception with for cause message the message in parameter.
   * @param message String
   */
  public TokenDecodingException(String message) {
    super(Response.status(Response.Status.UNAUTHORIZED)
        .entity(message)
        .type("text/plain")
        .build());
  }

  /**
   * generate a UNAUTHORIZED exception with for cause
   * message the message of the throwable in parameter.
   * @param cause Throwable
   */
  public TokenDecodingException(Throwable cause) {
    super(Response.status(Response.Status.UNAUTHORIZED)
        .entity(cause.getMessage())
        .type("text/plain")
        .build());
  }
}
