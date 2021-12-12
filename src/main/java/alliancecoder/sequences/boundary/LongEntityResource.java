package alliancecoder.sequences.boundary;

import java.net.URI;
import java.util.List;

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
import alliancecoder.sequences.entity.EntityUsingLong;

@RequestScoped
@Path("sequences")
public class LongEntityResource {
    
    private static final Logger LOGGER = Logger.getLogger(LongEntityResource.class.getName());

    @Inject
    LongEntityManager manager;

	// POST should return 201 with Location URI in header
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
    public Response save(EntityUsingLong longEntity) {
		if (!longEntity.isValid()) {
			throw new EntityNotValidException(
					"The Entity is not properly formatted. Please refer to the API documentation.");
		}
		try {
			EntityUsingLong saved = this.manager.save(longEntity);
			Long id = saved.getLongAsId();
			URI uri = URI.create("/sequences/" + id);
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

	@GET
	@Path("{longAsId}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response find(@PathParam("longAsId") Long longAsId) {
		EntityUsingLong longEntity = this.manager.findById(longAsId);
		if (longEntity != null) {
			return Response.ok(longEntity).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAll() {
		List<EntityUsingLong> longEntities = this.manager.getAll();
		if (longEntities != null && !longEntities.isEmpty()) {
			return Response.ok(longEntities).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@PUT
	@Path("{longAsId}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response update(@PathParam("longAsId") Long longAsId, EntityUsingLong longEntity) {
		if (!longEntity.isValid() || !longEntity.getLongAsId().equals(longAsId)) {
			throw new EntityNotValidException(
					"The Entity is not properly formatted. Please refer to the API documentation.");
		}
		try {
			EntityUsingLong longEntityToUpdate = this.manager.findById(longAsId);
			if (longEntityToUpdate != null) {
				longEntityToUpdate = this.manager.save(longEntity);
				if (longEntityToUpdate != null) {
					return Response.ok(longEntityToUpdate).build();
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
	@Path("{longAsId}")
	public Response delete(@PathParam("longAsId") Long longAsId) {
		try {
			EntityUsingLong longEntityToDelete = this.manager.findById(longAsId);
			if (longEntityToDelete == null) {
				return Response.noContent().build();
			}
			OperationResult result = this.manager.delete(longAsId);
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