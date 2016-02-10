
package edu.uci.ics.crawler4j.hw;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres 28808697
 * 			Leonard Bejosano 32437030
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;


/*
1. How much time did it take to crawl the entire domain?

2. How many unique pages did you find in the entire domain?
(Uniqueness is established by the URL, not the page's content.)

3. How many subdomains did you find? Submit the list of subdomains ordered
alphabetically and the number of unique pages detected in each subdomain.
The file should be called Subdomains.txt, and its content should be lines
containing the URL, a comma, a space, and the number.

4. What is the longest page in terms of number of words?
(Don't count HTML markup as words.)

5. What are the 500 most common words in this domain?
(Ignore English stop words, which can be found, for example,
at http://www.ranks.nl/stopwords.) Submit the list of common
words ordered by frequency (and alphabetically for words with
the same frequency) in a file called CommonWords.txt.
*/

public class ProcessData {
	private static final Logger logger = LoggerFactory.getLogger(ProcessData.class);

	public static Set<String> setStopWords(){
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

		return new HashSet<String>(Arrays.asList(words.replaceAll("'", "").split("\\s+")));
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
		List<String> subdomains = getSubdomains(pages);
		Map<String, Integer> subdomainCounter = Helper.createMapFreq(subdomains);
		return Helper.createFrequencies(subdomainCounter);
	}

	public static List<String> getSubdomains(List<CrawlerData> pages){
		List<String> subdomains = new ArrayList<String>();

		for(int i = 0; i < pages.size(); ++i){
			subdomains.add(pages.get(i).getSubdomain());
		}

		return subdomains;
	}
	
	public static String longestPage(List<CrawlerData> pages){
		String biggestURL = pages.get(0).getURL();
		List<String> textArray = new ArrayList<String>(Arrays.asList(pages.get(0).getText().split("\\s+")));
		List<String> currentText = new ArrayList<String>();
		
		for(int i = 0; i < textArray.size(); ++i){
			if(textArray.get(i).length() > 0)
				currentText.add(textArray.get(i));
		}
		int max = currentText.size();
		
		for(int i = 0; i < pages.size(); i++){
			textArray.clear();
			currentText.clear();
			textArray = new ArrayList<String>(Arrays.asList(pages.get(i).getText().split("\\s+")));
			
			for(int k = 0; k < textArray.size(); ++k){
				if(textArray.get(k).length() > 0)
					currentText.add(textArray.get(k));
			}
			
			int currentSize = currentText.size();
			if(currentSize > max){
				max = currentSize;
				biggestURL = pages.get(i).getURL();
			}
		}
		System.out.println("CASTS SIZE: " + max);
		
		return biggestURL;
	}
	
	public static List<Frequency> getWordFreq(List<CrawlerData> pages) {
		Map<String, Integer> wFreq = new HashMap<String,Integer>();
		
		for(CrawlerData page: pages){
			List<String> text = Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']"));
			Set<String> unique = new HashSet<String>(text);
			unique.remove("");
			
			for(String key : unique){
				if(key.length() <= 1 || Character.isDigit(key.charAt(0))) continue;
				Integer count = wFreq.get(key);
				Integer freq = Collections.frequency(text, key);
				
				if(count == null){
					wFreq.put(key, freq);
				} else {
					wFreq.put(key, freq+count);
				}
			}
		}
		
		for (String word : setStopWords()) {
			wFreq.remove(word);
		}
		wFreq.remove("");
		
		
		return Helper.createFrequencies(wFreq);
	}
	
	public static class WordComparator implements Comparator<Frequency> {
		@Override
		public int compare(Frequency a, Frequency b) {
			return a.getText().compareTo(b.getText());
		}
	}
	
	public static class FreqComparator implements Comparator<Frequency> {
		@Override
		public int compare(Frequency a, Frequency b) {
			if (a.getFrequency() > b.getFrequency()) {
				return -1;
			}
			else if (a.getFrequency() < b.getFrequency()) {
				return 1;
			}
			return a.getText().compareTo(b.getText());
		}
	}

	public static void main(String[] args) {
		// SETUP
		if(args.length != 1) {
			System.out.println("Need 1 argument");
			return;
		}

		String dir = args[0].endsWith(File.separator) ? args[0] : (args[0]+=File.separator);
		// END SETUP

		logger.info("Processing Data");
		List<String> files = getFilesInDirectory(dir+"CrawlerData"+File.separator);

		// Grabs the data from each file
		// Each index of this list contains one URL's information:
		// (String url, String subdomain, String text)
		List<CrawlerData> pages = getDataFromFiles(files); // Use for #2
		System.out.println("2. Number of unique URL's: " + pages.size()); // Answer for #2

		// #3 Get subdomain frequencies, sort
		List<Frequency> subdomainFreq = getSubdomainFreq(pages); // Use for #3
		Collections.sort(subdomainFreq, new WordComparator());
		System.out.println("3. Number of subdomains: " + subdomainFreq.size());
		
		// Write to file
		// Subdomains ordered alphabetically with the number of unique pages detected in each subdomain
		// The file should be called Subdomains.txt
		// Content should be lines containing the URL, a comma, a space, and the number.
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("Subdomains.txt", false));
			for (int i = 0; i < subdomainFreq.size(); i++) {
            	String line = subdomainFreq.get(i).getText() + ", " + subdomainFreq.get(i).getFrequency();
            	bw.write(line);
                bw.newLine();
            }
			bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

		// #4 Longest page in terms of number of words (not including HTML markup)
		String numberFour = longestPage(pages);
		System.out.println("4. Number of words on longest page: " + numberFour);
		
		// #5 500 most common words in this domain, ignoring stop words
		// List common words ordered by frequency (and alphabetically for words with the same frequency) in CommonWords.txt
		List<Frequency> wordFreq = getWordFreq(pages);
		Collections.sort(wordFreq, new FreqComparator());
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("CommonWords.txt", false));
			for (int i = 0; i < 500; i++) {
            	String line = wordFreq.get(i).getText() + ", " + wordFreq.get(i).getFrequency();
            	bw.write(line);
                bw.newLine();
            }
			bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		/*
		for(int i = 0; i < pages.size(); ++i)
			System.out.println(pages.get(i));
		*/
		
		// PRINTING OUT ANSWERS
		System.out.println("\n\nANSWERS:");
		System.out.println("2. Number of unique URL's: " + pages.size()); // Answer for #2
		System.out.println("3. Number of subdomains: " + subdomainFreq.size());
		System.out.println("4. Number of words on longest page: " + numberFour);
	}

}

