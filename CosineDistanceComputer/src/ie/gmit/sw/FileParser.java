package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class FileParser implements Runnable{
	
	private BlockingQueue<Word> queue;
	private String file;
	
	//Constructor
	public FileParser(BlockingQueue<Word> q, String f){
		this.queue = q;
		this.file = f;
	}

	public void run() {
		BufferedReader br = null;
		String line = null;
		String sString = null;
		
		System.out.println("Hello " + file);
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			while((line = br.readLine()) != null) {
				String[] words = line.split(" ");
				
				for(String s: words){
					sString = s.toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");
					
					queue.put(new Word (file, sString));
				}
				
			}
			queue.put(new Poison(file, sString)); //finishes
			System.out.println("Finished");
			br.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
