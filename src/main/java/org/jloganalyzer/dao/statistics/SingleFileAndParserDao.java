package org.jloganalyzer.dao.statistics;

import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.Statistics;

/**
 * DAO for persisting and accessing one file-parsing-result.
 * 
 * @author Jonathan Strampp
 *
 */
public interface SingleFileAndParserDao {
	
	/**
	 * Persist the result for given id.
	 * 
	 * @param id The id for which this result is saved.
	 * @param parseResult The result.
	 */
	public void persist(String id, ParseResult parseResult);
	
	/**
	 * Get the statistics for given id.
	 * 
	 * @param id The id.
	 * @return null if no Statistics where found. Otherwise the Statistics.
	 */
	public Statistics getStatistics(String id);
	
}
