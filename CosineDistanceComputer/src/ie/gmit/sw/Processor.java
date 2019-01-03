package ie.gmit.sw;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
* Process documents selected by user
*
* @author Matthew Sloyan
*/
public class Processor {
	
	/**
	* Base of program.
	* Handles processing of subject directory with a thread pool (Fastest with 2 threads from testing)
	* Also handles putting both the query file and subject directory into maps for comparison using Callable Threads
	* Loop through files creating an instance of FileParser() and passing the queue, directory and the file
	* The queue is used to take words from in the ShingleTaker() class which are added in the FileParser() class.
	* Using SingleThreadExecutors execute both ShingleTaker() and QueryFileParser() and return to a Future ConcurrentHashMap
	* Create an instance of CalculateDotProductAndMagnitude() and pass in the maps when ready to calculate and print results.
	* Running time: Linear O(N)
	* T(n) = n + 14
	* 
	* @param String directory of files
	* @param String query file or url entered by user to compare
	* @param boolean if query is a URL or not
	* 
	* @see #FileParser
	* @see #ShingleTaker
	* @see #QueryFileParser
	* @see #CalculateDotProductAndMagnitude
	*/
	public void process(String dir, String queryFile, boolean url) {
		File f = new File(dir);
		String [] files = f.list();
		int fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount);
		
		//start the running time of program to be printed out for user
		long startTime = System.nanoTime(); 
		
		//Start a thread pool of size 2, and loop over files executing FileParser
		ExecutorService es = Executors.newFixedThreadPool(2);
		
		for (String s: files){
			es.execute(new FileParser(queue, dir, s));
		}

		//Start a single thread executor for ShingleTaker, and return fileMap
		ExecutorService es1 = Executors.newSingleThreadExecutor();
		Future<ConcurrentHashMap<Integer, List<Index>>> fileMap = es1.submit(new ShingleTaker(queue, files.length));
		
		//Start a single thread executor for QueryFileParser, and return queryMap
		ExecutorService es2 = Executors.newSingleThreadExecutor();
		Future<ConcurrentHashMap<Integer, Integer>> queryMap = es2.submit(new QueryFileParser(queryFile, url));
	
		try {
			//pass in both maps when ready for comparision
			new CalculateDotProductAndMagnitude(fileMap.get(), queryMap.get(), queryFile, files, startTime).calculate();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		//close threads
		es.shutdown();
		es1.shutdown();
		es2.shutdown();
	}
}
