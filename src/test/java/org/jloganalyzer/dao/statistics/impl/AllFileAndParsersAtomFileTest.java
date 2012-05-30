package org.jloganalyzer.dao.statistics.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.ParseResult.LogEntry;
import org.junit.Test;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class AllFileAndParsersAtomFileTest {
	
	@Test
	public void test() {
		AllFileAndParsersAtomFile cudSpy = spy(new AllFileAndParsersAtomFile());
		doNothing().when(cudSpy).writeFeedToFile(isA(SyndFeed.class), anyString()); // do not write file to disk
		
		HashMap<String, ParseResult> allParserResults = new HashMap<String, ParseResult>();
		ParseResult parseResult1 = new ParseResult();
		parseResult1.setDuration(1234L);
		parseResult1.setLastTimestamp("5234455");
		parseResult1.getEntries().add(getDummyEntry());
		parseResult1.getEntries().add(getDummyEntry());
		parseResult1.getEntries().add(getDummyEntry());

		// Every parser has the three parseResults.
		allParserResults.put("test 1", parseResult1);
		allParserResults.put("test 2", parseResult1);
		
		SyndFeed feed = cudSpy.persistFeed(allParserResults);
		verify(cudSpy).writeFeedToFile(isA(SyndFeed.class), anyString());
		assertEquals(2, feed.getEntries().size());
	}
	
	@Test
	public void testMaxEntriesInDetail() {
		AllFileAndParsersAtomFile cudSpy = spy(new AllFileAndParsersAtomFile());
		doNothing().when(cudSpy).writeFeedToFile(isA(SyndFeed.class), anyString()); // do not write file to disk
		
		cudSpy.setMaxEntriesInDetail(2);
		
		HashMap<String, ParseResult> allParserResults = new HashMap<String, ParseResult>();
		ParseResult parseResult0 = new ParseResult();
		parseResult0.setDuration(1234L);
		parseResult0.setLastTimestamp("5234455");
		parseResult0.getEntries().add(getDummyEntry());
		parseResult0.getEntries().add(getDummyEntry());
		parseResult0.getEntries().add(getDummyEntry());
		allParserResults.put("test 1", parseResult0);
		
		ParseResult parseResult1 = new ParseResult();
		parseResult1.setDuration(1234L);
		parseResult1.setLastTimestamp("5234455");
		parseResult1.getEntries().add(getDummyEntry());
		parseResult1.getEntries().add(getDummyEntry());
		allParserResults.put("test 2", parseResult1);
		
		SyndFeed feed = cudSpy.persistFeed(allParserResults);
		verify(cudSpy).writeFeedToFile(isA(SyndFeed.class), anyString());
		assertEquals(2, feed.getEntries().size());
		
		
		SyndEntry entry0 = (SyndEntry) feed.getEntries().get(0);
		assertFalse(entry0.getDescription().getValue().endsWith(AllFileAndParsersAtomFile.DESCRIPTION_ENDING_MAX_ENTRIES));
		
		SyndEntry entry1 = (SyndEntry) feed.getEntries().get(1);
		assertTrue(entry1.getDescription().getValue().endsWith(AllFileAndParsersAtomFile.DESCRIPTION_ENDING_MAX_ENTRIES));
	}
	
	@Test
	public void testNoResults() {
		AllFileAndParsersAtomFile cudSpy = spy(new AllFileAndParsersAtomFile());
		doNothing().when(cudSpy).writeFeedToFile(isA(SyndFeed.class), anyString()); // do not write file to disk
		
		cudSpy.setMaxEntriesInDetail(2);
		
		HashMap<String, ParseResult> allParserResults = new HashMap<String, ParseResult>();
		
		SyndFeed feed = cudSpy.persistFeed(allParserResults);
		verify(cudSpy).writeFeedToFile(isA(SyndFeed.class), anyString());
		assertEquals(0, feed.getEntries().size());
		
		assertNotNull(feed.getTitle());
	}
	

	private LogEntry getDummyEntry() {
		return new LogEntry("tread-1", "org.jloganalyzer.dao.statistics.impl.AllFileAndParsersAtomFileTest", "ERROR", "4256456", "testmessage1");
	}
	
}