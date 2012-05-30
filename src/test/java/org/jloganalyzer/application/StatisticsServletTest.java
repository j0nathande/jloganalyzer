package org.jloganalyzer.application;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.jloganalyzer.domain.FileAndParser;
import org.jloganalyzer.service.Analyzer;
import org.jloganalyzer.service.impl.AnalyzerImpl;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class StatisticsServletTest {
	
	@Test
	public void testValidFileAndParserStatisticsJson() throws IOException {
		StatisticsServlet cudSpy = spy(new StatisticsServlet());
		cudSpy.setPersistDirectory("src/test/resources/testPersistDir/");
		Map<String, FileAndParser> id2parser = new HashMap<String, FileAndParser>();
		id2parser.put("Logfile 1", null);
		Analyzer analyzer = new AnalyzerImpl(id2parser);
		cudSpy.setAnalyzer(analyzer);
		
		HttpServletResponse resp = new MockHttpServletResponse();
		cudSpy.getSingleFileAndParserStatisticsJson(resp, "Logfile 1");
		verify(cudSpy).writeFileToResponse(resp, "Logfile 1.json");
	}
	
	@Test
	public void testInvalidFileAndParserStatisticsJson() throws IOException {
		StatisticsServlet cudSpy = spy(new StatisticsServlet());
		cudSpy.setPersistDirectory("src/test/resources/testPersistDir/");
		Map<String, FileAndParser> id2parser = new HashMap<String, FileAndParser>();
		id2parser.put("Logfile 1", null);
		Analyzer analyzer = new AnalyzerImpl(id2parser);
		cudSpy.setAnalyzer(analyzer);
		
		HttpServletResponse resp = new MockHttpServletResponse();
		cudSpy.getSingleFileAndParserStatisticsJson(resp, "Logfile asdf");
		verify(cudSpy, never()).writeFileToResponse(resp, "Logfile asdf.json");
	}
}

