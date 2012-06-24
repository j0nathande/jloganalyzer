package org.jloganalyzer.dao.statistics.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.ParseResult.LogEntry;
import org.jloganalyzer.domain.Statistics;
import org.jloganalyzer.domain.Statistics.Data;
import org.junit.Test;

public class SingleFileAndParserJsonFileDaoTest {
	
	@Test
	public void testPersistNewStatistics() {
		
		SingleFileAndParserJsonFileDao cudSpy = spy(new SingleFileAndParserJsonFileDao());
		doNothing().when(cudSpy).writeToFile(isA(File.class), isA(Statistics.class)); // do not write file to disk
		
		final Long duration = 1234L;
		final String timestamp = "23452345";
		
		ParseResult parseResult = new ParseResult();
		parseResult.setDuration(duration);
		parseResult.setLastTimestamp(timestamp);
		parseResult.getEntries().add(getDummyEntry());
		parseResult.getEntries().add(getDummyEntry());
		parseResult.getEntries().add(getDummyEntry());
		
		// new Statistics / no existing
		Statistics stats = cudSpy.persistStatistics("test 1", parseResult);
		
		assertEquals(timestamp, stats.getLastTimestamp());
		assertEquals(1, stats.getResults().size());
		
		Map<String, Data> results = stats.getResults();
		for (Map.Entry<String, Data> entry : results.entrySet()) {
			assertEquals(3, entry.getValue().getQuantity());
			assertEquals(duration, entry.getValue().getDuration());
		}
	}
	
	@Test
	public void testPersistAvailableStatistics() {
		
		SingleFileAndParserJsonFileDao cudSpy = spy(new SingleFileAndParserJsonFileDao());
		cudSpy.setPersistDirectory("src/test/resources/testPersistDir/");
		doNothing().when(cudSpy).writeToFile(isA(File.class), isA(Statistics.class)); // do not write file to disk
		
		final Long duration = 1234L;
		final String timestamp = "23452345";
		
		ParseResult parseResult = new ParseResult();
		parseResult.setDuration(duration);
		parseResult.setLastTimestamp(timestamp);
		parseResult.getEntries().add(getDummyEntry());
		parseResult.getEntries().add(getDummyEntry());
		
		// "src/test/resources/testPersistDir/Logfile 1.json" is existing Statistics with one entry
		Statistics stats = cudSpy.persistStatistics("Logfile 1", parseResult);
		
		assertEquals(timestamp, stats.getLastTimestamp());
		assertEquals(2, stats.getResults().size()); // second entry

		Map<String, Data> results = stats.getResults();
		boolean containsNewEntry = false;
		for (Map.Entry<String, Data> entry : results.entrySet()) {
			if(entry.getValue().getQuantity() == 2 
					&& entry.getValue().getDuration() == duration) {
				containsNewEntry = true;
			}
		}
		assertTrue(containsNewEntry);
	}
	
	
	@Test
	public void testGetAvailableStatistics() {
		SingleFileAndParserJsonFileDao cud = new SingleFileAndParserJsonFileDao();
		cud.setPersistDirectory("src/test/resources/testPersistDir/");
		
		// "src/test/resources/testPersistDir/Logfile 1.json" is existing Statistics with one entry
		Statistics stats = cud.getStatistics("Logfile 1");
		
		assertEquals("2012-05-26 08:45:42,035", stats.getLastTimestamp());
		assertEquals(1, stats.getResults().size()); // second entry

		Map<String, Data> results = stats.getResults();
		boolean containsOldEntry = false;
		for (Map.Entry<String, Data> entry : results.entrySet()) {
			if(entry.getValue().getQuantity() == 1 
					&& entry.getValue().getDuration() == 24L) {
				containsOldEntry = true;
			}
		}
		assertTrue(containsOldEntry);
	}
	
	@Test
	public void testGetUnavailableStatistics() {
		SingleFileAndParserJsonFileDao cud = new SingleFileAndParserJsonFileDao();
		cud.setPersistDirectory("src/test/resources/testPersistDir/");
		// "src/test/resources/testPersistDir/Logfile 1.json" is existing Statistics with one entry
		Statistics stats = cud.getStatistics("adsfadsf");
		assertNull(stats);
	}
	
	@Test
	public void testFormattedDate() {
		SingleFileAndParserJsonFileDao cud = new SingleFileAndParserJsonFileDao();
		Date date = new Date(1338712659652L);
		assertEquals("2012-06-03 10:37:39", cud.getFormattedDate(date));
	}
	
	@Test
	public void testShrinkToMaxResultEntries() {
		SingleFileAndParserJsonFileDao cud = new SingleFileAndParserJsonFileDao();
		cud.setMaxResultEntries(2);
		Statistics stats = new Statistics();
		SortedMap<String, Data> results = new TreeMap<String, Statistics.Data>();
		results.put("2012-06-03 10:37:39", null); // entry to remove
		results.put("2012-06-03 10:38:39", null);
		results.put("2012-06-01 10:37:39", null); // entry to remove
		results.put("2012-06-05 10:37:39", null);
		stats.setResults(results);
		
		assertEquals(4, stats.getResults().size());
		cud.shrinkToMaxResultEntries(stats);
		assertEquals(2, stats.getResults().size());
		assertFalse(stats.getResults().containsKey("2012-06-03 10:37:39"));
		assertFalse(stats.getResults().containsKey("2012-06-01 10:37:39"));
	}
	
	@Test
	public void testNoShrinkToMaxResultEntries() {
		SingleFileAndParserJsonFileDao cud = new SingleFileAndParserJsonFileDao();
		cud.setMaxResultEntries(4);
		Statistics stats = new Statistics();
		SortedMap<String, Data> results = new TreeMap<String, Statistics.Data>();
		results.put("2012-06-03 10:37:39", null);
		results.put("2012-06-03 10:38:39", null);
		results.put("2012-06-01 10:37:39", null);
		results.put("2012-06-05 10:37:39", null);
		stats.setResults(results);
		
		assertEquals(4, stats.getResults().size());
		cud.shrinkToMaxResultEntries(stats);
		assertEquals(4, stats.getResults().size());
	}
	
	private LogEntry getDummyEntry() {
		return new LogEntry("tread-1", "org.jloganalyzer.dao.statistics.impl.AllFileAndParsersAtomFileTest", "ERROR", "4256456", "testmessage1");
	}
		
}