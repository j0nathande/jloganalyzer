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
 * 
 * @author Jonathan Strampp
 *
 */
public class TextParserImpl  implements LogParser {

private static Logger LOG = LoggerFactory.getLogger(TextParserImpl.class);
    
    private String level;
    
    /** Pattern for entry begin */
    private Pattern ENTRY_BEGIN_PATTERN;
    
    public TextParserImpl(String level) {
        this.level = level;
        ENTRY_BEGIN_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) \\[(.*)\\] " + level + "\\s+([^ ]*)\\s+- (.*)$");
    }

    @Override
    public ParseResult parse(File file, String timestamp) {
    	ParseResult parseResult = new ParseResult();
    	
    	Pattern patternFirstEvent = getFirstEventPattern(timestamp);
    	LOG.debug("parse file {} with pattern {}", file, patternFirstEvent);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String lastTimestamp = null;
            boolean eventIsValidByTimestamp = false;
            while((line = br.readLine()) != null) {
            	
            	// matcher for regular event
                Matcher matcherEvent = ENTRY_BEGIN_PATTERN.matcher(line);
                
                // matcher for first event by timestamp
            	Matcher matcherFirstEvent = patternFirstEvent.matcher(line);
                
                // set last analyzed timestamp to new last timestamp
                if (matcherEvent.matches()) {
                	lastTimestamp = matcherEvent.group(1);
                }
                
                if (matcherFirstEvent.matches()) {
            		eventIsValidByTimestamp = true;
            	}
                
                if (eventIsValidByTimestamp && matcherEvent.matches()) {
                    LogEntry entry = new LogEntry(matcherEvent.group(2), matcherEvent.group(3), level, lastTimestamp, matcherEvent.group(4));
                    LOG.debug("Match: " + entry);
                    parseResult.getEntries().add(entry);
                }
            }
        	parseResult.setLastTimestamp(lastTimestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseResult;
    }
    
    
    private Pattern getFirstEventPattern(String timestamp) {
		if (timestamp == null) {
			return ENTRY_BEGIN_PATTERN;
		} else {
			return Pattern.compile(timestamp + " \\[(.*)\\] " + level + "\\s+([^ ]*)\\s+- (.*)$");
		}
	}
    
    
    public String toString() {
        return "TextParserImpl [Level: " + level + "]";
    }

}
