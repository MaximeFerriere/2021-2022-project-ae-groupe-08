package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;

@Provider
public class WebExceptionMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable exception) {
    exception.printStackTrace();
    MyLogger.addLogger(exception.getMessage(), Level.WARNING);
    if (exception instanceof WebApplicationException) {
      return ((WebApplicationException) exception).getResponse();
      // the response is already prepared
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}
