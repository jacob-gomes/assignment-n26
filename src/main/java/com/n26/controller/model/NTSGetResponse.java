package com.n26.controller.model;
/**
 * A transfer object that publishes the record to GET
 * @author Jacob
 *
 */
public class NTSGetResponse {
	private String sum;
	
	private String avg;
	
	private String max;
	
	private String min;
	
	private Long count;

	/**
	 * @return the sum
	 */
	public String getSum() {
		return sum;
	}

	/**
	 * @param sum the sum to set
	 */
	public void setSum(String sum) {
		this.sum = sum;
	}

	/**
	 * @return the avg
	 */
	public String getAvg() {
		return avg;
	}

	/**
	 * @param avg the avg to set
	 */
	public void setAvg(String avg) {
		this.avg = avg;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(String min) {
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
}
