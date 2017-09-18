package anubis.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import anubis.MessageSystem;
import anubis.enumeration.EnumStatusRetorno;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response implements Serializable{

	private static final long serialVersionUID = -4369130483321841391L;
	
	private EnumStatusRetorno status;
	private String message;
	private List<Error> errors;
	private Object response;

	private Response() {
		super();
	}

	public static Response getInstance() {
		return new Response();
	}
	
	public static Response returnObject(Object object) {
		Response response = new Response();
		response.setStatus(EnumStatusRetorno.SUCCESS);
		response.setResponse(object);
		response.setMessage("");
		return response;
	}
	
	public static Response returnObject(Object object, String message, String... params) {
		Response response = new Response();
		response.setStatus(EnumStatusRetorno.SUCCESS);
		response.setResponse(object);
		response.setMessage(MessageSystem.formatMessage(message, params));
		return response;
	}
	
	public static Response returnMessage(String message, String... params) {
		Response response = new Response();
		response.setStatus(EnumStatusRetorno.SUCCESS);
		response.setMessage(MessageSystem.formatMessage(message, params));
		return response;
	}

	public static void verificarErros(BindingResult bindingResult) throws ResponseException {
		if (bindingResult.hasErrors()) {
			List<Error> errors = new ArrayList<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(new Error(error.getField(), error.getDefaultMessage(), false));
			}
			throw new ResponseException("validacao", errors);
		}
	}

	public static void verificarErros(BindingResult bindingResult, ObjectError... erros) throws ResponseException {
		for (ObjectError error : erros) {
			if (error != null) {
				bindingResult.addError(error);
			}
		}
		if (bindingResult.hasErrors()) {
			List<Error> errors = new ArrayList<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(new Error(error.getField(), error.getDefaultMessage(), false));
			}
			for (ObjectError error : erros) {
				if (error != null) {
					errors.add(new Error(error.getObjectName(), error.getDefaultMessage(), false));
				}
			}
			throw new ResponseException("validacao", errors);
		}
	}
	
	public static void verificarErros(BindingResult bindingResult, List<ObjectError> erros) throws ResponseException {
		for (ObjectError error : erros) {
			if (error != null) {
				bindingResult.addError(error);
			}
		}
		if (bindingResult.hasErrors()) {
			List<Error> errors = new ArrayList<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.add(new Error(error.getField(), error.getDefaultMessage(), false));
			}
			for (ObjectError error : erros) {
				if (error != null) {
					errors.add(new Error(error.getObjectName(), error.getDefaultMessage(), false));
				}
			}
			throw new ResponseException("validacao", errors);
		}
	}
	
}
