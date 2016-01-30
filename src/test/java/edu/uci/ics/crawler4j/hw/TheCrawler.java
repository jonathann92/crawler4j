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
		String dir = this.getMyController().getConfig().getCrawlStorageFolder();
		if(!dir.endsWith("/")) dir += "/";

		oos = Helper.writeDataToFile(page, oos, this.getMyId(), dir);
		Helper.logInfo(page, this.logger);
		
	}

	@Override
  public void onBeforeExit() {
    if(oos != null){
    	try {
    		oos.writeObject(null);
    		oos.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
  }

}
