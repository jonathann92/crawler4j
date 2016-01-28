package edu.uci.ics.crawler4j.submission.crawler;

import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.submission.crawler.CrawlStats;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres
 * 			Leonard Bejosano
 */

public class Crawler extends WebCrawler {

	private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
      "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	CrawlStats localStats;

	public Crawler(){
		localStats = new CrawlStats();
	}

	@Override
	public boolean shouldVisit(Page refPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS && href.startsWith("http://www.ics.uci.edu/") || href.startsWith("https://www.ics.uci.edu/") || href.startsWith("https://ics.uci.edu/") ;
	}

	@Override
	public void visit(Page page){
		logger.info("Visited: {}", page.getWebURL().getURL());


	}


}
