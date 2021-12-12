package alliancecoder.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UniqueConstraintException extends WebApplicationException {

	private static final long serialVersionUID = -7995338774328579031L;

	public UniqueConstraintException(String message) {
		super(Response.status(Response.Status.CONFLICT).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
