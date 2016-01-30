package edu.uci.ics.crawler4j.hw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
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

	public static long startTime = 0;

	public static CrawlController setup(String storageFolder,  boolean longRun) throws Exception{
		String userAgent = "UCI Inf141-CS121 crawler " + jonID + " " + gessicaID + " " + leoID;
		CrawlConfig config = new CrawlConfig();
		int maxDepth = 2;
		int maxPages = 10;
		
		String folder = storageFolder;
		if(!folder.endsWith("/")) folder += "/";
		folder += "CrawlerData/";
		
		File theDir = new File(folder);


		if (!theDir.exists()) {
		    System.out.println("creating directory: " + folder);
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		    }
		    catch(SecurityException se){
		        se.printStackTrace();		    }
		    if(result) {
		        System.out.println("DIR created");
		    }
		}



		if(longRun){
			System.out.println("Longrun == true");
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
		// TODO insert stop words into Set<String> stopWords
		//stopWords = ....
	}

	public static List<String> getFilesInDirectory(String dir){
		List<String> toReturn = new ArrayList<String>();

		File folder = new File(dir);

		for(File file: folder.listFiles()){
			String path = file.getAbsolutePath();

			if(path.endsWith(".cwl")){
				toReturn.add(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			}
		}

		return toReturn;
	}

	public static List<CrawlerData> getDataFromFiles(List<String> files){
		List<CrawlerData> toReturn = new ArrayList<CrawlerData>();

		for(int i = 0; i < files.size(); ++i){
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(files.get(i)));
				CrawlerData data = null;
				while((data = (CrawlerData) ois.readObject()) != null)
					toReturn.add(data);
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					if(ois != null)
						ois.close(); }
				catch (IOException e2 ) { }
			}
		}

		return toReturn;
	}

	public static List<String> getSubdomains(List<CrawlerData> pages){
		List<String> subdomains = new ArrayList<String>();

		for(int i = 0; i < pages.size(); ++i){
			subdomains.add(pages.get(i).getSubdomain());
		}

		return subdomains;
	}

	public static List<Frequency> getSubdomainFreq(List<CrawlerData> pages){
		List<String> subdomains = getSubdomains(pages);
		Map<String, Integer> subdomainCounter = Helper.subdomainFreq(subdomains);
		return Helper.createFrequencies(subdomainCounter);
	}

	public static void processData(String dir){
		//Grabs list of files from directory
		List<String> files = getFilesInDirectory(dir+"/CrawlerData/");

		// Grabs the data from each file
		// Each index contains one URL's information:
		// (String url, String subdomain, Map<String, Integer> wordFrequencies)
		List<CrawlerData> pages = getDataFromFiles(files);

		List<Frequency> subdomainFreq = getSubdomainFreq(pages);

		for(int i = 0; i < subdomainFreq.size(); ++i){
			Frequency subdomain = subdomainFreq.get(i);
			System.out.println(subdomain.getText() + ": " + subdomain.getFrequency());
		}

		/*
		for(int i = 0; i < pages.size(); ++i){
			System.out.println(pages.get(i).getURL());
		}
		*/



		System.out.println(pages.size());

		//System.out.println("wordFreq: " + data.get(0).getWordFreq().toString());
		//System.out.println("Num pages: " + allPages.size());

		/*
		 * TODO
		 * SORT subdomainFreq
		 * SORT wordFreq
		 *
		 * DO #2 use Pages.size() to get number of URLs
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

		CrawlController controller = setup(crawlStorageFolder, longRun);

		controller.addSeed("http://sli.ics.uci.edu/");
		controller.addSeed("http://www.ics.uci.edu/");

		startTime = System.currentTimeMillis();
		controller.start(TheCrawler.class, numberOfCrawlers);

		processData(crawlStorageFolder);
	}


}
