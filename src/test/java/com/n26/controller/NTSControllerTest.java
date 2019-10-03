package com.n26.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.n26.controller.model.NTSGetResponse;
import com.n26.controller.model.NTSPostRequest;
import com.n26.service.NTSService;
import com.n26.service.exception.NTSInvalidException;

@RunWith(MockitoJUnitRunner.class)
public class NTSControllerTest {
	
	private NTSController ntsController;
	
	@Mock
	private NTSService ntsService;
	
	@Before
	public void init() {
		ntsController = new NTSController(ntsService);
	}

	@Test
	public void addANewRecordPostiveTest() throws NTSInvalidException {
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		when(ntsService.addANewRecord(any(NTSPostRequest.class))).thenReturn(HttpStatus.CREATED);
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp("2019-09-06T17:49:00.312Z");
		
		ResponseEntity<String> requestEntity = ntsController.addANewRecord(ntsPostRequest);
		
		assertEquals(HttpStatus.CREATED, requestEntity.getStatusCode());
		assertTrue(requestEntity.getBody().isEmpty());
	}
	
	@Test(expected = NTSInvalidException.class)
	public void addANewRecordThrowExceptionTest() throws NTSInvalidException {
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		when(ntsService.addANewRecord(any(NTSPostRequest.class))).thenThrow(new NTSInvalidException("message"));
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp("2019-09-06T17:49:00.312Z");
		
		ntsController.addANewRecord(ntsPostRequest);	
	}
	
	@Test
	public void getMinuteStatisticsPositiveTest() {
		NTSGetResponse ntsGetResponse = new NTSGetResponse();
		ntsGetResponse.setSum("1000.00");
		ntsGetResponse.setMax("1.00");
		ntsGetResponse.setMin("1.00");
		ntsGetResponse.setAvg("1.00");
		ntsGetResponse.setCount(1L);	
		
		when(ntsService.getMinuteStatistics()).thenReturn(ntsGetResponse);
		
		ResponseEntity<NTSGetResponse> requestEntity = ntsController.getMinuteStatistics();
		
		assertEquals(HttpStatus.OK, requestEntity.getStatusCode());
		assertEquals("1000.00", ntsGetResponse.getSum());
		assertEquals("1.00", ntsGetResponse.getMax());
		assertEquals("1.00", ntsGetResponse.getMin());
		assertEquals("1.00", ntsGetResponse.getAvg());
		assertEquals(Long.valueOf(1L), ntsGetResponse.getCount());		
		
	}
	
	@Test
	public void deleteAllRecordsPositiveTest() {
		
		ResponseEntity<String> requestEntity = ntsController.deleteAllRecords();	
		assertEquals(HttpStatus.NO_CONTENT, requestEntity.getStatusCode());
		assertTrue(requestEntity.getBody().isEmpty());
	}
}
