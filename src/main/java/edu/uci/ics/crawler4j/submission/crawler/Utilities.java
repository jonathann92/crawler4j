package edu.uci.ics.crawler4j.submission.crawler;

import java.util.*;

public class Utilities {

	public static Map<String, Integer> wordFrequencies(List<String> words) {
    Map<String, Integer> freq = new HashMap<String, Integer>();

    for(int i = 0; i < words.size(); ++i){
			Integer count = freq.get(words.get(i));
			if(count == null)
				freq.put(words.get(i), 1);
			else
				freq.put(words.get(i), count+1);
		}

    return freq;
  }

}
