package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres 28808697
 * 			Leonard Bejosano 32437030
 */

import java.util.*;

import java.io.Serializable;

public class CrawlerData implements Serializable {
	String subdomain = null;
	String url = null;
	String text = null;
    String title = null;

	public CrawlerData(){}

		public CrawlerData(String url, String subdomain, String text, String title){ 
			this.subdomain = subdomain;
			this.url = url;
			this.text = text;
            this.title = title;
		}

		public String getURL(){
			return this.url;
		}

		public String getSubdomain(){
			return this.subdomain;
		}

		public String getText(){
			return this.text;
		}

        public String getTitle(){
            return this.title;
        }

		@Override
		public String toString(){
			return "URL: " + this.url + "\nSubdomain: " + this.subdomain + "\nText length: " + this.text.length();
		}
}
