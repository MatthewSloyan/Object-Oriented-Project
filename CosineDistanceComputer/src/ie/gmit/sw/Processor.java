package ie.gmit.sw;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class Processor {
	
	private int fileCount;
	
	public void process(String dir) {
		File f = new File(dir);
		String [] files = f.list();
		fileCount = files.length; //count file
		BlockingQueue <Word> q = ArrayBlockingQueue<>(fileCount); //put in size also 
		
		//new Thread(new ShingleTaker(q, fileCount)).start();
		
		for (String s: files){
			System.out.println(s);
			//new Thread(new FileParser(q, s)).start();
		}
	}
}

