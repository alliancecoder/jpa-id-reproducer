package alliancecoder.uuids.boundary;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import alliancecoder.exceptions.EntityNotValidException;
import alliancecoder.exceptions.UniqueConstraintException;
import alliancecoder.globals.OperationResult;
import alliancecoder.uuids.entity.EntityUsingUuid;

@RequestScoped
@Path("uuids")
public class UuidEntityResource {
    
    private static final Logger LOGGER = Logger.getLogger(UuidEntityResource.class.getName());

    @Inject
    UuidEntityManager manager;

	// POST should return 201 with Location URI in header
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(EntityUsingUuid uuidEntity) {
		if (!uuidEntity.isValid()) {
			throw new EntityNotValidException(
					"The Entity is not properly formatted. Please refer to the API documentation.");
		}
		try {
			EntityUsingUuid saved = this.manager.save(uuidEntity);
			UUID id = saved.getUuidAsId();
			URI uri = URI.create("/uuids/" + id);
			return Response.created(uri).build();
		} catch (Exception ex) {
			if (ex.getCause().getCause().toString().contains("Constraint")) {
				throw new UniqueConstraintException(
						"An Entity with this unique data already exists.");
			}
			LOGGER.log(Level.DEBUG, ex.toString(), ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.header("error", "An unknown error has occured, please contact support.").build();
		}
	}

    // @POST
	// @Path("/build")
    // public Response build() {
    //     try {
    //         OperationResult result = this.manager.buildSampleData();
    //         if (result == OperationResult.SUCCESS) {
	// 		return Response.ok().build();
    //     } else {
	// 		return Response.status(Response.Status.BAD_REQUEST).build();
    //     }
    //     } catch (Exception ex) {
	// 		LOGGER.log(Level.DEBUG, ex.toString(), ex);
	// 		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	// 				.header("error", "An unknown error has occured, please contact support.").build();
    //     }
    // }

	@GET
	@Path("{uuid}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response find(@PathParam("uuid") String uuid) {
		EntityUsingUuid uuidEntity = this.manager.findById(uuid);
		if (uuidEntity != null) {
			return Response.ok(uuidEntity).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAll() {
		List<EntityUsingUuid> longEntities = this.manager.getAll();
		if (longEntities != null && !longEntities.isEmpty()) {
			return Response.ok(longEntities).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("{uuid}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response update(@PathParam("uuid") String uuid, EntityUsingUuid uuidEntity) {
		if (!uuidEntity.isValid() || !uuidEntity.getUuidAsId().equals(UUID.fromString(uuid))) {
			throw new EntityNotValidException(
					"The Entity is not properly formatted. Please refer to the API documentation.");
		}
		try {
			EntityUsingUuid uuidEntityToUpdate = this.manager.findById(uuid);
			if (uuidEntityToUpdate != null) {
				uuidEntityToUpdate = this.manager.save(uuidEntity);
				if (uuidEntityToUpdate != null) {
					return Response.ok(uuidEntityToUpdate).build();
				} else {
					return Response.status(Response.Status.CONFLICT).build();
				}
			}
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (PersistenceException ex) {
			if (ex.getCause().getMessage().contains("Constraint")) {
				throw new UniqueConstraintException("An Entity with this unique data already exists.");
			}
			LOGGER.log(Level.DEBUG, ex.toString(), ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.header("error", "An unknown error has occured, please contact support.").build();
		}
	}

	@DELETE
	@Path("{uuid}")
	public Response delete(@PathParam("uuid") String uuid) {
		try {
			EntityUsingUuid uuidEntityToDelete = this.manager.findById(uuid);
			if (uuidEntityToDelete == null) {
				return Response.noContent().build();
			}
			OperationResult result = this.manager.delete(uuid);
			switch (result) {
			case SUCCESS:
				return Response.ok().build();
				
			case CONFLICT:
				String message = "The Entity is in use and cannot be deleted.";
				return Response.status(Response.Status.CONFLICT).entity(message).type(MediaType.TEXT_PLAIN).build();
				
			default:
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		} catch (Exception ex) {
			LOGGER.log(Level.DEBUG, ex.toString(), ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.header("error", "An unknown error has occured, please contact support.").build();
		}
	}
}