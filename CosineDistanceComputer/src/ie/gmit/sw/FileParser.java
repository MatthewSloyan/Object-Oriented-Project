package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable{
	
	private BlockingQueue<Word> q;
	private String file;
	
	//Constructor
	public FileParser(BlockingQueue<Word> q, String f){
		this.q = q;
		this.file = f;
	}

	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = null;
		
		while((line = br.readLine()) != null) {
			String[] words = line.split(" ");
			
			for(String s: words){
				s = s.toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");
				q.put(new Word (file, s));
			}
			
			q.put(new Poison(file, s)); //finishes
			br.close();
		}
	}

}
