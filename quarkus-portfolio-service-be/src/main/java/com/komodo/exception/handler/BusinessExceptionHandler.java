package com.komodo.exception.handler;

import com.komodo.exception.BusinessException;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class BusinessExceptionHandler implements ExceptionMapper<BusinessException> {

    private static final Logger log = LoggerFactory.getLogger(BusinessExceptionHandler.class);
    @Override
    public Response toResponse(BusinessException exception) {
        log.error("Something went wrong : {} ", exception);
        JsonObject entity = new JsonObject();
        entity.put("message", exception.getMessage());
        return Response.status(Response.Status.CONFLICT)
                .entity(entity)
                .build();
    }
}
