package org.jloganalyzer.dao.parser.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class contains common methods for parser-impls of log4j-files.
 * 
 * @author Jonathan Strampp
 *
 */
public class Log4jParser {
	
	private static Logger LOG = LoggerFactory.getLogger(Log4jParser.class);
	
	protected String level;
	
	/**
     * Returns true is loglevel is the same as the level the parser is configured with.
     * 
     * @param loglevelOfCheckedEvent
     * @return true if the loglevels are the same.
     */
    protected boolean isEventValidForLoglevel(String loglevelOfCheckedEvent) {
    	LOG.debug("isEventValidForLoglevel {}? Checking {}", level, loglevelOfCheckedEvent);
    	if (level == null) return false;
    	if (loglevelOfCheckedEvent == null) return false;
    	return level.equalsIgnoreCase(loglevelOfCheckedEvent);
	}

	/**
     * Returns true if timestampToCheck is smaller than timestampReference.
     * If timestampReference is null true is returned.
     * 
     * @param timestampReference
     * @param timestampToCheck
     * @return True if event is valid.
     */
    protected boolean isEventValidForTimestamp(String timestampReference, String timestampToCheck) {
    	LOG.debug("isEventValidForTimestamp {}? Checking {}", timestampReference, timestampToCheck);
    	if (timestampReference == null) return true;
    	if (timestampToCheck == null) return false;
    	int result = timestampToCheck.compareTo(timestampReference);
    	return result > 0;
	}
    
	public String toString() {
        return "XmlParserImpl [Level: " + level + "]";
    }

}
