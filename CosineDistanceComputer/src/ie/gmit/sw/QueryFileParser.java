package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;

public class QueryFileParser implements Callable<ConcurrentSkipListMap<Integer, Integer>>{
	
	private ConcurrentSkipListMap<Integer, Integer> queryMap = new ConcurrentSkipListMap<Integer, Integer>();
	private String file;
	
	public QueryFileParser(String file) {
		super();
		this.file = file;
	}
	
	public ConcurrentSkipListMap<Integer, Integer> call() throws Exception{
		
		BufferedReader br = null;
		String line = null;
		String savedString = "";
		String stripptedString = null;
		Pattern pattern = Pattern.compile(" ");
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			while((line = br.readLine()) != null) {
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