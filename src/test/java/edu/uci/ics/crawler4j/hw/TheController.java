package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres TODO
 * 			Leonard Bejosano TODO
 */

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

	public static long startTime = 0;

	public static CrawlController setup(String storageFolder,  boolean longRun) throws Exception{
		String userAgent = "UCI Inf141-CS121 crawler " + jonID + " " + gessicaID + " " + leoID;
		CrawlConfig config = new CrawlConfig();
		int maxDepth = 2;
		int maxPages = 10;
		int megabyte = 1048576;
		
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
		config.setUserAgentString("crawler4j inf 121 tester");
		config.setMaxDownloadSize(megabyte * 10);

		PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		return controller;
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

		final String crawlStorageFolder = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);

		
		final CrawlController controller = setup(crawlStorageFolder, longRun);

		controller.addSeed("http://www.ics.uci.edu/");
		//controller.addSeed("http://sli.ics.uci.edu/");


		startTime = System.currentTimeMillis();
		
		controller.startNonBlocking(TheCrawler.class, numberOfCrawlers);
		
	    Runtime.getRuntime().addShutdownHook(new Thread()
	    {
	        @Override
	        public void run()
	        {
	        	logger.info("Received kill signal, exiting now");
	            controller.shutdown();
	            controller.waitUntilFinish();
	            System.out.println("SHUTDOWN CRAWLER");
	        }
	    });  
	}


}
