package edu.uci.ics.crawler4j.submission.crawler;

import java.util.*;
import edu.uci.ics.crawler4j.crawler.Page;

public class CrawlStats {
	/*
	This is a map of URL's as a key and
	word frequencies as a value

	word frequencies is a map of words and their counts
	in each document
	*/
	Set<String> domainSet = new HashSet();
	Map<String, Map<String,Integer>> pageInfo = new HashMap();

	public CrawlStats(){}

	public void insertDomain(String domain){
		domainSet.add(domain);
	}

	public Set<String> getDomains(){
		return domainSet;
	}

	public int numDomains(){
		return domainSet.size();
	}

	public void insertURL(String url, Map<String, Integer> wordFreq){
		if(!pageInfo.containsKey(url)){
			pageInfo.put(url, wordFreq);
		}
	}

	public Map<String, Map<String,Integer>> getPageInfo(){
		return pageInfo;
	}

	public int numPages(){
		return pageInfo.size();
	}
}
