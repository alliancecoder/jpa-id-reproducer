package alliancecoder.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class EntityNotValidException extends WebApplicationException {

	private static final long serialVersionUID = -1532640186971317347L;

	public EntityNotValidException(String message) {
		super(Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
