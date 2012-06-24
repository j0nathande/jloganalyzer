package org.jloganalyzer.application;

import java.io.IOException;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.jloganalyzer.service.Analyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for serving json-statistics.
 * 
 * @author Jonathan Strampp
 *
 */
@Controller
public class StatisticsServlet extends FileReaderServlet {

	private static Logger LOG = LoggerFactory.getLogger(StatisticsServlet.class);
	
	@Inject
	Analyzer analyzer;
	
	@RequestMapping(value = "/allStatistic/ids.json", produces="application/json")
	public @ResponseBody Set<String> getAllStatisticIds() {
		LOG.debug("getAllStatisticIds");
		return analyzer.getId2FileAndParser().keySet();
	}
	
	/**
	 * Controller for writing the statistics in json-format to the response.
	 * 
	 * @param resp The repsonse to write to.
	 * @param id The id of the statistics-file.
	 * @throws IOException If given id is invalid.
	 */
	@RequestMapping(value = "/{id}.json")
	public void getSingleFileAndParserStatisticsJson(HttpServletResponse resp, @PathVariable String id) throws IOException {
		LOG.debug("getSingleFileAndParserStatisticsJson with id {}", id);
		if (isValidId(id)) {
			resp.setContentType("application/json; charset=UTF-8");
			resp.setHeader("Cache-Control", "no-cache");
			String fileName = id + ".json";
			writeFileToResponse(resp, fileName);
		} else {
			LOG.debug("Id not valid: {}", id);
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private boolean isValidId(String id) {
		return analyzer.getId2FileAndParser().containsKey(id);
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
}
