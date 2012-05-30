package org.jloganalyzer.application;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for serving atom-feed of the last run.
 * 
 * @author Jonathan Strampp
 *
 */
@Controller
public class LastResultsServlet extends FileReaderServlet {

	private static Logger LOG = LoggerFactory.getLogger(LastResultsServlet.class);
	
	/**
	 * Controller for writing the atom.xml to the response.
	 * 
	 * @param resp The response to write to.
	 * @throws IOException If atom.xml is not found.
	 */
	@RequestMapping(value = "/atom.xml")
	public void getAllFileAndParsersAtom(HttpServletResponse resp) throws IOException {
		LOG.debug("getAllFileAndParsersAtom");
		resp.setContentType("application/xml; charset=UTF-8");
		resp.setHeader("Cache-Control", "no-cache");
		String fileName = "atom.xml";
		writeFileToResponse(resp, fileName);
	}
	
}
