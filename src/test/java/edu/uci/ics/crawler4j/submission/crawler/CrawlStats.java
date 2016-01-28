package edu.uci.ics.crawler4j.submission.crawler;

import java.util.*;
import edu.uci.ics.crawler4j.crawler.Page;

public class CrawlStats {
	Set<String> domainSet = new HashSet();
	Map<String, Map<String,Integer>> pageInfo = new HashMap();
	
	public CrawlStats(){}
	
	public void insertDomain(String domain){
		domainSet.add(domain);
	}
	
	public Set<String> getDomains(){
		return domainSet;
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
