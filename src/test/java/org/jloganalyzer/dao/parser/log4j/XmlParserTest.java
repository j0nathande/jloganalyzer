package org.jloganalyzer.dao.parser.log4j;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jloganalyzer.dao.parser.log4j.XmlParserImpl;
import org.jloganalyzer.domain.ParseResult;
import org.junit.Test;



public class XmlParserTest {

    
    @Test
    public void testParseWholeFile() {
        
        XmlParserImpl parser = new XmlParserImpl("INFO");
        ParseResult parseResult = parser.parse(new File("src/test/resources/log4j/example.xml"), null);
        System.out.println(parseResult.getEntries().size());
        System.out.println(parseResult.getEntries().get(0));
        assertTrue(parseResult.getEntries().size() == 1000);
        
    }
    
}
