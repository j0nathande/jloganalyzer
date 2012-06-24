package org.jloganalyzer.dao.statistics.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jloganalyzer.dao.statistics.SingleFileAndParserDao;
import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.Statistics;
import org.jloganalyzer.domain.Statistics.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DAO for persisting each result to a statistics json-file.
 * 
 * @author Jonathan Strampp
 * 
 */
public class SingleFileAndParserJsonFileDao implements SingleFileAndParserDao {

	private static Logger LOG = LoggerFactory
			.getLogger(SingleFileAndParserJsonFileDao.class);

	private String persistDirectory = "";

	private static final String CURRENT_MODEL_VERSION = "jloganalyzer-0.1";

	ObjectMapper mapper = new ObjectMapper();

	private int maxResultEntries = 40;

	@Override
	public void persist(String id, ParseResult parseResult) {
		persistStatistics(id, parseResult);
	}

	/**
	 * Persist the given results.
	 * 
	 * @param id
	 *            The id of the file and parser.
	 * @param parseResult
	 *            The result to persist.
	 * @return The Statistics-object which was persisted.
	 */
	protected Statistics persistStatistics(String id, ParseResult parseResult) {
		File file = getFile(id);
		Statistics stats = getStatistics(id);
		if (stats == null) {
			stats = new Statistics();
			stats.setModelVersion(CURRENT_MODEL_VERSION);
		}
		addEntryToStatistics(stats, parseResult);
		shrinkToMaxResultEntries(stats);
		writeToFile(file, stats);
		return stats;
	}

	/**
	 * Deletes all older stats-results which exceed maxEntries.
	 * 
	 * @param stats
	 *            The stats-objekt to change.
	 */
	protected void shrinkToMaxResultEntries(Statistics stats) {
		SortedMap<String, Data> results = stats.getResults();
		if (maxResultEntries >= results.size()) {
			return;
		}
		int diff = results.size() - maxResultEntries;
		LOG.debug("{} old result entries have to be deleted.", diff);
		for (int i = 0; i < diff; i++) {
			String keyToRemove = results.firstKey();
			LOG.debug("Removing entry '{}' from map.", keyToRemove);
			results.remove(keyToRemove);
		}
	}

	private void addEntryToStatistics(Statistics stats, ParseResult parseResult) {
		if (stats.getResults() == null) {
			stats.setResults(new TreeMap<String, Data>());
		}
		stats.getResults().put(
				getFormattedDate(new Date()),
				new Data(parseResult.getEntries().size(), parseResult
						.getDuration()));
		stats.setLastTimestamp(parseResult.getLastTimestamp());
	}

	@Override
	public Statistics getStatistics(String id) {
		Statistics stats = null;
		File file = getFile(id);
		if (file.exists()) {
			try {
				stats = mapper.readValue(file, Statistics.class);
			} catch (IOException e) {
				LOG.error("Error getting Statistics for id '" + id + "'", e);
			}
		}
		return stats;
	}

	/**
	 * Write the Statistics-object to the given file.
	 * 
	 * @param file
	 *            The file to write to.
	 * @param stats
	 *            The statistics-object.
	 */
	protected void writeToFile(File file, Statistics stats) {
		try {
			mapper.writeValue(file, stats);
		} catch (IOException e) {
			LOG.error("Error writing to json-file.", e);
		}
	}

	/**
	 * Formats given date to yyyy-MM-dd hh:mm:ss.
	 * 
	 * @param date
	 *            The date to format.
	 * @return The formatted string.
	 */
	protected String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
		return sdf.format(date);
	}

	/**
	 * Filename is <id>.json
	 * 
	 * @param id
	 *            The id to get the File for.
	 * @return The File.
	 */
	private File getFile(String id) {
		String filename = persistDirectory + id + ".json";
		File file = new File(filename);
		LOG.debug("File is {}", file);
		return file;
	}

	public String getPersistDirectory() {
		return persistDirectory;
	}

	public void setPersistDirectory(String persistDirectory) {
		this.persistDirectory = persistDirectory;
	}

	public int getMaxResultEntries() {
		return maxResultEntries;
	}

	public void setMaxResultEntries(int maxResultEntries) {
		this.maxResultEntries = maxResultEntries;
	}

}
