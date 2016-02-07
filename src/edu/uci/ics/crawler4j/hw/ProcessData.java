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

		return new HashSet<String>(Arrays.asList(words.split("\\s+")));
	}

	public static List<String> getFilesInDirectory(String dir){
		List<String> toReturn = new ArrayList<String>();

		File folder = new File(dir);

        System.out.println(folder.getAbsolutePath());
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
        Set<String> urls = new HashSet<String>();

        ObjectOutputStream oos = null;
        try{
            oos = new ObjectOutputStream(new FileOutputStream("./Combined.cwl"));
        } catch (Exception e) { e.printStackTrace(); }



		for(int i = 0; i < files.size(); ++i){
        	ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(files.get(i)));
				CrawlerData data = null;
				while((data = (CrawlerData) ois.readObject()) != null){
                    if(urls.add(data.getURL())){
                        oos.writeObject(data);
                        oos.flush();
					    toReturn.add(data);
                    }
                }
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					if(ois != null)
						ois.close(); }
				catch (IOException e2 ) { }
			}
		}

        try{ if (oos != null){
            oos.writeObject(null);
            oos.flush(); 
            oos.close(); }} catch (Exception e3) { e3.printStackTrace();}

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

	public static void main(String[] args) {
		// SETUP
		if(args.length != 1) {
			System.out.println("Need 1 argument");
			return;
		}
		Set<String> stopWords = setStopWords();
		String dir = args[0].endsWith(File.separator) ? args[0] : (args[0]+=File.separator);
		// END SETUP

		logger.info("Processing Data");
		List<String> files = getFilesInDirectory(dir+"CrawlerData"+File.separator);

		// Grabs the data from each file
		// Each index of this list contains one URL's information:
		// (String url, String subdomain, String text)
		List<CrawlerData> pages = getDataFromFiles(files); // Use for #2

		List<Frequency> subdomainFreq = getSubdomainFreq(pages); // Use for #3 TODO sort list

		for(int i = 0; i < pages.size(); ++i)
			System.out.println(pages.get(i));
		System.out.println("Number of unique URLS: " + pages.size()); // Answer for #2
        System.out.println(pages.get(0).getURL());
        String text = pages.get(0).getText();
        List<String> x = Arrays.asList(text.split("[^A-Za-z0-9'`]"));
        System.out.println(x.get(0));
        System.out.println(x.get(0).length());


	}

}
