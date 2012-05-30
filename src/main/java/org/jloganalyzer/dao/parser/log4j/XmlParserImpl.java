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
 * This parser is for log4j-xml-layout.
 * 
 * @author Jonathan Strampp
 *
 */
public class XmlParserImpl  implements LogParser {

    private static Logger LOG = LoggerFactory.getLogger(XmlParserImpl.class);
    
    private String level;
    
    /** Pattern for entry begin */
    private Pattern ENTRY_BEGIN_PATTERN;
    
    /** Pattern String for entry message end */
    private String ENTRY_MSG_END = "(\\Q]]></log4j:message>\\E)?";
    
    /** Pattern for entry message begin */
    private Pattern ENTRY_MSG_BEGIN_PATTERN = Pattern.compile("^\\Q<log4j:message><![CDATA[\\E(.*?)" + ENTRY_MSG_END + "$");

    public XmlParserImpl(String level) {
        this.level = level;
        ENTRY_BEGIN_PATTERN = Pattern.compile("^<log4j:event logger=\"(.*?)\" timestamp=\"(\\d*?)\" level=\""+ level +"\" thread=\"(.*?)\">$");
    }

    @Override
    public ParseResult parse(File file, String timestamp) {
    	ParseResult parseResult = new ParseResult();
    	
        Pattern patternFirstEvent = getFirstEventPattern(timestamp);
        LOG.debug("parse file {} with pattern {}", file, patternFirstEvent);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String timestampOfLogentry = null;
            boolean eventIsValidByTimestamp = false;
            while((line = br.readLine()) != null) {
            	
            	// matcher for first event by timestamp
            	Matcher matcherFirstEvent = patternFirstEvent.matcher(line);
            	
                // matcher for regular event
                Matcher matcherEvent = ENTRY_BEGIN_PATTERN.matcher(line);
            	
            	// set last analyzed timestamp to new last timestamp
                if (matcherEvent.matches()) {
                	timestampOfLogentry = matcherEvent.group(2);
                }
                
                if (matcherFirstEvent.matches()) {
            		eventIsValidByTimestamp = true;
            	}
            	
                if (eventIsValidByTimestamp && matcherEvent.matches()) {
            		// read next line:
                    line = br.readLine();
                    if (line != null) {
                    	// Message of event
                        Matcher matcherEventMessage = ENTRY_MSG_BEGIN_PATTERN.matcher(line);
                        if (matcherEventMessage.matches()) {
                            LogEntry entry = new LogEntry(matcherEvent.group(3), matcherEvent.group(1), level, timestampOfLogentry, matcherEventMessage.group(1));
                            parseResult.getEntries().add(entry);
                            LOG.debug("Match: " + entry);
                        }
                    }
            	}
            	parseResult.setLastTimestamp(timestamp);
            }
        	parseResult.setLastTimestamp(timestampOfLogentry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseResult;
    }
    
    private Pattern getFirstEventPattern(String timestamp) {
		if (timestamp == null) {
			return ENTRY_BEGIN_PATTERN;
		} else {
			return Pattern.compile("^<log4j:event logger=\"(.*?)\" timestamp=\""+ timestamp +"\" level=\""+ level +"\" thread=\"(.*?)\">$");
		}
	}

	public String toString() {
        return "XmlParserImpl [Level: " + level + "]";
    }

}
