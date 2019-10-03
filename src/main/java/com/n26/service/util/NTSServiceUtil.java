package com.n26.service.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.n26.controller.model.NTSGetResponse;
import com.n26.controller.model.NTSPostRequest;
import com.n26.service.exception.NTSInvalidException;
import com.n26.service.model.NTSMinuteStatisticsModel;
/**
 * Util class to subordinate Service
 * @author Jacob
 *
 */
public class NTSServiceUtil {
	
	/**
	 * Obstructing from being initialized
	 */
	private NTSServiceUtil() {}
	
	/**
	 * calculate the seconds elapsed from epoch
	 * @param timestamp
	 * @return
	 * @throws NTSInvalidException
	 */
	public static Long calculateTimeInSecondFromEpoch(String timestamp) throws NTSInvalidException  {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date date = dateFormat.parse(timestamp);
			return date.getTime() / 1000;
		}catch(ParseException e) {
			throw new NTSInvalidException("Date could not be parsed");
		}
	}

	/**
	 * Map NTSPostRequest to NTSMinuteStatisticsModel object
	 * @param ntsPostRequest
	 * @param ntsMinuteStatistics
	 * @return
	 * @throws NTSInvalidException
	 */
	public static void populateNTSMinuteStatisticObjectFromPostRequest(NTSPostRequest ntsPostRequest, NTSMinuteStatisticsModel ntsMinuteStatistics) throws NTSInvalidException {
		BigDecimal amount = null;
		
		Long requestTimeInSecondFromEpoch = calculateTimeInSecondFromEpoch(ntsPostRequest.getTimestamp());
		
		ntsMinuteStatistics.setTimeStampSeconds(requestTimeInSecondFromEpoch);
		
		try {
			amount = new BigDecimal(ntsPostRequest.getAmount());
		}catch(NumberFormatException e) {
			throw new NTSInvalidException(e.getMessage());
		}
		ntsMinuteStatistics.setSum(amount);
		ntsMinuteStatistics.setMin(amount);
		ntsMinuteStatistics.setMax(amount);
		ntsMinuteStatistics.setCount(1L);
	}
	
	/**
	 * Check if record older the 60 seconds
	 * @param currentTimeInSecondFromEpoch
	 * @param timeInSecondFromEpoch
	 * @return
	 * @throws NTSInvalidException
	 */
	public static boolean isRecordOlderThenAMinute(Long currentTimeInSecondFromEpoch, Long timeInSecondFromEpoch) throws NTSInvalidException {
		Long differenceInTime = currentTimeInSecondFromEpoch-timeInSecondFromEpoch;
		if(differenceInTime < 0) {
			throw new NTSInvalidException("Future dated transaction");
		}
		
		return differenceInTime >= 60;
		
	}

	/**
	 * Map NTSGetResponse to NTSMinuteStatisticsModel object and also ensure there is no null pointers
	 * @param ntsGetResponse
	 * @param responseNTSMinuteStatistics
	 */
	public static void populateNTSGetResponseFromNTSMinuteStatistics(NTSGetResponse ntsGetResponse,
			NTSMinuteStatisticsModel responseNTSMinuteStatistics) {
		if(null != responseNTSMinuteStatistics) {
			ntsGetResponse.setSum(null != responseNTSMinuteStatistics.getSum() ?
					responseNTSMinuteStatistics.getSum().toString() : "0.00");

			ntsGetResponse.setMax(null != responseNTSMinuteStatistics.getMax() ?
					responseNTSMinuteStatistics.getMax().toString() : "0.00");

			ntsGetResponse.setMin(null != responseNTSMinuteStatistics.getMin() ?
					responseNTSMinuteStatistics.getMin().toString() : "0.00");

			ntsGetResponse.setAvg(null != responseNTSMinuteStatistics.getAvg() ?
					responseNTSMinuteStatistics.getAvg().toString() : "0.00");

			ntsGetResponse.setCount(null != responseNTSMinuteStatistics.getCount() ?
					responseNTSMinuteStatistics.getCount() : 0L);
		}
	}

}
