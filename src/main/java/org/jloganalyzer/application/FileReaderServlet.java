package org.jloganalyzer.application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serving persisted files to the response.
 * 
 * @author Jonathan Strampp
 *
 */
public abstract class FileReaderServlet {

	private static Logger LOG = LoggerFactory.getLogger(FileReaderServlet.class);
	
	private String persistDirectory = "";
	
	/**
	 * Writes file to given HttpServletResponse.
	 * 
	 * @param resp The HttpServletResponse to write to.
	 * @param fileName The filename in the persistDirectory to read from.
	 * @throws IOException If filename is invalid.
	 */
	protected void writeFileToResponse(HttpServletResponse resp, String fileName) throws IOException {
		String filePath = persistDirectory + fileName;
		LOG.debug("Reading file {}", filePath);
		PrintWriter out = resp.getWriter();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
        String line;
        while((line = br.readLine()) != null) {
        	out.write(line + "\n");
        }
		out.close();
	}

	public String getPersistDirectory() {
		return persistDirectory;
	}
	public void setPersistDirectory(String persistDirectory) {
		this.persistDirectory = persistDirectory;
	}
}
