package org.jloganalyzer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogGenerator {
	
	private static Logger LOG = LoggerFactory.getLogger(LogGenerator.class);
	
	@Test
	public void generateLog() {
		for(int i = 0; i<1000; i++) {
			LOG.trace("Testentry for trace {}", "test üöäqß");
			LOG.debug("Testentry for debug {}", "test üöäqß");
			LOG.info("Testentry for info {}", "test üöäqß");
			LOG.warn("Testentry for warn {}", "test üöäqß");
			LOG.error("Testentry for error {} with exception {}", "test üöäqß", new Exception("testExcepion"));
		}
	}
	

}
