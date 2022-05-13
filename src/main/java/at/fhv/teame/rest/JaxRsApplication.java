package at.fhv.teame.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(
        title = "OpenAPIDefinition annotation is required only once in a project",
        version = "1",
        description = "OpenAPI annotation example"))
@ApplicationPath("/rest")
public class JaxRsApplication extends Application { }
