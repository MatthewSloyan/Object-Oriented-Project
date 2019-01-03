package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
* Parse the query file/url line by line into three word shingles and add them to a ConcurrentHashMap to be returned
*
* @author Matthew Sloyan
*/
public class QueryFileParser implements Callable<ConcurrentHashMap<Integer, Integer>>{
	
	private ConcurrentHashMap<Integer, Integer> queryMap = new ConcurrentHashMap<Integer, Integer>();
	private String file;
	private boolean url;
	
	/**
	* Constructor
	* 
	* @param String - name of the file to read in
	* @param boolean - whether or not the input is a url
	*/
	public QueryFileParser(String file, boolean url) {
		super();
		this.file = file;
		this.url = url;
	}
	
	/**
	* Callable thread that uses the input query file/url and parses it line by line, then returns a complete ConcurrentHashMap
	* If it's a URL then a connection is opened and the HTML is returned. jSoup is used to remove all HTML tags and keep only text
	* Each line is stripped of everything but words and spaces.
	* Three word shingles are taken from this line, and any remainders are added as single or double shingles (E.g thank you, you)
	* Each shingle is added to a ConcurrentHashMap, if it already exists then the count is incremented
	* 
	* For simplicity and SRP this class only parses the query files/urls as it deals with just a single returned map.
	* However the FileParser class deals with subject directory files (multiple files) and adds them to a queue.
	* Running time: Quadratic O(N^2);
	* T(n) = n^2 + 10n
	* 
	* @return ConcurrentHashMap of the created map of words and count
	* 
	* @throws Exception if error occurs when reading file
	* @exception IOException
	*/
	public ConcurrentHashMap<Integer, Integer> call() throws Exception{
		
		BufferedReader br = null;
		Document doc; //used for JSoup
		String line = null;
		String savedString = "";
		String stripptedString = null;
		Pattern pattern = Pattern.compile(" ");
		
		try {
			if (url == false) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			}
			else {
				URL websiteURL = new URL(file);
		        URLConnection connectionEstablished = websiteURL.openConnection();
		        br = new BufferedReader(new InputStreamReader(connectionEstablished.getInputStream()));
			}
			
			while((line = br.readLine()) != null) {
				
				//If it's from a URL then parse using Jsoup and get just the text to remove the HTMl tags.
				//Then set the line to the string for parsing.			
				if (url == true) {
					doc = Jsoup.parse(line);
					line = doc.text();
				}
				
				String[] words = pattern.split(line.toUpperCase().replaceAll("[^A-Za-z0-9 ]", ""));
				
				int arrayLength = words.length;
				
				//if the remainder is 1 then there is one extra single word to be put on the queue separately.
				if (arrayLength % 3 == 1) {
					savedString = words[arrayLength - 1];
					arrayLength -= 1;
				}
				else if (arrayLength % 3 == 2) {
					savedString = words[arrayLength - 2] + " " + words[arrayLength - 1];
					arrayLength -= 2; 
				}
				
				//Loop by three to get three word shingles
				for (int i = 0; i < arrayLength; i+=3) { 
					int count = 1;
					
					stripptedString = words[i];
					stripptedString += " " + words[i+1];
					stripptedString += " " + words[i+2];
					
					if (queryMap.containsKey(stripptedString.hashCode())){
						count = queryMap.get(stripptedString.hashCode());
						count++;
					}
					
					queryMap.put(stripptedString.hashCode(), count);
				}
				
				//if savedString is not equal to "" then there must be a remainder words
				if (savedString != "") {
					int count = 1;
					
					if (queryMap.containsKey(savedString.hashCode())){
						count = queryMap.get(savedString.hashCode());
						count++;
					}
					queryMap.put(savedString.hashCode(), count);
					savedString = "";
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
		
		return queryMap;
	}
}