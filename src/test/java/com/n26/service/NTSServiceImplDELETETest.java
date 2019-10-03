package com.n26.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.n26.service.exception.NTSInvalidException;
import com.n26.service.model.NTSMinuteStatisticsModel;
import com.n26.service.model.NTSOverallStatisticsModel;

@RunWith(MockitoJUnitRunner.class)
public class NTSServiceImplDELETETest extends NTSServiceAbstractTest {

	@Test
	public void deleteAllRecordsTest() throws NTSInvalidException {		
		NTSOverallStatisticsModel ntsDailyStatisticsModel = new NTSOverallStatisticsModel();
		Map<Long, NTSMinuteStatisticsModel> perSecondMap = new ConcurrentHashMap<>();
		Long currentTimeSecond = Calendar.getInstance().getTimeInMillis()/1000;
		
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel.setSum(BigDecimal.valueOf(1000.00));
		ntsMinuteStatisticsModel.setMax(BigDecimal.valueOf(100.00));
		ntsMinuteStatisticsModel.setMin(BigDecimal.valueOf(10.00));
		ntsMinuteStatisticsModel.setAvg(BigDecimal.valueOf(10.00));
		ntsMinuteStatisticsModel.setCount(20L);
		
		perSecondMap.put(currentTimeSecond, ntsMinuteStatisticsModel);
		
		ntsDailyStatisticsModel.setPerSecondRecordMap(perSecondMap);
		
		ntsServiceImpl = new NTSServiceImpl(ntsDailyStatisticsModel);
		
		assertFalse(ntsDailyStatisticsModel.getPerSecondRecordMap().isEmpty());
		
		ntsServiceImpl.deleteAllRecords();
		
		assertTrue(ntsDailyStatisticsModel.getPerSecondRecordMap().isEmpty());
	}
}
