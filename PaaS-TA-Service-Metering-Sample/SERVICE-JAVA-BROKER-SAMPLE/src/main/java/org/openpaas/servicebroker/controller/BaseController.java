package org.openpaas.servicebroker.controller;

import javax.servlet.http.HttpServletResponse;

import org.openpaas.servicebroker.exception.ServiceBrokerApiVersionException;
import org.openpaas.servicebroker.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ExceptionHandler를 정의한 기본 컨트롤러이고 Catalog, ServiceInstance, ServiceInstanceBinding 컨트롤러는 이 클래스를 상속한다.
 * 
 * @author 송창학
 * @date 2015.0629
 */

public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
		
	@ExceptionHandler(ServiceBrokerApiVersionException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			ServiceBrokerApiVersionException ex, 
			HttpServletResponse response) {
	    return getErrorResponse(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			HttpMessageNotReadableException ex, 
			HttpServletResponse response) {
	    return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			MethodArgumentNotValidException ex, 
			HttpServletResponse response) {
	    BindingResult result = ex.getBindingResult();
	    String message = "Missing required fields:";
	    for (FieldError error: result.getFieldErrors()) {
	    	message += " " + error.getField();
	    }
		return getErrorResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			Exception ex, 
			HttpServletResponse response) {
		logger.warn("Exception", ex);
	    return getErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public ResponseEntity<ErrorMessage> getErrorResponse(String message, HttpStatus status) {
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(message), 
				status);
	}
	
}
