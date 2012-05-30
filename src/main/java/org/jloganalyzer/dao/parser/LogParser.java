package org.jloganalyzer.dao.parser;

import java.io.File;

import org.jloganalyzer.domain.ParseResult;


/**
 * Interface for LogParsers.
 * 
 * @author Jonathan Strampp
 *
 */
public interface LogParser {
    
    /**
     * Parse the file.
     * 
     * @param file The file to parse.
     * @param timestamp The timestamp to start the analyis from. If null is given then the timestamp is ignored and the whole file is analysed.
     * @return The found Logentries and meta-data.
     */
    public ParseResult parse(File file, String timestamp);
    
}
