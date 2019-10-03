package com.n26.service.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NTSMinuteStatisticsModelTest {
	
	private NTSMinuteStatisticsModel ntsMinuteStatisticsModel;
	
	@Before
	public void init() {
		ntsMinuteStatisticsModel = new NTSMinuteStatisticsModel();
	}
	
	@Test
	public void updateNTSMinuteStatisticsModelPositiveTest() {
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel1 = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel1.setSum(BigDecimal.valueOf(1000.12));
		ntsMinuteStatisticsModel1.setMax(BigDecimal.valueOf(100.34));
		ntsMinuteStatisticsModel1.setMin(BigDecimal.valueOf(9.45));
		ntsMinuteStatisticsModel1.setCount(20L);
		
		ntsMinuteStatisticsModel.setSum(BigDecimal.valueOf(2000.23));
		ntsMinuteStatisticsModel.setMax(BigDecimal.valueOf(101.12));
		ntsMinuteStatisticsModel.setMin(BigDecimal.valueOf(10.23));
		ntsMinuteStatisticsModel.setCount(20L);
		
		ntsMinuteStatisticsModel.update(ntsMinuteStatisticsModel1);
		
		assertEquals(BigDecimal.valueOf(3000.35), ntsMinuteStatisticsModel.getSum());
		assertEquals(BigDecimal.valueOf(101.12), ntsMinuteStatisticsModel.getMax());
		assertEquals(BigDecimal.valueOf(9.45), ntsMinuteStatisticsModel.getMin());
		assertEquals(BigDecimal.valueOf(3000.35/40).setScale(2, RoundingMode.HALF_UP), ntsMinuteStatisticsModel.getAvg());
		assertEquals(Long.valueOf(40L), ntsMinuteStatisticsModel.getCount());
	}
	
	@Test
	public void updateNTSMinuteStatisticsModelPositiveWithEmptyTest() {
		NTSMinuteStatisticsModel ntsMinuteStatisticsModel1 = new NTSMinuteStatisticsModel();
		ntsMinuteStatisticsModel1.setSum(BigDecimal.valueOf(1000.12));
		ntsMinuteStatisticsModel1.setMax(BigDecimal.valueOf(100.34));
		ntsMinuteStatisticsModel1.setMin(BigDecimal.valueOf(9.45));
		ntsMinuteStatisticsModel1.setCount(20L);
		
		ntsMinuteStatisticsModel.update(ntsMinuteStatisticsModel1);
		
		assertEquals(BigDecimal.valueOf(1000.12), ntsMinuteStatisticsModel.getSum());
		assertEquals(BigDecimal.valueOf(100.34), ntsMinuteStatisticsModel.getMax());
		assertEquals(BigDecimal.valueOf(9.45), ntsMinuteStatisticsModel.getMin());
		assertEquals(BigDecimal.valueOf(1000.12/20).setScale(2, RoundingMode.HALF_UP), ntsMinuteStatisticsModel.getAvg());
		assertEquals(Long.valueOf(20L), ntsMinuteStatisticsModel.getCount());
	}
}
