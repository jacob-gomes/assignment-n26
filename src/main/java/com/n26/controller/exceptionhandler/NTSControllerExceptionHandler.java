package com.n26.controller.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.n26.service.exception.NTSInvalidException;

/**
 * 
 * @author Jacob
 *
 */
@ControllerAdvice
public class NTSControllerExceptionHandler {
	Logger logger = LoggerFactory.getLogger(NTSControllerExceptionHandler.class);
	
	/**
	 * Handles all Exception
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
	public ResponseEntity<String> handleException(Exception exception) {
		logger.error("Exception caught",exception);
		return new ResponseEntity<>( exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	
	/**
	 * Handles all JSONException
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ResponseBody
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		logger.error("HttpMessageNotReadableException caught",exception);
		return new ResponseEntity<>( exception.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
	
	/**
	 * Handles NTSInvalidException
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = NTSInvalidException.class)
	@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
	public ResponseEntity<String> handleNTSInvalidException(NTSInvalidException exception) {
		logger.error("NTSInvalidException caught",exception);
		return new ResponseEntity<>( exception.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		
	}
}
