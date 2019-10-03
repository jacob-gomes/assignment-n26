package com.n26.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.n26.controller.model.NTSPostRequest;
import com.n26.service.exception.NTSInvalidException;
import com.n26.service.model.NTSMinuteStatisticsModel;

@RunWith(MockitoJUnitRunner.class)
public class NTSServiceImplPOSTTest extends NTSServiceAbstractTest {

	@Test
	public void addANewRecordWithEmptyMapTest() throws NTSInvalidException {			
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp(getDateTimeFromOffSet(0));
		
		HttpStatus  httpStatus = ntsServiceImpl.addANewRecord(ntsPostRequest);
		
		assertEquals(HttpStatus.CREATED, httpStatus);
	}
	
	@Test
	public void addANewRecordWithNonEmptyMapForUpdateTest() throws NTSInvalidException {	
		Map<Long, NTSMinuteStatisticsModel> perSecondMap = new ConcurrentHashMap<>();
		Long currentTimeSecond = Calendar.getInstance().getTimeInMillis()/1000;
		
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel.setSum(BigDecimal.valueOf(1000.00));
		ntsMinuteStatisticsModel.setMax(BigDecimal.valueOf(100.00));
		ntsMinuteStatisticsModel.setMin(BigDecimal.valueOf(10.00));
		ntsMinuteStatisticsModel.setAvg(BigDecimal.valueOf(10.00));
		ntsMinuteStatisticsModel.setCount(20L);
		
		perSecondMap.put(currentTimeSecond, ntsMinuteStatisticsModel);		
		
		when(ntsDailyStatisticsModel.getPerSecondRecordMap()).thenReturn(perSecondMap);
		
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp(getDateTimeFromOffSet(0));
		
		HttpStatus  httpStatus = ntsServiceImpl.addANewRecord(ntsPostRequest);
		
		assertEquals(HttpStatus.CREATED, httpStatus);
		assertEquals(BigDecimal.valueOf(1123.23), perSecondMap.get(currentTimeSecond).getSum());
		assertEquals(BigDecimal.valueOf(123.23), perSecondMap.get(currentTimeSecond).getMax());
		assertEquals(BigDecimal.valueOf(10.00).setScale(2), perSecondMap.get(currentTimeSecond).getMin());
		assertEquals(BigDecimal.valueOf(1123.23/21L).setScale(2, RoundingMode.HALF_UP), perSecondMap.get(currentTimeSecond).getAvg());
		assertEquals(Long.valueOf(21L), perSecondMap.get(currentTimeSecond).getCount());
	}
	
	
	@Test
	public void addANewRecordWithEmptyMapForCreateTest() throws NTSInvalidException {	
		Map<Long, NTSMinuteStatisticsModel> perSecondMap = new ConcurrentHashMap<>();
		Long currentTimeSecond = Calendar.getInstance().getTimeInMillis()/1000;			
		
		when(ntsDailyStatisticsModel.getPerSecondRecordMap()).thenReturn(perSecondMap);
		
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp(getDateTimeFromOffSet(0));
		
		HttpStatus  httpStatus = ntsServiceImpl.addANewRecord(ntsPostRequest);
		
		assertEquals(HttpStatus.CREATED, httpStatus);
		assertEquals(BigDecimal.valueOf(123.23), perSecondMap.get(currentTimeSecond).getSum());
		assertEquals(BigDecimal.valueOf(123.23), perSecondMap.get(currentTimeSecond).getMax());
		assertEquals(BigDecimal.valueOf(123.23), perSecondMap.get(currentTimeSecond).getMin());
		assertEquals(Long.valueOf(1L), perSecondMap.get(currentTimeSecond).getCount());
	}
	
	
	@Test
	public void addANewRecordWithNonEmptyMapForOlderDateTest() throws NTSInvalidException {	
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp(getDateTimeFromOffSet(70));
		
 		HttpStatus  httpStatus = ntsServiceImpl.addANewRecord(ntsPostRequest);
		
		assertEquals(HttpStatus.NO_CONTENT, httpStatus);
	}
	
	@Test(expected = NTSInvalidException.class)
	public void addANewRecordWithEmptyMapForFutureDateTest() throws NTSInvalidException {	
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("123.23");
		ntsPostRequest.setTimestamp(getDateTimeFromOffSet(-70));
		
		ntsServiceImpl.addANewRecord(ntsPostRequest);		
	}
	
	@Test(expected = NTSInvalidException.class)
	public void addANewRecordInvalidAmountTest() throws NTSInvalidException {	
		
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("ABC.DE");
		ntsPostRequest.setTimestamp(getDateTimeFromOffSet(0));
		
		ntsServiceImpl.addANewRecord(ntsPostRequest);		
	}
	
	@Test(expected = NTSInvalidException.class)
	public void addANewRecordInvalidDateTest() throws NTSInvalidException {	
		
		NTSPostRequest ntsPostRequest = new NTSPostRequest();
		ntsPostRequest.setAmount("100.00");
		ntsPostRequest.setTimestamp("Hello");
		
		ntsServiceImpl.addANewRecord(ntsPostRequest);		
	}

}
