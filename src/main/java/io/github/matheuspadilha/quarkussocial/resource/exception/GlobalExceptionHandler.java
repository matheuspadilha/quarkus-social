package io.github.matheuspadilha.quarkussocial.resource.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        return mapExceptionToResponse(exception);
    }

    private Response mapExceptionToResponse(Exception exception) {
        Response response = Response.serverError().entity("Internal Server Error").build();

        if ((exception instanceof UserNotFoundException)) {
            response = Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
        } else if ((exception instanceof FollowerAndUserConflictException)) {
            response = Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
        } else if ((exception instanceof PostForbiddenException)) {
            response = Response.status(Response.Status.FORBIDDEN).entity(exception.getMessage()).build();
        } else if ((exception instanceof PostBadRequestException)) {
            response = Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
        }

        return response;
    }
}
