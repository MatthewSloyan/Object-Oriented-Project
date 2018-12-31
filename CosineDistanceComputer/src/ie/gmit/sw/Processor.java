package ie.gmit.sw;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* Process documents selected by user
*
* @author Matthew Sloyan
*/
public class Processor {
	
	/**
	* Base of program.
	* Handles processing of subject directory with a thread pool (Fastest with 2 threads from testing)
	* Loop through files creating an instance of FileParser() and passing the queue, directory and the file
	* Create an instance of CalculateDotProductAndMagnitude() and pass in the queue and other variables
	* The queue will used to take words from.
	* 
	* @param String directory of files
	* @param String query file or url entered by user to compare
	* @param boolean if query is a URL or not
	* 
	* @see #FileParser
	* @see #CalculateDotProductAndMagnitude
	*/
	public void process(String dir, String queryFile, boolean url) {
		File f = new File(dir);
		String [] files = f.list();
		int fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount);
		
		//Start a thread pool of size 2
		ExecutorService es = Executors.newFixedThreadPool(2);
		
		//start the running time of program to be printed out for user
		long startTime = System.nanoTime(); 
		
		for (String s: files){
			es.execute(new FileParser(queue, dir, s));
		}
		
		new CalculateDotProductAndMagnitude(queue, queryFile, files, url, startTime).calculate();
		
		es.shutdown();
	}
}
