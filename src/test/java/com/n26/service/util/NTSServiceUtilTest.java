package com.n26.service.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.n26.controller.model.NTSGetResponse;
import com.n26.controller.model.NTSPostRequest;
import com.n26.service.exception.NTSInvalidException;
import com.n26.service.model.NTSMinuteStatisticsModel;

@RunWith(MockitoJUnitRunner.class)
public class NTSServiceUtilTest {
	@Test
	public void calculateTimeInSecondFromEpochPositiveTest() throws NTSInvalidException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Long secondsFromEpoch = NTSServiceUtil.calculateTimeInSecondFromEpoch("2019-09-06T17:49:00.312Z");
		assertEquals(Long.valueOf(1567792140L),secondsFromEpoch);
	}
	
	@Test(expected = NTSInvalidException.class)
	public void calculateTimeInSecondFromEpochThrowExceptionTest() throws NTSInvalidException {
		NTSServiceUtil.calculateTimeInSecondFromEpoch("Not a date");
	}
	
	
	
	@Test
	public void populateNTSMinuteStatisticObjectFromPostRequestPossitiveTest() throws NTSInvalidException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		NTSMinuteStatisticsModel ntsMinuteStatistics = new NTSMinuteStatisticsModel();
		
		ntsPostRequest.setAmount("1000.00");
		ntsPostRequest.setTimestamp("2019-09-06T17:49:00.312Z");
		
		NTSServiceUtil.populateNTSMinuteStatisticObjectFromPostRequest(ntsPostRequest, ntsMinuteStatistics);
		
		assertEquals(Long.valueOf(1567792140L),ntsMinuteStatistics.getTimeStampSeconds());
		assertEquals(BigDecimal.valueOf(1000.00).setScale(2,RoundingMode.HALF_UP), ntsMinuteStatistics.getSum());
		assertEquals(BigDecimal.valueOf(1000.00).setScale(2,RoundingMode.HALF_UP), ntsMinuteStatistics.getMin());
		assertEquals(BigDecimal.valueOf(1000.00).setScale(2,RoundingMode.HALF_UP), ntsMinuteStatistics.getMax());
		assertEquals(Long.valueOf(1L), ntsMinuteStatistics.getCount());
	}
	
	@Test(expected = NTSInvalidException.class)
	public void populateNTSMinuteStatisticObjectFromPostRequestImproperAmountTest() throws NTSInvalidException {
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		NTSMinuteStatisticsModel ntsMinuteStatistics = new NTSMinuteStatisticsModel();
		
		ntsPostRequest.setAmount("ABC.DE");
		ntsPostRequest.setTimestamp("2019-09-06T17:49:00.312Z");
		
		NTSServiceUtil.populateNTSMinuteStatisticObjectFromPostRequest(ntsPostRequest, ntsMinuteStatistics);
	}
	
	@Test(expected = NTSInvalidException.class)
	public void populateNTSMinuteStatisticObjectFromPostRequestImproperDateTest() throws NTSInvalidException {
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		NTSMinuteStatisticsModel ntsMinuteStatistics = new NTSMinuteStatisticsModel();
		
		ntsPostRequest.setAmount("100.28");
		ntsPostRequest.setTimestamp("Not a date");
		
		NTSServiceUtil.populateNTSMinuteStatisticObjectFromPostRequest(ntsPostRequest, ntsMinuteStatistics);
	}
	
	@Test
	public void isRecordOlderThenAMinutePositiveTest() throws NTSInvalidException {
		long currentTimeInMiliSeconds = Calendar.getInstance().getTimeInMillis();
		Boolean isRecordOrlder = NTSServiceUtil.isRecordOlderThenAMinute(currentTimeInMiliSeconds , currentTimeInMiliSeconds - 70);
		assertTrue(isRecordOrlder);
	}
	
	@Test
	public void isRecordOlderThenAMinuteNegativeTest() throws NTSInvalidException {
		long currentTimeInMiliSeconds = Calendar.getInstance().getTimeInMillis();
		Boolean isRecordOrlder = NTSServiceUtil.isRecordOlderThenAMinute(currentTimeInMiliSeconds, currentTimeInMiliSeconds);
		assertTrue(!isRecordOrlder);
	}
	
	@Test(expected = NTSInvalidException.class)
	public void isRecordOlderThenAMinuteFutureDatedTest() throws NTSInvalidException {
		long currentTimeInMiliSeconds = Calendar.getInstance().getTimeInMillis();
		NTSServiceUtil.isRecordOlderThenAMinute(currentTimeInMiliSeconds, currentTimeInMiliSeconds + 70);
		
	}
	
	@Test
	public void populateNTSGetResponseFromNTSMinuteStatisticsPositiveTest() {
		NTSGetResponse ntsGetResponse = new NTSGetResponse();
		NTSMinuteStatisticsModel responseNTSMinuteStatistics = new NTSMinuteStatisticsModel();
		
		responseNTSMinuteStatistics.setSum(BigDecimal.valueOf(1000.23).setScale(2 , RoundingMode.HALF_UP));
		responseNTSMinuteStatistics.setMax(BigDecimal.valueOf(100.23).setScale(2 , RoundingMode.HALF_UP));
		responseNTSMinuteStatistics.setMin(BigDecimal.valueOf(10.23).setScale(2 , RoundingMode.HALF_UP));
		responseNTSMinuteStatistics.setAvg(BigDecimal.valueOf(120.223).setScale(2 , RoundingMode.HALF_UP));
		responseNTSMinuteStatistics.setCount(20L);
		
		NTSServiceUtil.populateNTSGetResponseFromNTSMinuteStatistics(ntsGetResponse,responseNTSMinuteStatistics);
		
		assertEquals("1000.23",ntsGetResponse.getSum());
		assertEquals("100.23",ntsGetResponse.getMax());
		assertEquals("10.23",ntsGetResponse.getMin());
		assertEquals("120.22",ntsGetResponse.getAvg());
		assertEquals(Long.valueOf(20L),ntsGetResponse.getCount());
	}
	
	
	@Test
	public void populateNTSGetResponseFromNTSMinuteStatisticsWithEmptyNTSMinuteStatisticsModelTest() {
		NTSGetResponse ntsGetResponse = new NTSGetResponse();
		NTSMinuteStatisticsModel responseNTSMinuteStatistics = new NTSMinuteStatisticsModel();
				
		NTSServiceUtil.populateNTSGetResponseFromNTSMinuteStatistics(ntsGetResponse,responseNTSMinuteStatistics);
		
		assertEquals("0.00",ntsGetResponse.getSum());
		assertEquals("0.00",ntsGetResponse.getMax());
		assertEquals("0.00",ntsGetResponse.getMin());
		assertEquals("0.00",ntsGetResponse.getAvg());
		assertEquals(Long.valueOf(0L),ntsGetResponse.getCount());
	}
	
	@Test
	public void populateNTSGetResponseFromNTSMinuteStatisticsWithNullNTSMinuteStatisticsModelTest() {
		NTSGetResponse ntsGetResponse = new NTSGetResponse();
				
		NTSServiceUtil.populateNTSGetResponseFromNTSMinuteStatistics(ntsGetResponse,null);
		
		assertEquals(null,ntsGetResponse.getSum());
		assertEquals(null,ntsGetResponse.getMax());
		assertEquals(null,ntsGetResponse.getMin());
		assertEquals(null,ntsGetResponse.getAvg());
		assertEquals(null,ntsGetResponse.getCount());
	}
}
