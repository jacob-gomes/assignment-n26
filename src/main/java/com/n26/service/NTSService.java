package com.n26.service;

import org.springframework.http.HttpStatus;

import com.n26.controller.model.NTSGetResponse;
import com.n26.controller.model.NTSPostRequest;
import com.n26.service.exception.NTSInvalidException;
/*
 * Interface for the service layer
 */
public interface NTSService {
	/**
	 * Adding new record
	 * @param ntsPostRequest
	 * @return
	 * @throws NTSInvalidException
	 */
	HttpStatus addANewRecord(NTSPostRequest ntsPostRequest) throws  NTSInvalidException;

	/**
	 * Retrieving record for past 60 seconds
	 * @return
	 */
	NTSGetResponse getMinuteStatistics();

	/**
	 * Deleting all records
	 */
	void deleteAllRecords();

}
