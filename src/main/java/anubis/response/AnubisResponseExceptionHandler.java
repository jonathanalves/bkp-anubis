package anubis.response;

import anubis.MessageSystem;
import anubis.enumeration.EnumStatusRetorno;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class AnubisResponseExceptionHandler {

	/** @param request */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Response handleNotReadableException(RuntimeException e, WebRequest request) {
		HttpMessageNotReadableException exception = (HttpMessageNotReadableException) e;
		e.printStackTrace();
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		
		if(exception.getCause() instanceof InvalidFormatException) {
			InvalidFormatException invalid = (InvalidFormatException) exception.getCause();
			String valor = invalid.getValue().toString();
			response.setMessage(MessageSystem.getMessage("system.erro.converter.valor", valor));
		} else {
			response.setMessage(MessageSystem.getMessage("system.erro.converter.valor"));
		}
		return response;
	}

	/** @param request */
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({CannotCreateTransactionException.class})
	public Response handleCannotCreateTransactionException(WebRequest request, RuntimeException e) {
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage(MessageSystem.getMessage("system.tempo.esgotado"));
		response.setResponse(e);
		return response;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public Response processValidationError(MethodArgumentNotValidException manve, WebRequest request) {
        List<Error> errors = new ArrayList<>();
        BindingResult br = manve.getBindingResult();
        List<FieldError> fieldErrors = br.getFieldErrors();
        for (FieldError error : fieldErrors) {
        	String message = MessageSystem.formatMessage(error.getDefaultMessage());
        	errors.add(new Error(error.getField(), message, true));
        }
        
        Response response = Response.getInstance();
        response.setStatus(EnumStatusRetorno.ERROR);
        response.setMessage("validacao");
        response.setErrors(errors);
        return response;
    }

	/** @param request */
	@ResponseBody
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({AccessDeniedException.class,AuthenticationServiceException.class, InternalAuthenticationServiceException.class})
	public Response handleAccessDeniedException(HttpServletRequest req, RuntimeException e, WebRequest request) {
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage(MessageSystem.getMessage("system.acesso.negado"));
		response.setResponse(e);
		return response;
	}
	
	/** @param request */
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({RuntimeException.class, Exception.class})
	public Response handleGenericException(HttpServletRequest req, RuntimeException e, WebRequest request) {
		e.printStackTrace();
		Response response = Response.getInstance();
		response.setStatus(EnumStatusRetorno.ERROR);
		response.setMessage(MessageSystem.getMessage("system.erro.interno"));
		response.setResponse(e);
		return response;
	}
	
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ResponseException.class)
	protected Response handleInvalidRequest(RuntimeException e, WebRequest request) {
		ResponseException ire = (ResponseException) e;
		return ire.getResponse();
	}
	
}
