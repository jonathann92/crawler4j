package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres 28808697
 * 			Leonard Bejosano 32437030
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
import java.sql.Timestamp;



public class Helper {

	public static Map<String, Integer> wordFrequencies(String text) {
    Map<String, Integer> freq = new HashMap<String, Integer>();
    System.out.println("splitting items");

    String[] items = text.split("[^a-zA-Z0-9'`]+");
    List<String> words = Arrays.asList(items);

    for(int i = 0; i < words.size(); ++i){
    	System.out.println(i);

		Integer count = freq.get(words.get(i));
		if(count == null)
			freq.put(words.get(i), 1);
		else
			freq.put(words.get(i), count+1);
		}

    return freq;
	}

	public static Map<String, Integer> createMapFreq(List<String> subdomains) {
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
    
	public static ObjectOutputStream writeDataToFile(String url, String subdomain, String text, String title, ObjectOutputStream oos, int id, String dir){
		CrawlerData data = new CrawlerData(url,subdomain, text, title);

		try{
	    	if(oos == null){
					if(!dir.endsWith(File.separator)) dir += File.separator;
					String pathToFile = dir + "CrawlerData/crawler-" + (char) (id+64);
					boolean fileExists = true;
					while(fileExists){
						File file = new File (pathToFile+".cwl");
						if(file.exists()){
							pathToFile += (char)(id+64);
						} else fileExists = false;
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

	public static void logInfo(int docid, String url, String subDomain, String text, Logger logger){
        logger.info("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.info("Sub-domain: '{}'", subDomain);
        logger.info("Text length: {}", text.length());
        logger.info("\tDate: {}", new Timestamp(System.currentTimeMillis()));
		logger.info("=================");
	}

}
