package edu.uci.ics.crawler4j.hw;

import java.util.*;


public class Helper {

	public static Map<String, Integer> wordFrequencies(String text) {
    Map<String, Integer> freq = new HashMap<String, Integer>();
    
    String[] items = text.split("[^a-zA-Z0-9'`]+");
    List<String> words = Arrays.asList(items);

    for(int i = 0; i < words.size(); ++i){
    	// TODO: uncomment after stop words has been implemented
    	//if(TheController.stopWords.contains(words.get(i)))
    	//	continue;
    		
		Integer count = freq.get(words.get(i));
		if(count == null)
			freq.put(words.get(i), 1);
		else
			freq.put(words.get(i), count+1);
		}

    return freq;
	}
	
	public static List<Frequency> createFrequencies(Map<String, Integer> counter){
		List<Frequency> freqs = new ArrayList<Frequency>();
		
		for (Map.Entry<String,Integer> wordCounter : counter.entrySet()) {
	        String word = wordCounter.getKey();
	        Integer count = wordCounter.getValue();
	        
	        freqs.add(new Frequency(word, count));
	    }
		
		return freqs;
	}

}
