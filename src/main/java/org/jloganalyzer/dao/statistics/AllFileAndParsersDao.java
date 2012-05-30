package org.jloganalyzer.dao.statistics;

import java.util.HashMap;

import org.jloganalyzer.domain.ParseResult;

/**
 * DAO for persisting the overall result of parsing.
 * 
 * @author Jonathan Strampp
 *
 */
public interface AllFileAndParsersDao {
	
	/**
	 * Persist the given results.
	 * 
	 * @param allParserResults The overall result to persist.
	 */
	public void persist(HashMap<String, ParseResult> allParserResults);

}
