package org.jloganalyzer.dao.parser.log4j;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.jloganalyzer.dao.parser.log4j.XmlParserImpl;
import org.jloganalyzer.domain.ParseResult;
import org.junit.Test;



public class XmlParserTest {

	@Test
    public void testParseWholeFile_TRACE() {
        XmlParserImpl parser = new XmlParserImpl("TRACE");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_DEBUG() {
        XmlParserImpl parser = new XmlParserImpl("DEBUG");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_INFO() {
        XmlParserImpl parser = new XmlParserImpl("INFO");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_WARN() {
        XmlParserImpl parser = new XmlParserImpl("WARN");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
	
	@Test
    public void testParseWholeFile_ERROR() {
        XmlParserImpl parser = new XmlParserImpl("ERROR");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
    
    @Test
    public void testParseWholeFile() {
        XmlParserImpl parser = new XmlParserImpl("INFO");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        assertEquals(1000, parseResult.getEntries().size());
    }
    
    @Test
    public void testStartAnalysisFromTimestamp() {
    	XmlParserImpl parser = new XmlParserImpl("INFO");
    	// Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), "1337525406167");
        assertEquals(2, parseResult.getEntries().size());
    }
    
    @Test
    public void testStartAnalysisFromTimestampWithLastEntry() {
    	XmlParserImpl parser = new XmlParserImpl("INFO");
    	// Last Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), "1337525406176");
        assertEquals(0, parseResult.getEntries().size());
    }
    
    @Test
    public void testLastTimestampLastEntrySameLoglevel() {
    	XmlParserImpl parser = new XmlParserImpl("ERROR");
    	// Last Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), "1337525406175");
        assertEquals("1337525406180", parseResult.getLastTimestamp());
    }
    
    @Test
    public void testLastTimestampLastEntryDifferentLoglevel() {
    	XmlParserImpl parser = new XmlParserImpl("WARN");
    	// Last Logentry with this timestamp must not be included in parseResult.
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), "1337525406175");
        assertEquals("1337525406180", parseResult.getLastTimestamp());
    }
    
}
