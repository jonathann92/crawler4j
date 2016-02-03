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
 * 			Gessica Torres TODO
 * 			Leonard Bejosano TODO
 */

public class TheCrawler extends WebCrawler {
	FileOutputStream fout = null;
	ObjectOutputStream oos = null;
	File file = null;





	private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|mpg|wav|avi|mov|mpeg|ram|m4v|pdf|mso|thmx|uai|odp|smil|exe" +
      "|rm|smil|wmv|swf|wma|zip|rar|gz|pdf|ps|pptx?|xls|xlsx|zip|tgz|jar|7z|rar|tar|bz2|bw|bigwig|jemdoc|docx?))$");

	private static final Pattern HTML = Pattern.compile(".*\\.(html|php|asp|aspx|shtml|xml)$");




	@Override
	public boolean shouldVisit(Page refPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		String sub = url.getSubDomain().toLowerCase();

		return !FILTERS.matcher(href).matches() && sub.contains(".ics") && !href.contains("?");
	}

	@Override
	public void visit(Page page){
		String dir = this.getMyController().getConfig().getCrawlStorageFolder();
		if(!dir.endsWith("/")) dir += "/";
		String text = null;
		boolean seen;
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            text = htmlParseData.getText();

            seen = !TheController.contentHash.add(text.replaceAll("\\s+", " ").trim().hashCode());
          } else return;
        if(seen) return;

		oos = Helper.writeDataToFile(page, oos, this.getMyId(), dir);
		Helper.logInfo(page, this.logger);
	}

	@Override
  public void onBeforeExit() {
    if(oos != null){
    	try {
    		oos.writeObject(null);
            oos.flush();
    		oos.close();
            logger.info("Closed ObjectOutputStream for crawler-{}", this.getMyId());
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
  }

}
