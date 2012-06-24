package org.jloganalyzer.dao.parser.log4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jloganalyzer.dao.parser.log4j.TextParserImpl;
import org.jloganalyzer.domain.ParseResult;
import org.junit.Test;


public class TextParserTest {
    
	@Test
    public void testParseWholeFile_TRACE() {
        TextParserImpl parser = new TextParserImpl("TRACE");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_DEBUG() {
        TextParserImpl parser = new TextParserImpl("DEBUG");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_INFO() {
        TextParserImpl parser = new TextParserImpl("INFO");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_WARN() {
        TextParserImpl parser = new TextParserImpl("WARN");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_ERROR() {
        TextParserImpl parser = new TextParserImpl("ERROR");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
    
    @Test
    public void testStartAnalysisFromTimestamp() {
    	TextParserImpl parser = new TextParserImpl("DEBUG");
    	// Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), "2012-05-20 16:50:06,166");
        assertEquals(2, parseResult.getEntries().size());
    }
    
    @Test
    public void testStartAnalysisFromTimestampWithLastEntry() {
    	TextParserImpl parser = new TextParserImpl("DEBUG");
    	// Last Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), "2012-05-20 16:50:06,176");
        assertEquals(0, parseResult.getEntries().size());
    }
    
    @Test
    public void testLastTimestampLastEntrySameLoglevel() {
    	TextParserImpl parser = new TextParserImpl("ERROR");
    	// Last Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), "2012-05-20 16:50:06,176");
        assertEquals("2012-05-20 16:50:06,180", parseResult.getLastTimestamp());
    }
    
    @Test
    public void testLastTimestampLastEntryDifferentLoglevel() {
    	TextParserImpl parser = new TextParserImpl("WARN");
    	// Last Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.log"), "2012-05-20 16:50:06,176");
        assertEquals("2012-05-20 16:50:06,180", parseResult.getLastTimestamp());
    }
    
    @Test
    public void testIsEventValidForTimestamp() {
    	TextParserImpl parser = new TextParserImpl("DEBUG");
    	
    	assertTrue(parser.isEventValidForTimestamp("2012-05-20 16:50:06,176", "2012-05-20 16:50:06,177"));
    	assertTrue(parser.isEventValidForTimestamp(null, "2012-05-20 16:50:06,177"));
    	
    	assertFalse(parser.isEventValidForTimestamp("2012-05-20 16:50:06,177", "2012-05-20 16:50:06,177"));
    	assertFalse(parser.isEventValidForTimestamp("2012-05-20 16:50:06,177", "2012-05-20 16:50:06,176"));
    	assertFalse(parser.isEventValidForTimestamp("2012-05-20 16:50:06,177", null));
    }
    
}
