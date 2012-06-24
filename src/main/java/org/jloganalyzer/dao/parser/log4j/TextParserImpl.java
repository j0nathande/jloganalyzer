package org.jloganalyzer.dao.parser.log4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jloganalyzer.dao.parser.LogParser;
import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.ParseResult.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This parser is for log4j-ConversionPattern: %10d [%t] %-5p %c %x - %m%n
 * Possible entry is: 2012-05-20 16:50:06,180 [main] ERROR org.jloganalyzer.LogGenerator  - Testentry for error test üöäqß with exception java.lang.Exception: testExcepion
 * 
 * @author Jonathan Strampp
 *
 */
public class TextParserImpl extends Log4jParser implements LogParser {

	private static Logger LOG = LoggerFactory.getLogger(TextParserImpl.class);
    
    /** Pattern for entry begin */
    private Pattern ENTRY_BEGIN_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) \\[(.*)\\] (\\w+)\\s* ([^ ]*)\\s+- (.*)$");
    
    public TextParserImpl(String level) {
        this.level = level;
    }

    @Override
    public ParseResult parse(File file, String timestamp) {
    	ParseResult parseResult = new ParseResult();
    	
    	LOG.debug("Parse file {} with pattern {}", file, ENTRY_BEGIN_PATTERN);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String timestampOfCheckedEvent = null;
            
            while((line = br.readLine()) != null) {
            	// matcher for event
            	Matcher matcherEvent = ENTRY_BEGIN_PATTERN.matcher(line);
                if (matcherEvent.matches()) {
                	timestampOfCheckedEvent = matcherEvent.group(1);
                	String loglevelOfCheckedEvent = matcherEvent.group(3);
                	
                	if (isEventValidForTimestamp(timestamp, timestampOfCheckedEvent) 
                			&& isEventValidForLoglevel(loglevelOfCheckedEvent)) {
	                    LogEntry entry = new LogEntry(matcherEvent.group(2), matcherEvent.group(4), loglevelOfCheckedEvent, timestampOfCheckedEvent, matcherEvent.group(5));
	                    LOG.debug("Match: " + entry);
	                    parseResult.getEntries().add(entry);
                	}
                }
            }
        	parseResult.setLastTimestamp(timestampOfCheckedEvent);
        } catch (IOException e) {
            LOG.error("Exception parsing file.", e);
        }
        return parseResult;
    }

    
    public String toString() {
        return "TextParserImpl [Level: " + level + "]";
    }

}
