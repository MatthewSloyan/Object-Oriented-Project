package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class FileParser implements Runnable{
	
	private BlockingQueue<Word> queue;
	//private Map<String, Integer> queryMap;
	private String dir;
	private String file;
	
	//Constructors
	public FileParser(BlockingQueue<Word> queue, String dir, String file){
		this.queue = queue;
		this.dir = dir;
		this.file = file;
	}
	
	/*public FileParser(Map<String, Integer> queryMap, String queryFile){
		this.queryMap = queryMap;
		this.file = queryFile;
	}*/
	
	/*public Map<String, Integer> getQueryMap() {
		return queryMap;
	}*/

	public void run() {
		BufferedReader br = null;
		String line = null;
		String stripptedString = null;
		Pattern pattern = Pattern.compile(" ");
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(dir+"/"+file)));
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			while((line = br.readLine()) != null) {
				
				String[] words = pattern.split(line);
				
				for(String s: words){
					stripptedString = s.toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");
					
					queue.put(new Word (file, stripptedString));
				}
			}
			queue.put(new Poison(file, stripptedString)); //finishes
			System.out.println("Finished");
			br.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
