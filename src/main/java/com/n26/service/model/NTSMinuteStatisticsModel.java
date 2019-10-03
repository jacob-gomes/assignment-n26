package com.n26.service.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Model to publish record Statistics
 * @author Jacob
 *
 */
public class NTSMinuteStatisticsModel {
		
	private BigDecimal sum;
	
	private BigDecimal avg;
	
	private BigDecimal max;
	
	private BigDecimal min;
	
	private Long count;
	
	private Long timeStampSeconds;
	
	/**
	 * Check whether the model isEmpty
	 * @return
	 */
	private boolean isEmpty() {
		return null == this.sum
				&& null == this.min
				&& null == this.max
				&& null == this.avg
				&& null == this.count;
	}
	/**
	 * Update the old value merging with new value
	 * @param newNTSMinuteStatistics
	 */
	public void update(NTSMinuteStatisticsModel newNTSMinuteStatistics) {

		if(!newNTSMinuteStatistics.isEmpty()) {
			if(this.isEmpty()) {
				this.sum = newNTSMinuteStatistics.getSum().setScale(2,  RoundingMode.HALF_UP);
				this.max = newNTSMinuteStatistics.getMax().setScale(2,  RoundingMode.HALF_UP);
				this.min = newNTSMinuteStatistics.getMin().setScale(2,  RoundingMode.HALF_UP);
				this.count = newNTSMinuteStatistics.getCount();				
			}else {
				this.sum = this.sum.add(newNTSMinuteStatistics.getSum()).setScale(2,  RoundingMode.HALF_UP);
				this.max = this.max.max(newNTSMinuteStatistics.getMax()).setScale(2,  RoundingMode.HALF_UP);
				this.min = this.min.min(newNTSMinuteStatistics.getMin()).setScale(2,  RoundingMode.HALF_UP);
				this.count = this.count + newNTSMinuteStatistics.getCount();
			}
			this.avg = this.sum.divide(BigDecimal.valueOf(this.count), 2,  RoundingMode.HALF_UP);
		}

	}
		
	/**
	 * @return the sum
	 */
	public BigDecimal getSum() {
		return sum;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

	/**
	 * @return the avg
	 */
	public BigDecimal getAvg() {
		return avg;
	}

	/**
	 * @param avg the avg to set
	 */
	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}

	/**
	 * @return the max
	 */
	public BigDecimal getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(BigDecimal max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public BigDecimal getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(BigDecimal min) {
		this.min = min;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	public Long getTimeStampSeconds() {
		return timeStampSeconds;
	}

	public void setTimeStampSeconds(Long timeStampSeconds) {
		this.timeStampSeconds = timeStampSeconds;
	}	
}
