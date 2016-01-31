package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres TODO
 * 			Leonard Bejosano TODO
 */

import java.util.*;

import java.io.Serializable;

public class CrawlerData implements Serializable {
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
