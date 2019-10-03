package com.n26.controller.exceptionhandler;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.n26.service.exception.NTSInvalidException;

@RunWith(MockitoJUnitRunner.class)
public class NTSControllerExceptionHandlerTest {
	
	private NTSControllerExceptionHandler ntsControllerExceptionHandler;
	
	@Before
	public void init() {
		ntsControllerExceptionHandler = new NTSControllerExceptionHandler();
	}
	
	
	@Test
	public void handleExceptionTest() {
		ResponseEntity<String> responseEntity = ntsControllerExceptionHandler.handleException(new Exception("Hello"));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals("Hello",responseEntity.getBody());
	}
	
	@Test
	public void handleNTSInvalidExceptionTest() {
		ResponseEntity<String> responseEntity = ntsControllerExceptionHandler.handleNTSInvalidException(new NTSInvalidException("Hello"));
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		assertEquals("Hello",responseEntity.getBody());
	}
	
	@Test
	public void handleHttpMessageNotReadableExceptionTest() {
		ResponseEntity<String> responseEntity = ntsControllerExceptionHandler.handleHttpMessageNotReadableException(new HttpMessageNotReadableException("Hello"));
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Hello",responseEntity.getBody());
	}
	
}
