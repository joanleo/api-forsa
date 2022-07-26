package com.prueba.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Joan Leon
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	/**
	 * 
	 * @param exception
	 * @param webRequest
	 * @return
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorDetalles> manejarResourceNotFoundException(ResourceNotFoundException exception,WebRequest webRequest){
		ErrorDetalles errorDetalles = new ErrorDetalles(new Date(),exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetalles,HttpStatus.NOT_FOUND);
	}
	
	/**
	 * 
	 * @param exception
	 * @param webRequest
	 * @return
	 */
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorDetalles> manejarResourceAlreadyExistsException(ResourceAlreadyExistsException exception, WebRequest webRequest){
		ErrorDetalles errorDetalles = new ErrorDetalles(new Date(),exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetalles>(errorDetalles, HttpStatus.CONFLICT);
	}
	
	/**
	 * 
	 * @param exception
	 * @param webRequest
	 * @return
	 */
	@ExceptionHandler(ResourceCannotBeDeleted.class)
	public ResponseEntity<ErrorDetalles> manejarResourceCannotBeDeleted(ResourceCannotBeDeleted exception, WebRequest webRequest){
		ErrorDetalles errorDetalles = new ErrorDetalles(new Date(),exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<ErrorDetalles>(errorDetalles, HttpStatus.CONFLICT);
	}
	
	/**
	 * 
	 * @param exception
	 * @param webRequest
	 * @return
	 */
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorDetalles> manejarApiException(ApiException exception, WebRequest webRequest){
		System.out.println("manejando excepcion " + exception.getEstado());
		ErrorDetalles errorDetalles = new ErrorDetalles(new Date(), exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetalles,HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 
	 * @param exception
	 * @param webRequest
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetalles> manejarGlobalException(Exception exception,WebRequest webRequest){
		ErrorDetalles errorDetalles = new ErrorDetalles(new Date(),exception.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorDetalles,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, String> errores = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String nombreCampo = ((FieldError)error).getField();
			String mensaje = error.getDefaultMessage();
			
			errores.put(nombreCampo, mensaje);
		});
		
		return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
	}
}

