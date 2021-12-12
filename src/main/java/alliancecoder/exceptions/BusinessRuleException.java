package alliancecoder.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BusinessRuleException extends WebApplicationException {

	private static final long serialVersionUID = -1300165064324745714L;

	public BusinessRuleException(String message) {
		super(Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}