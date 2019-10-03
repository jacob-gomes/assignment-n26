package com.n26.service;

import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.n26.controller.model.NTSGetResponse;
import com.n26.controller.model.NTSPostRequest;
import com.n26.service.exception.NTSInvalidException;
import com.n26.service.model.NTSOverallStatisticsModel;
import com.n26.service.model.NTSMinuteStatisticsModel;
import com.n26.service.util.NTSServiceUtil;
/**
 * Service class Implementation
 * @author Jacob
 *
 */
@Component
public class NTSServiceImpl implements NTSService {

	Logger logger = LoggerFactory.getLogger(NTSServiceImpl.class);
		
	private NTSOverallStatisticsModel ntsDailyStatisticsModel;
	
	@Autowired
	public NTSServiceImpl(NTSOverallStatisticsModel ntsDailyStatisticsModel) {
		this.ntsDailyStatisticsModel = ntsDailyStatisticsModel;
	}

	/**
	 * Service method used to add new record
	 */
	@Override
	public HttpStatus addANewRecord(NTSPostRequest ntsPostRequest) throws NTSInvalidException   {		
		
		NTSMinuteStatisticsModel newNTSMinuteStatistics = new NTSMinuteStatisticsModel();
		
		NTSServiceUtil.populateNTSMinuteStatisticObjectFromPostRequest(ntsPostRequest, newNTSMinuteStatistics);
		
		Long currentTimeInSecondFromEpoch = Calendar.getInstance().getTimeInMillis() / 1000;
		
		if(NTSServiceUtil.isRecordOlderThenAMinute(currentTimeInSecondFromEpoch,newNTSMinuteStatistics.getTimeStampSeconds())) {
			logger.info("No records found for this minute");
			return HttpStatus.NO_CONTENT;
		}

		Map<Long,NTSMinuteStatisticsModel> perSecondMap = ntsDailyStatisticsModel.getPerSecondRecordMap();
		
		logger.info("Entering lock for pushing data");
		synchronized(perSecondMap) {
			NTSMinuteStatisticsModel existingNTSMinuteStatistics = perSecondMap.get(newNTSMinuteStatistics.getTimeStampSeconds());
			if(null == existingNTSMinuteStatistics) {
				logger.info("Adding new record");
				perSecondMap.put(newNTSMinuteStatistics.getTimeStampSeconds(), newNTSMinuteStatistics);
			}else {
				logger.info("Updating existing record");
				existingNTSMinuteStatistics.update(newNTSMinuteStatistics);
			}
		}
		logger.info("Releasing lock for pushing data");
		
		return HttpStatus.CREATED;
	}

	/**
	 * Service method to fetch existing records in past 60 seconds
	 */
	@Override
	public NTSGetResponse getMinuteStatistics() {
		Long currentTimeInSecondFromEpoch = Calendar.getInstance().getTimeInMillis() / 1000;
		Map<Long,NTSMinuteStatisticsModel> perSecondMap = ntsDailyStatisticsModel.getPerSecondRecordMap();
		NTSMinuteStatisticsModel responseNTSMinuteStatistics = new NTSMinuteStatisticsModel();
		NTSGetResponse ntsGetResponse = new NTSGetResponse();
		
		logger.info("Entering lock for retreival");
		synchronized(perSecondMap) {
			for(Long counter = 0L; counter < 60L; counter++) {
				NTSMinuteStatisticsModel existingNTSMinuteStatistics = perSecondMap.get(currentTimeInSecondFromEpoch-counter);
				if(null != existingNTSMinuteStatistics) {
					responseNTSMinuteStatistics.update(existingNTSMinuteStatistics);
				}
			}
		}
		logger.info("Releasing lock for retreival");
		
		NTSServiceUtil.populateNTSGetResponseFromNTSMinuteStatistics(ntsGetResponse,responseNTSMinuteStatistics);
		
		return ntsGetResponse;
	}

	/**
	 * Replenishes the whole map
	 */
	@Override
	public void deleteAllRecords() {
		ntsDailyStatisticsModel.flushAllRecords();		
	}

}
