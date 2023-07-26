package com.komodo.exception.handler;

import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Provider
public class ThrowableExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger log = LoggerFactory.getLogger(ThrowableExceptionHandler.class);

    @Override
    public Response toResponse(Throwable ex) {
        log.error("auth-Service", ex);
        JsonObject result = new JsonObject();
        result.put("message",ex.getMessage());
        result.put("code", 500);
        return Response.serverError().entity(result).build();
    }
}
