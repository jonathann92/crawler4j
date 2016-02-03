package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres TODO
 * 			Leonard Bejosano TODO
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	final static String gessicaID = "28808697"; 
	final static String leoID = "32437030"; 

	public static long startTime = 0;
	
	public static Set<Integer> contentHash = new HashSet<Integer>();

	public static CrawlController setup(String storageFolder,  boolean longRun) throws Exception{
		String userAgent = "INF 121 test";
		CrawlConfig config = new CrawlConfig();
		int maxDepth = 2;
		int maxPages = 10;
		int megabyte = 1048576;
		
		String folder = storageFolder;
		if(!folder.endsWith(File.separator)) folder += File.separator;
		folder += "CrawlerData" + File.separator;
		
		File theDir = new File(folder);


		if (!theDir.exists()) {
		    logger.info("creating directory: {}" , folder);
		    logger.info("Did not retrieve a previous hashset"); 
		    try{
		        theDir.mkdir();
		        logger.info("CrawlerData DIR created");
		    }
		    catch(SecurityException se){
		        se.printStackTrace();		    }
		    
		}else if(new File(folder+"hashset").exists()){
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(folder+"hashset"));
				HashSet<Integer> input = (HashSet<Integer>) ois.readObject();
				if(input.size()> 1){
					contentHash = input;
					logger.info("Retreived previous hashSet");
		            System.out.println("HashSet length: " + TheController.contentHash.size());
				} 
			} catch (Exception e) { e.printStackTrace(); } finally { if (ois != null){ try { ois.close(); } catch (Exception e2 ) {}}}
		} else logger.info("Did not retrieve a previous hashset"); 
		
		
		if(longRun){
			System.out.println("Longrun == true");
			maxDepth = -1;
			maxPages = -1;
			userAgent = "UCI Inf141-CS121 crawler " + jonID + " " + gessicaID + " " + leoID;
		} 
		
		config.setCrawlStorageFolder(storageFolder);
		config.setPolitenessDelay(601);
		config.setMaxDepthOfCrawling(maxDepth);
		config.setMaxPagesToFetch(maxPages);
		config.setIncludeBinaryContentInCrawling(false);
		config.setResumableCrawling(longRun);
		config.setUserAgentString(userAgent);
		config.setMaxDownloadSize(megabyte * 10);

		PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	    logger.info("The storage folder:  {}", storageFolder);

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

		controller.addSeed("http://www.ics.uci.edu/~jonattn2");
		controller.addSeed("http://sli.ics.uci.edu/");
        controller.addSeed("http://archive.ics.uci.edu/");


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
	            String toHashSet = "CrawlerData" + File.separator + "hashset";
	            String dir = (crawlStorageFolder.endsWith(File.separator)) ? crawlStorageFolder + toHashSet  : crawlStorageFolder + File.separator +  toHashSet;

	            try {
		            File f = new File(dir);
		            if(!f.exists())
		            	f.createNewFile();
		            
		            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dir));
		            oos.writeObject(TheController.contentHash);
		            oos.writeObject(null);
		            oos.close();
		            System.out.println("HashSet length: " + TheController.contentHash.size());
		            logger.info("Saved HashSet to file");
	            } catch (Exception e ) { e.printStackTrace(); }
	            System.out.println("SHUTDOWN CRAWLER");
	        }
	    });  
	    
	}
	
	
	


}
