package edu.uci.ics.crawler4j.hw;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessData {
	private static final Logger logger = LoggerFactory.getLogger(ProcessData.class);

	
	public static List<Frequency> getTotalWordFreq(List<CrawlerData> pages, Set<String> stopWords){
		// This functions adds up all the counters for each every word.
		// wordFreq is a Map of words as the KEY
		// and Count as the VALUE
		Map<String, Integer> wordFreq = new HashMap<String, Integer>();
		
		for(CrawlerData page: pages){
			Map<String, Integer> pageWordCount = page.getWordFreq();
			
			for(Map.Entry<String, Integer> wordCounter : pageWordCount.entrySet()){
				String word = wordCounter.getKey();
				if(stopWords.contains(word)) continue;
				
				Integer count = wordCounter.getValue();
				
				Integer currentCount = wordFreq.get(word);
				if(currentCount == null)
					wordFreq.put(word,count);
				else
					wordFreq.put(word, currentCount + count);
			}
		}
		
		return Helper.createFrequencies(wordFreq);
	}
	
	public static Set<String> stopWords(){
		String words = "a about above after again against all am an "
				+ "and any are aren't as at be because been before being "
				+ "below between both but by can't cannot could couldn't did didn't "
				+ "do does doesn't doing don't down during each few for from further "
				+ "had hadn't has hasn't have haven't having he he'd he'll he's her here "
				+ "here's hers herself him himself his how how's i i'd i'll i'm i've if "
				+ "in into is isn't it it's its itself let's me more most mustn't my myself "
				+ "no nor not of off on once only or other ought our ours ourselves out "
				+ "over own same shan't she she'd she'll she's should shouldn't so some such "
				+ "than that that's the their theirs them themselves then there there's these "
				+ "they they'd they'll they're they've this those through to too under until up "
				+ "very was wasn't we we'd we'll we're we've were weren't what what's when when's "
				+ "where where's which while who who's whom why why's with won't would wouldn't you "
				+ "you'd you'll you're you've your yours yourself yourselves";
		
		
		return new HashSet<String>(Arrays.asList(words.split("\\s+")));
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
                toReturn.remove(toReturn.size()-1);
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
	
	public static List<Frequency> getSubdomainFreq(List<CrawlerData> pages){
		if(pages == null) return null;
		List<String> subdomains = getSubdomains(pages);
		Map<String, Integer> subdomainCounter = Helper.subdomainFreq(subdomains);
		return Helper.createFrequencies(subdomainCounter);
	}
	
	public static List<String> getSubdomains(List<CrawlerData> pages){
		List<String> subdomains = new ArrayList<String>();

		for(int i = 0; i < pages.size(); ++i){
			subdomains.add(pages.get(i).getSubdomain());
		}

		return subdomains;
	}
	
	
	public static Map<String, Integer> getUrlSize(List<CrawlerData> pages){
		if(pages == null) return null;
		Map<String, Integer> toReturn = new HashMap<String, Integer>();
		
		for(CrawlerData page : pages){
			Integer text = 0;
			
			for(Integer count: page.getWordFreq().values())
				text += count;
			
			toReturn.put(page.getURL(), text);
		}
		
		return toReturn;
	}
	
	public static String findLongestPage(Map<String, Integer> urlSize){
		String longestPage = null;
		long num = 0;
		for(Map.Entry<String, Integer> wordCounter : urlSize.entrySet()){
			String url = wordCounter.getKey();
			Integer count = wordCounter.getValue();
			if(count > num){
				longestPage = url;
				num = count;
			}
		}
		
		return longestPage;
	}

	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Need 1 argument");
			return;
		}
		String dir = args[0];
		
		logger.info("Processing Data");
		List<String> files = getFilesInDirectory(dir+"/CrawlerData/");

		// Grabs the data from each file
		// Each index of this list contains one URL's information:
		// (String url, String subdomain, Map<String, Integer> wordFrequencies)
		List<CrawlerData> pages = getDataFromFiles(files); // Use for #2
		System.out.println("HERE");

		List<Frequency> subdomainFreq = getSubdomainFreq(pages); // Use for #3 TODO sort list
		
		//Map<String, Integer> urlSize = getUrlSize(pages); // Use for #4
		//String longestPage = findLongestPage(urlSize); // Use for #4
		// These 3 lines are equivalent
		String longestPage = findLongestPage(getUrlSize(pages)); // Use for #4
		
		List<Frequency> totalWordFreq = getTotalWordFreq(pages, stopWords()); // Use for #5 TODO SORT list
		
		// Answer for #2
		System.out.println("Total num pages: " + pages.size()); 

		// Answer for #3 TODO Sort the List and write to file
		for(int i = 0; i < subdomainFreq.size(); ++i){
			Frequency subdomain = subdomainFreq.get(i);
			System.out.println(subdomain.getText() + ", " + subdomain.getFrequency());
		}
		
		// Answer for #4
		System.out.println("longest page is: " + longestPage); 



		/*
		 * TODO
		 * SORT subdomainFreq
		 * SORT totalWordFreq
		 *
		 * DO #3  
		 * DO #5 
		 */

		
		/*
		for(int i = 0; i < pages.size(); ++i){
			System.out.println(pages.get(i).getURL());
		}
		*/

	}

}
