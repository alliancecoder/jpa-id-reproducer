package alliancecoder.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RecordNotFoundException extends WebApplicationException {
	
	private static final long serialVersionUID = 794014517164920706L;

	public RecordNotFoundException(String message) {
		super(Response.status(Response.Status.NOT_FOUND).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
