package anubis.response;

import anubis.MessageSystem;
import anubis.enumeration.EnumStatusRetorno;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(value=HttpStatus.OK)
public class ResponseException extends RuntimeException  {

	private static final long serialVersionUID = 5242151978325562763L;

	private Response response;
	private Exception exception;

	public ResponseException(Response response) {
		this.response = response;
	}
	
	public ResponseException(String message, String... params) {
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage(MessageSystem.formatMessage(message, params));
		this.response = response;
	}
	
	public ResponseException(FieldForm field, String message, String... params) {
		List<Error> errors = new ArrayList<>();
        errors.add(new Error(field.getName(), MessageSystem.formatMessage(message, params), true));
        
        Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage("validacao");
		response.setErrors(errors);
		this.response = response;
	}
	
	public ResponseException(String message, List<Error> errors) {
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage(MessageSystem.formatMessage(message));
		response.setErrors(errors);
		this.response = response;
	}
	
	public ResponseException(Exception exception, String message, String... params) {
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage(MessageSystem.formatMessage(message, params));
		this.response = response;
		this.exception = exception;
	}
	

	public Response getResponse() {
		return response;
	}

	public Exception getException() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return response.getMessage();
	}

}
