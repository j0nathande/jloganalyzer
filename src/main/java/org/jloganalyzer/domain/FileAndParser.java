package org.jloganalyzer.domain;

import org.jloganalyzer.dao.parser.LogParser;
import org.springframework.core.io.Resource;

/**
 * This Bean is used for connecting a file to a parser.
 * 
 * @author Jonathan Strampp
 *
 */
public class FileAndParser {

	private Resource resource;
	private LogParser logParser;
	
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public LogParser getLogParser() {
		return logParser;
	}
	public void setLogParser(LogParser logParser) {
		this.logParser = logParser;
	}
}
