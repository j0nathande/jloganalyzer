package org.jloganalyzer.dao.statistics.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jloganalyzer.dao.statistics.AllFileAndParsersDao;
import org.jloganalyzer.domain.ParseResult;
import org.jloganalyzer.domain.ParseResult.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * DAO for persisting the overall result of parsing in form of an atom-feed.
 * 
 * @author Jonathan Strampp
 *
 */
public class AllFileAndParsersAtomFile implements AllFileAndParsersDao {

	public static final String DESCRIPTION_ENDING_MAX_ENTRIES = "... the following entries are in the logfile.";
	
	public static final String FILENAME = "atom.xml";

	private static Logger LOG = LoggerFactory.getLogger(AllFileAndParsersAtomFile.class);
	
	private String persistDirectory = "";
	
	// default is 5
	private int maxEntriesInDetail = 5;
	
	@Override
	public void persist(HashMap<String, ParseResult> allParserResults) {
		persistFeed(allParserResults);
	}

    /**
     * Persist the given results. 
     * 
     * @param allParserResults All the results.
     * @return The SyndFeed which was written.
     */
    protected SyndFeed persistFeed(HashMap<String, ParseResult> allParserResults) {
    	LOG.info("createAtom10FeedFile of {} entries", allParserResults.size());
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("atom_1.0");
        feed.setTitle("Logfile analyzer");
        feed.setLink("https://url/to/jloganalyzer");
        feed.setDescription("This feed displays the last parser outputs.");
        
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        
        for (Entry<String, ParseResult> allParserResultsEntry : allParserResults.entrySet()) {
            String id = allParserResultsEntry.getKey();
            ParseResult parseResult = allParserResultsEntry.getValue();
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(id);
            entry.setPublishedDate(new Date());
            SyndContent description = new SyndContentImpl();
            description.setType("text/html");
            String logEntriesAsDescription = getAtomDescription(parseResult);
            description.setValue(logEntriesAsDescription);
            entry.setDescription(description);
            entries.add(entry);
        }
        feed.setEntries(entries);
        
        String filePath = persistDirectory + FILENAME;
        writeFeedToFile(feed, filePath);
        return feed;
    }

    /**
     * Generating description for parsing-result.
     * 
     * @param parseResult The result to generate the description for.
     * @return Generated description.
     */
    private String getAtomDescription(ParseResult parseResult) {
        StringBuffer sb = new StringBuffer();
        sb.append("Number of logentries: " + parseResult.getEntries().size());
        sb.append(", duration of analysis: " + parseResult.getDuration());
        sb.append(", last analyzed timestamp: " + parseResult.getLastTimestamp());
        sb.append("<ol>");
        int counter = 0;
        for (Iterator<LogEntry> iterator = parseResult.getEntries().iterator(); iterator.hasNext();) {
            if (++counter > maxEntriesInDetail) {
                break;
            }
            LogEntry logEntry2 = (LogEntry) iterator.next();
            sb.append("<li> " + logEntry2 + "</li>");
        }
        sb.append("</ol>");
        if (counter > maxEntriesInDetail) {
        	sb.append(DESCRIPTION_ENDING_MAX_ENTRIES);
        }
        return sb.toString();
    }
    
    /**
     * Write the SyndFeed to given file.
     *  
     * @param feed The SyndFeed to write.
     * @param filePath The filepath to write to.
     */
    protected void writeFeedToFile(SyndFeed feed, String filePath) {
    	try {
        	LOG.debug("Writing file {}", filePath);
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed,writer);
            writer.close();
        } catch (IOException e) {
        	LOG.error("Error writing atom.xml", e);
        } catch (FeedException e) {
        	LOG.error("Error writing atom.xml", e);
        }
    }

    public String getPersistDirectory() {
		return persistDirectory;
	}

	public void setPersistDirectory(String persistDirectory) {
		this.persistDirectory = persistDirectory;
	}


	public int getMaxEntriesInDetail() {
		return maxEntriesInDetail;
	}


	public void setMaxEntriesInDetail(int maxEntriesInDetail) {
		this.maxEntriesInDetail = maxEntriesInDetail;
	}
	
}
