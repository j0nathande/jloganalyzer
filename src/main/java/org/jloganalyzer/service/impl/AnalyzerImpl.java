package org.jloganalyzer.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jloganalyzer.dao.parser.LogParser;
import org.jloganalyzer.dao.statistics.AllFileAndParsersDao;
import org.jloganalyzer.dao.statistics.SingleFileAndParserDao;
import org.jloganalyzer.domain.FileAndParser;
import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.Statistics;
import org.jloganalyzer.service.Analyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;


public class AnalyzerImpl implements Analyzer {

	SingleFileAndParserDao singleFileAndParserDao;
	
	AllFileAndParsersDao allFileAndParsersDao;
	
    private static Logger LOG = LoggerFactory.getLogger(AnalyzerImpl.class);
    
    private Map<String, FileAndParser> id2FileAndParser = new HashMap<String, FileAndParser>();

	public AnalyzerImpl(Map<String, FileAndParser> map) {
        this.id2FileAndParser = map;
    }

	@Override
    public void run() {
        LOG.info("Analyze {} logfiles", id2FileAndParser.size());
        HashMap<String, ParseResult> allParserResults = new HashMap<String, ParseResult>();
        for (Map.Entry<String, FileAndParser> entry : id2FileAndParser.entrySet()) {
        	String id = entry.getKey();
        	LOG.info("Parse combination with label {}", id);
        	FileAndParser fileAndParser = entry.getValue();
            try {
            	File file = fileAndParser.getResource().getFile();
            	LogParser parser = fileAndParser.getLogParser();
            	LOG.info("Parse file {} using parser {}", file, parser);
            	//measuring elapsed time using Spring StopWatch
                StopWatch watch = new StopWatch();
                watch.start();
            	ParseResult parseResult = parser.parse(file, getLastTimestamp(id));
	            watch.stop();
	            parseResult.setDuration(watch.getTotalTimeMillis());
	            singleFileAndParserDao.persist(id, parseResult);
	            allParserResults.put(id, parseResult);
			} catch (IOException e) {
				LOG.error("File error: ", e);
			}
        }
        allFileAndParsersDao.persist(allParserResults);
    }

	@Override
    public Map<String, FileAndParser> getId2FileAndParser() {
		return id2FileAndParser;
	}
	
	/**
	 * Get the last timestamp which is safed for this id.
	 * 
	 * @param id The id.
	 * @return The timestamp if statistics can be found. Otherwise null.
	 */
	private String getLastTimestamp(String id) {
		String lastTimestamp = null;
		Statistics statsOfFile = singleFileAndParserDao.getStatistics(id);
    	if (statsOfFile != null) {
    		lastTimestamp = statsOfFile.getLastTimestamp();
    		if ("".equals(lastTimestamp)) return null;
    	}
    	return lastTimestamp;
	}

	public void setSingleFileAndParserDao(
			SingleFileAndParserDao singleFileAndParserDao) {
		this.singleFileAndParserDao = singleFileAndParserDao;
	}

	public void setAllFileAndParsersDao(AllFileAndParsersDao allFileAndParsersDao) {
		this.allFileAndParsersDao = allFileAndParsersDao;
	}

}
