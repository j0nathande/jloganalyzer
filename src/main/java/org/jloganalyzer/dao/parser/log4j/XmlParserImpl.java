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
public class XmlParserImpl extends Log4jParser implements LogParser {

    private static Logger LOG = LoggerFactory.getLogger(XmlParserImpl.class);
    
    /** Pattern for entry begin */
    private Pattern ENTRY_BEGIN_PATTERN = Pattern.compile("^<log4j:event logger=\"(.*?)\" timestamp=\"(\\d*?)\" level=\"(.*?)\" thread=\"(.*?)\">$");
    
    /** Pattern String for entry message end */
    private String ENTRY_MSG_END = "(\\Q]]></log4j:message>\\E)?";
    
    /** Pattern for entry message begin */
    private Pattern ENTRY_MSG_BEGIN_PATTERN = Pattern.compile("^\\Q<log4j:message><![CDATA[\\E(.*?)" + ENTRY_MSG_END + "$");

    public XmlParserImpl(String level) {
        super.level = level;
    }

    @Override
    public ParseResult parse(File file, String timestamp) {
    	ParseResult parseResult = new ParseResult();
    	
        LOG.debug("parse file {} with pattern {}", file, ENTRY_BEGIN_PATTERN);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String timestampOfCheckedEvent = null;
            
            while((line = br.readLine()) != null) {
                // matcher for event
                Matcher matcherEvent = ENTRY_BEGIN_PATTERN.matcher(line);
                if (matcherEvent.matches()) {
                	
                    timestampOfCheckedEvent = matcherEvent.group(2);
                	String loglevelOfCheckedEvent = matcherEvent.group(3);
                	
                	if (isEventValidForTimestamp(timestamp, timestampOfCheckedEvent) 
                			&& isEventValidForLoglevel(loglevelOfCheckedEvent)) {
	            		// read next line:
	                    line = br.readLine();
	                    if (line != null) {
	                    	// Message of event
	                        Matcher matcherEventMessage = ENTRY_MSG_BEGIN_PATTERN.matcher(line);
	                        
	                        if (matcherEventMessage.matches()) {
	                            LogEntry entry = new LogEntry(matcherEvent.group(4), matcherEvent.group(1), loglevelOfCheckedEvent, timestampOfCheckedEvent, matcherEventMessage.group(1));
	                            parseResult.getEntries().add(entry);
	                            LOG.debug("Match: " + entry);
	                        }
	                    }
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
        return "XmlParserImpl [Level: " + level + "]";
    }

}
