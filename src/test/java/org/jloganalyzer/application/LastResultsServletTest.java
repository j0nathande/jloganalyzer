package org.jloganalyzer.application;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

public class LastResultsServletTest {

	@Test
	public void testValidAtomRequest() throws IOException {
		LastResultsServlet cudSpy = spy(new LastResultsServlet());
		cudSpy.setPersistDirectory("src/test/resources/testPersistDir/");
		HttpServletResponse resp = new MockHttpServletResponse();
		cudSpy.getAllFileAndParsersAtom(resp);
		verify(cudSpy).writeFileToResponse(resp, "atom.xml");
	}
}

