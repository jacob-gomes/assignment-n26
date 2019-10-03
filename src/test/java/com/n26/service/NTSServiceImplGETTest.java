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

import com.n26.controller.model.NTSGetResponse;
import com.n26.service.model.NTSMinuteStatisticsModel;

@RunWith(MockitoJUnitRunner.class)
public class NTSServiceImplGETTest extends NTSServiceAbstractTest{
	@Test
	public void getMinuteStatisticsForWithMapWithNoRecordsTest() {
		Map<Long, NTSMinuteStatisticsModel> perSecondMap = new ConcurrentHashMap<>();
		
		when(ntsDailyStatisticsModel.getPerSecondRecordMap()).thenReturn(perSecondMap);
		
		NTSGetResponse ntsGetResponse = ntsServiceImpl.getMinuteStatistics();
		
		assertEquals("0.00", ntsGetResponse.getSum());
		assertEquals("0.00", ntsGetResponse.getMax());
		assertEquals("0.00", ntsGetResponse.getMin());
		assertEquals("0.00", ntsGetResponse.getAvg());
		assertEquals(Long.valueOf(0L), ntsGetResponse.getCount());
	}
	
	@Test
	public void getMinuteStatisticsForWithMapWith3RecordsForMinuteTest() {
		Map<Long, NTSMinuteStatisticsModel> perSecondMap = new ConcurrentHashMap<>();
		Long currentTimeSecond = Calendar.getInstance().getTimeInMillis()/1000;
		
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel1 = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel1.setSum(BigDecimal.valueOf(1000.23));
		ntsMinuteStatisticsModel1.setMax(BigDecimal.valueOf(100.21));
		ntsMinuteStatisticsModel1.setMin(BigDecimal.valueOf(10.00));
		ntsMinuteStatisticsModel1.setCount(20L);
		
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel2 = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel2.setSum(BigDecimal.valueOf(900.23));
		ntsMinuteStatisticsModel2.setMax(BigDecimal.valueOf(99.21));
		ntsMinuteStatisticsModel2.setMin(BigDecimal.valueOf(11.00));
		ntsMinuteStatisticsModel2.setCount(20L);
		
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel3 = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel3.setSum(BigDecimal.valueOf(800.23));
		ntsMinuteStatisticsModel3.setMax(BigDecimal.valueOf(98.21));
		ntsMinuteStatisticsModel3.setMin(BigDecimal.valueOf(12.00));
		ntsMinuteStatisticsModel3.setCount(20L);
		
		perSecondMap.put(currentTimeSecond, ntsMinuteStatisticsModel1);	
		perSecondMap.put(currentTimeSecond-1, ntsMinuteStatisticsModel2);	
		perSecondMap.put(currentTimeSecond-2, ntsMinuteStatisticsModel3);	
		
		when(ntsDailyStatisticsModel.getPerSecondRecordMap()).thenReturn(perSecondMap);
		
		NTSGetResponse ntsGetResponse = ntsServiceImpl.getMinuteStatistics();
		
		assertEquals("2700.69", ntsGetResponse.getSum());
		assertEquals("100.21", ntsGetResponse.getMax());
		assertEquals("10.00", ntsGetResponse.getMin());
		assertEquals(BigDecimal.valueOf(2700.69/60L).setScale(2, RoundingMode.HALF_UP).toString(), ntsGetResponse.getAvg());
		assertEquals(Long.valueOf(60L), ntsGetResponse.getCount());
	}
}
