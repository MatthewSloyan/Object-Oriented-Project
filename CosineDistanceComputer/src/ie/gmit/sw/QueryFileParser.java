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

public class QueryFileParser implements Callable<ConcurrentHashMap<Integer, Integer>>{
	
	private ConcurrentHashMap<Integer, Integer> queryMap = new ConcurrentHashMap<Integer, Integer>();
	private String file;
	private boolean url;
	
	public QueryFileParser(String file, boolean url) {
		super();
		this.file = file;
		this.url = url;
	}
	
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
				
				if (arrayLength % 3 == 1) {
					savedString = words[arrayLength - 1];
					arrayLength -= 1;
				}
				else if (arrayLength % 3 == 2) {
					savedString = words[arrayLength - 2] + " " + words[arrayLength - 1];
					arrayLength -= 2; 
				}
				
				//FOR
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
			e.printStackTrace();
		}
		
		return queryMap;
	}
}