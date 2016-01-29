package edu.uci.ics.crawler4j.hw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.hw.CrawlerData;
import edu.uci.ics.crawler4j.hw.Helper;


public class TheController {
	private static final Logger logger = LoggerFactory.getLogger(TheController.class);

	final static String jonID = "54203830";
	final static String gessicaID = "#"; // TODO
	final static String leoID = "#"; // TODO
	public static Set<String> stopWords = new HashSet<String>(); // TODO see below @ setStopWords()

	public static CrawlController setupController(String storageFolder,  boolean longRun) throws Exception{
		String userAgent = "UCI Inf141-CS121 crawler " + jonID + " " + gessicaID + " " + leoID;
		CrawlConfig config = new CrawlConfig();
		int maxDepth = 2;
		int maxPages = 10;

		if(longRun){
			maxDepth = -1;
			maxPages = -1;
		}

		config.setCrawlStorageFolder(storageFolder);
		config.setPolitenessDelay(601);
		config.setMaxDepthOfCrawling(maxDepth);
		config.setMaxPagesToFetch(maxPages);
		config.setIncludeBinaryContentInCrawling(false);
		config.setResumableCrawling(longRun);
		config.setUserAgentString(userAgent);

		PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		return controller;
	}

	public static void incrementSubdomainCount(Map<String, Integer> subdomainCounter, Map<String, Integer> crawlerSubdomainData){
		for (Map.Entry<String,Integer> entry : crawlerSubdomainData.entrySet()) {
			// This will gather the counts for each subdomain from each crawler
		    String sDomain = entry.getKey();
		    Integer count = entry.getValue();

		    Integer currentCount = subdomainCounter.get(sDomain);
		    if(currentCount == null){
		    	subdomainCounter.put(sDomain, count);
		    } else {
		    	subdomainCounter.put(sDomain, currentCount + count);
		    }
		}
	}

	public static List<Frequency> getTotalWordFreq(Map<String, Map<String,Integer>> allPages){
		// This functions adds up all the counters for each every word.
		// wordFreq is a Map of words as the KEY
		// and Count as the VALUE
		Map<String, Integer> wordFreq = new HashMap<String, Integer>();

		for(Map<String, Integer> pageWordCount : allPages.values()){
			for (Map.Entry<String,Integer> wordCounter : pageWordCount.entrySet()) {
		        String word = wordCounter.getKey();
		        Integer count = wordCounter.getValue();

		        Integer currentCount = wordFreq.get(word);
		        if(currentCount == null)
		        	wordFreq.put(word, count);
		        else
		        	wordFreq.put(word, currentCount + count);
		    }
		}

		return Helper.createFrequencies(wordFreq);
	}

	public static void setStopWords(){
		// TODO insert stop words into stopWords
	}

	public static void processData(CrawlController controller){
		// Each index contains the data that the crawlers have stored
		// I.E it contains the data and methods in the CrawlerData class
		List<Object> crawlerData = controller.getCrawlersLocalData();

		// This is a map where
		// URLs are KEY
		// map of word frequencies are VALUE
		Map<String, Map<String,Integer>> allPages = new HashMap<String, Map<String,Integer>>();

		Map<String, Integer> subdomainCounter = new HashMap<String, Integer>();

		for(Object localData: crawlerData){
			CrawlerData data = (CrawlerData) localData;
			Map<String, Integer> crawlerSubdomainData = data.getDomainCounter();
			incrementSubdomainCount(subdomainCounter, crawlerSubdomainData);
			allPages.putAll(data.getPageInfo());
		}

		List<Frequency> subdomainFreq = Helper.createFrequencies(subdomainCounter);
		List<Frequency> wordFreq = getTotalWordFreq(allPages);

		System.out.println("subdomainFreq size: " + subdomainFreq.size());
		System.out.println("wordFreq.size: " + wordFreq.toString());
		System.out.println("Num pages: " + allPages.size());

		/*
		 * TODO
		 * SORT subdomainFreq
		 * SORT wordFreq
		 *
		 * DO #2 use allPages.size()
		 * DO #3 use subdomainFreq and write what teacher lady wanted
		 * DO #4 (use allPages to see who has the biggest map)
		 * DO #5 after sorting wordFreq write first 500 words to file
		 */

	}

	public static void main(String[] args) throws Exception {
		logger.info("Running The controller");
		if (args.length < 2) {
		      logger.info("Needed parameters: ");
		      logger.info("\t rootFolder (it will contain intermediate crawl data)");
		      logger.info("\t numberOfCralwers (number of concurrent threads)");
		      return;
		    }

		// Changes some values in setupController to run shorter if false
		// We automatically assume true, otherwise we pass through another argument to make it false
		boolean longRun = true;
		if(args.length > 2 && Integer.parseInt(args[2]) == 0){ longRun = false;}

		String crawlStorageFolder = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);

		// setStopWords(); TODO

		CrawlController controller = setupController(crawlStorageFolder, longRun);

		controller.addSeed("http://www.ics.uci.edu/");
		//controller.addSeed("http://www.ics.uci.edu/~jonattn2/resume.html");
		controller.start(TheCrawler.class, numberOfCrawlers);

		processData(controller);
	}


}
