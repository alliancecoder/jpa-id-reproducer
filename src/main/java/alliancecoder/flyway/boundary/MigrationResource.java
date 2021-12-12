package alliancecoder.flyway.boundary;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import alliancecoder.flyway.control.MigrationService;
import alliancecoder.globals.OperationResult;

@RequestScoped
@Path("migrations")
public class MigrationResource {
    
    @Inject
    MigrationService service;

    @GET
    @Path("/version")
    @Produces({MediaType.TEXT_PLAIN})
    public Response getCurrentVersion() {
        return Response.ok(service.checkMigrationVersion()).build();
    }

    @GET
    @Path("/reset")
    public Response resetSampleData() {
        OperationResult result = service.resetSampleData();
        if (result == OperationResult.SUCCESS) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
    }
}
