package ie.gmit.sw;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Processor {
	
	private int fileCount;
	
	public void process(String dir, String queryFile) {
		File f = new File(dir);
		String [] files = f.list();
		fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount); //put in size also 
		
		//new Thread(new FileParser(queue, queryFile)).start();
		
		for (String s: files){
			System.out.println(s);
			new Thread(new FileParser(queue, s)).start();
		}
		
		new Thread(new ShingleTaker(queue, fileCount)).start();
		
	}
}

