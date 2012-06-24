package org.jloganalyzer.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * This Bean serves as result of parsing a logfile.
 * 
 * @author Jonathan Strampp
 *
 */
public class ParseResult {

	private String lastTimestamp;
	private Long duration;
	private List <LogEntry> entries = new LinkedList<LogEntry>();
	public String getLastTimestamp() {
		// Sinces String 'null' is saved to json-file if value is null.
		if ("null".equals(lastTimestamp)) return null;
		return lastTimestamp;
	}
	public void setLastTimestamp(String lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public List<LogEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<LogEntry> entries) {
		this.entries = entries;
	}
	
	
	public static class LogEntry {
	    private String logger;
	    private String level;
	    private String message;
	    private String timestamp;
	    private String thread;
	    
	    /**
	     * @param thread
	     * @param logger
	     * @param level
	     * @param timestamp
	     * @param message
	     */
	    public LogEntry(String thread, String logger, String level, String timestamp, String message) {
	        this.thread = thread;
	        this.logger = logger;
	        this.level = level;
	        this.timestamp = timestamp;
	        this.message = message;
	    }
	    
	    public String toString() {
	        return "LogEntry[" +
	        		"thread: " + thread + 
	                ", logger: " + logger +
	                ", level: " + level + 
	                ", timestamp: " + timestamp +
	                ", message: " + message +
	                "]";
	    }
	    
	    /**
	     * @return the level
	     */
	    public String getLevel() {
	        return level;
	    }
	    /**
	     * @param level the level to set
	     */
	    public void setLevel(String level) {
	        this.level = level;
	    }
	    /**
	     * @return the message
	     */
	    public String getMessage() {
	        return message;
	    }
	    /**
	     * @param message the message to set
	     */
	    public void setMessage(String message) {
	        this.message = message;
	    }
	    /**
	     * @return the timestamp
	     */
	    public String getTimestamp() {
	        return timestamp;
	    }
	    /**
	     * @param timestamp the timestamp to set
	     */
	    public void setTimestamp(String timestamp) {
	        this.timestamp = timestamp;
	    }
	    /**
	     * @return the thread
	     */
	    public String getThread() {
	        return thread;
	    }
	    /**
	     * @param thread the thread to set
	     */
	    public void setThread(String thread) {
	        this.thread = thread;
	    }
	
	    /**
	     * @return the logger
	     */
	    public String getLogger() {
	        return logger;
	    }
	
	    /**
	     * @param logger the logger to set
	     */
	    public void setLogger(String logger) {
	        this.logger = logger;
	    }
	}
}
