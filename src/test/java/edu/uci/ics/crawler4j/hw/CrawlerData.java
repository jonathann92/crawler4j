package edu.uci.ics.crawler4j.hw;

import java.util.*;

public class CrawlerData {
	/*
	This is a map of URL's as a key and
	word frequencies as a value

	word frequencies is a map of words and their counts
	in each document
	*/
	Map<String, Integer> domainCounter = new HashMap<String, Integer>();
	Map<String, Map<String,Integer>> pageInfo = new HashMap<String, Map<String,Integer>>();

	public CrawlerData(){}
	
	public void incrementDomainCounter(String subdomain){
		Integer counter = domainCounter.get(subdomain);
		if(counter == null){
			domainCounter.put(subdomain, 1);
		}
		else{
			domainCounter.put(subdomain, counter + 1);
		}
	}

	public Map<String, Integer> getDomainCounter(){
		return domainCounter;
	}

	public void insertURL(String url, Map<String, Integer> wordFreq){
		if(!pageInfo.containsKey(url)){
			pageInfo.put(url, wordFreq);
		}
	}

	public Map<String, Map<String,Integer>> getPageInfo(){
		return pageInfo;
	}
}

