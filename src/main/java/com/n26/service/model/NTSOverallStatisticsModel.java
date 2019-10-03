package com.n26.service.model;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
/**
 * Model class for the service
 * @author Jacob
 *
 */
@Component
public class NTSOverallStatisticsModel {
	
	private Map<Long, NTSMinuteStatisticsModel> perSecondRecordMap;
	
	/**
	 * Initializing object
	 */
	@PostConstruct
	void initiateMap() {
		perSecondRecordMap = new HashMap<>();
	}
	
	/**
	 * Replenishing perSecondRecordMap
	 */
	public void flushAllRecords() {
		this.initiateMap();
	}

	/**
	 * @return the perSecondRecordMap
	 */
	public Map<Long, NTSMinuteStatisticsModel> getPerSecondRecordMap() {
		return perSecondRecordMap;
	}

	/**
	 * @param perSecondRecordMap the perSecondRecordMap to set
	 */
	public void setPerSecondRecordMap(Map<Long, NTSMinuteStatisticsModel> perSecondRecordMap) {
		this.perSecondRecordMap = perSecondRecordMap;
	}
	
	
	
}
