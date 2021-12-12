package alliancecoder.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ItemInUseException extends WebApplicationException {

	private static final long serialVersionUID = -3366233238104141826L;

	public ItemInUseException(String message) {
		super(Response.status(Response.Status.CONFLICT).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}