package com.komodo.exception.handler;

import com.komodo.exception.ValidationException;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        JsonObject result = new JsonObject();
        result.put("message", exception.getMessage());
        result.put("code", "400");
        return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
    }
}
