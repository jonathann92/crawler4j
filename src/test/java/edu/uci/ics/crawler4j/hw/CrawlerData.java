package edu.uci.ics.crawler4j.hw;

import java.util.*;

import java.io.Serializable;

public class CrawlerData implements Serializable {
	/*
	This is a map of URL's as a key and
	word frequencies as a value

	word frequencies is a map of words and their counts
	in each document
	*/
	String subdomain = null;
	String url = null;
	Map<String, Integer> wordFreq = new HashMap<String, Integer>();


	public CrawlerData(){}

		public CrawlerData(String url, String subdomain, Map<String, Integer> wordFreq){
			this.subdomain = subdomain;
			this.url = url;
			this.wordFreq = wordFreq;
		}

		public void setData(String url, String subdomain, Map<String, Integer> wordFreq){
			this.subdomain = subdomain;
			this.url = url;
			this.wordFreq = wordFreq;
		}

		public String getURL(){
			return this.url;
		}

		public String getSubdomain(){
			return this.subdomain;
		}

		public Map<String, Integer> getWordFreq(){
			return wordFreq;
		}
}
