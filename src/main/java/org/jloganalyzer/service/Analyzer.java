package org.jloganalyzer.service;

import java.util.Map;

import org.jloganalyzer.domain.FileAndParser;

/**
 * The Analyzer runs the parsers for each configured logfile.
 * 
 * @author Jonathan Strampp
 *
 */
public interface Analyzer {
    
    /**
     * Analyze the configured files by the parsers.
     */
    public void run();
    
    
    /**
     * Get the configured files and parsers and the id.
     * This is used to verify if an id is valid.
     * 
     * @return The configured map.
     */
    public Map<String, FileAndParser> getId2FileAndParser();
    
}
