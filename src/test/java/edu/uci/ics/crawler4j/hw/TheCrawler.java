package edu.uci.ics.crawler4j.hw;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.hw.CrawlerData;
import edu.uci.ics.crawler4j.hw.Helper;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres
 * 			Leonard Bejosano
 */

public class TheCrawler extends WebCrawler {
	FileOutputStream fout = null;
	ObjectOutputStream oos = null;
	File file = null;


	private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
      "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern HTML = Pattern.compile(".*\\.(html|php|asp|aspx|shtml|xml)$");


	@Override
	public boolean shouldVisit(Page refPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		//return HTML.matcher(href).matches() && href.contains("ics.uci.edu") ;
		return !FILTERS.matcher(href).matches() && href.contains("ics.uci.edu") ;
	}

	@Override
	public void visit(Page page){
		String url = page.getWebURL().getURL();
		String subdomain = page.getWebURL().getSubDomain();
		Map<String, Integer> wordFreq = null;

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();

			wordFreq = Helper.wordFrequencies(text);
		}
		CrawlerData data = new CrawlerData(url,subdomain, wordFreq);
		
		String dir = this.getMyController().getConfig().getCrawlStorageFolder();

		String pathToFile = dir + "CrawlerData/crawler-" + this.getMyId() + ".txt"; 
		try{
	    	if(fout == null){
	    		fout = new FileOutputStream(pathToFile,true);
	    	}
	    
	    	if(oos == null){
	    		oos = new ObjectOutputStream(fout);
	    	}
	    	
	    	oos.writeObject(data);
	    	oos.flush();

	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {}
	    
	// Log all the information
	int docid = page.getWebURL().getDocid();
    String domain = page.getWebURL().getDomain();
    String parentUrl = page.getWebURL().getParentUrl();

    logger.info("=============");
    logger.info("Docid: {}", docid);
    logger.info("Domain: '{}'", domain);
    logger.info("Sub-domain: '{}'", subdomain);
    logger.info("Parent page: {}", parentUrl);

    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      String html = htmlParseData.getHtml();
      Set<WebURL> links = htmlParseData.getOutgoingUrls();

      logger.info("Text length: {}", text.length());
      logger.info("Html length: {}", html.length());
      logger.info("Number of outgoing links: {}", links.size());
    }
    logger.info("URL: {}", url);
    logger.info("Time since start {}", System.currentTimeMillis() - TheController.startTime);
	logger.info("Timestamp: {}", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));


    logger.info("=============");
	}

	@Override
  public void onBeforeExit() {
    if(oos != null){
    	try {
    		oos.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
  }

}
