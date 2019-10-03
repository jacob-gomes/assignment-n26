package com.n26.service;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.n26.service.model.NTSOverallStatisticsModel;

@RunWith(MockitoJUnitRunner.class)
public abstract class NTSServiceAbstractTest {
	
	protected NTSServiceImpl ntsServiceImpl;
	
	@Mock
	protected NTSOverallStatisticsModel ntsDailyStatisticsModel;
	
	@Before
	public void init() {		
		ntsServiceImpl = new NTSServiceImpl(ntsDailyStatisticsModel);
	}
	
	@Test
	public void abstractTest() {
		assertTrue(true);
	}
	
	protected String getDateTimeFromOffSet(int offset) {
		Long miliSecondFromEpoch = Calendar.getInstance().getTimeInMillis() - offset*1000;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return formatter.format(miliSecondFromEpoch);
	}
}
