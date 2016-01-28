package edu.uci.ics.crawler4j.submission.crawler;

import java.util.*;
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

public class OurCrawler extends WebCrawler {

	private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" +
      "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	CrawlStats localStats;

	public OurCrawler(){
		localStats = new CrawlStats();
	}

	@Override
	public boolean shouldVisit(Page refPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches() && (href.startsWith("http://www.ics.uci.edu/") || href.startsWith("https://www.ics.uci.edu/") || href.startsWith("https://ics.uci.edu/")) ;
	}

	@Override
	public void visit(Page page){
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		localStats.insertDomain(domain);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			

			String[] items = text.split("[^a-zA-Z0-9'`]+");
			List<String> tokens = Arrays.asList(items);
			Map<String, Integer> wordFreq = Utilities.wordFrequencies(tokens);
			localStats.insertURL(url, wordFreq);
    }

		logger.info("Visited: {}", page.getWebURL().getURL());
	}

	@Override
	public Object getMyLocalData(){
		return localStats;
	}
}
