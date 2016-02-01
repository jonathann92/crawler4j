package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres TODO
 * 			Leonard Bejosano TODO
 */

import java.util.*;
import java.io.*;

import edu.uci.ics.crawler4j.hw.CrawlerData;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.hw.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.Header;



public class Helper {

	public static Map<String, Integer> wordFrequencies(String text) {
    Map<String, Integer> freq = new HashMap<String, Integer>();

    String[] items = text.split("[^a-zA-Z0-9'`]+");
    List<String> words = Arrays.asList(items);

    for(int i = 0; i < words.size(); ++i){

		Integer count = freq.get(words.get(i));
		if(count == null)
			freq.put(words.get(i), 1);
		else
			freq.put(words.get(i), count+1);
		}

    return freq;
	}

	public static Map<String, Integer> subdomainFreq(List<String> subdomains) {
		Map<String, Integer> freq = new HashMap<String, Integer>();

		for(int i = 0; i < subdomains.size(); ++i){

		Integer count = freq.get(subdomains.get(i));
		if(count == null)
			freq.put(subdomains.get(i), 1);
		else
			freq.put(subdomains.get(i), count+1);
		}

		return freq;
	}

	public static List<Frequency> createFrequencies(Map<String, Integer> counter){
		List<Frequency> freqs = new ArrayList<Frequency>();

		for (Map.Entry<String,Integer> wordCounter : counter.entrySet()) {
	        String word = wordCounter.getKey();
	        Integer count = wordCounter.getValue();

	        freqs.add(new Frequency(word, count));
	    }

		return freqs;
	}

	public static ObjectOutputStream writeDataToFile(Page page, ObjectOutputStream oos, int id, String dir){
		String url = page.getWebURL().getURL();
		String subdomain = page.getWebURL().getSubDomain();
		Map<String, Integer> wordFreq = null;

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();

			wordFreq = Helper.wordFrequencies(text);
		} else {
            wordFreq = new HashMap<String, Integer>();
        }
		CrawlerData data = new CrawlerData(url,subdomain, wordFreq);

		if(!dir.endsWith("/")) dir += "/";


		String pathToFile = dir + "CrawlerData/crawler-" + (char) (id+64);



		try{
	    	if(oos == null){
					boolean fileExists = true;
					while(fileExists){
						File file = new File (pathToFile+".cwl");
						if(file.exists()){
							pathToFile += (char)(id+64);
						} else fileExists = false;
					}

	    		File file = new File(pathToFile+".cwl");
	    			if(file.exists()){
	    				pathToFile += id;
	    		}
	    		pathToFile += ".cwl";
	    		oos = new ObjectOutputStream(new FileOutputStream(pathToFile));
	    	}

	    	oos.writeObject(data);
	    	oos.flush();
	    	oos.reset();

	    } catch (IOException e) {
	    	e.printStackTrace();
	    } finally {
	    }

		return oos;
	}

	public static void logInfo(Page page, Logger logger){
	    int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String subDomain = page.getWebURL().getSubDomain();

        logger.info("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.info("Sub-domain: '{}'", subDomain);

        if (page.getParseData() instanceof HtmlParseData) {
          HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
          String text = htmlParseData.getText();

          logger.info("Text length: {}", text.length());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
          logger.info("Response headers:");
          for (Header header : responseHeaders) {
              if(header.getName().equals("Date"))
                logger.info("\t{}: {}", header.getName(), header.getValue());
          }
        }
        logger.info("Time since this start (seconds): {}", (System.currentTimeMillis() - TheController.startTime)/1000.0);

		logger.info("=================");
	}

}
