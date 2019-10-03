package com.n26.controller;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.controller.model.NTSGetResponse;
import com.n26.controller.model.NTSPostRequest;
import com.n26.service.NTSService;
import com.n26.service.exception.NTSInvalidException;

/**
 * 
 * @author Jacob
 *
 */
@Component
@RestController
public class NTSController {
	Logger logger = LoggerFactory.getLogger(NTSController.class);
		
	private NTSService ntsService;
	
	@Autowired
	public NTSController(NTSService ntsService) {
		this.ntsService = ntsService;
	}

	/**
	 * Adds a new record on the basis of timestamp given
	 * @param ntsPostRequest
	 * @return
	 * @throws ParseException 
	 */
	@PostMapping(value = "/transactions",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addANewRecord(@RequestBody NTSPostRequest ntsPostRequest) throws NTSInvalidException {
		
		logger.info("Received call for POST /transactions");
		HttpStatus httpStatus= ntsService.addANewRecord(ntsPostRequest);
		
		return new ResponseEntity<>("", httpStatus);
	}
	
	/**
	 * Get combined results of all the values in past minute
	 * @param ntsPostRequest
	 * @return
	 */
	@GetMapping(value = "/statistics",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<NTSGetResponse> getMinuteStatistics() {
		
		logger.info("Received call for GET /statistics");
		NTSGetResponse ntsGetResponse = ntsService.getMinuteStatistics();
		
		return new ResponseEntity<>(ntsGetResponse, HttpStatus.OK);
	}
	
	/**
	 * Refreshes the value to initial state
	 * @param ntsPostRequest
	 * @return
	 */
	@DeleteMapping(value = "/transactions")
	public ResponseEntity<String> deleteAllRecords() {
		
		logger.info("Received call for DELETE /transactions");
		ntsService.deleteAllRecords();
		
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}
}
