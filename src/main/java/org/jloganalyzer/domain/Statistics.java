package org.jloganalyzer.domain;

import java.util.Map;

/**
 * This Bean is used to hold the statistics of each parsing of a file.
 * 
 * @author Jonathan Strampp
 *
 */
public class Statistics {
	private String modelVersion;
	private String lastTimestamp;
	private Map<String, Data> results;
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
	public Map<String, Data> getResults() {
		return results;
	}
	public void setResults(Map<String, Data> results) {
		this.results = results;
	}
	public String getLastTimestamp() {
		return lastTimestamp;
	}
	public void setLastTimestamp(String lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}
	
	public static class Data {
		private Integer quantity;
		private Long duration;
		public Data() {}
		public Data(Integer quantity, Long duration) {
			this.quantity = quantity;
			this.duration = duration;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		public Long getDuration() {
			return duration;
		}
		public void setDuration(Long duration) {
			this.duration = duration;
		}
	}
}
