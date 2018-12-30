package ie.gmit.sw;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Processor {
	
	private int fileCount;
	
	public void process(String dir, String queryFile, boolean url) {
		File f = new File(dir);
		String [] files = f.list();
		fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount); //put in size also 
		
		//Start a thread pool of size poolSize
		ExecutorService es = Executors.newFixedThreadPool(4);
		
		Map<Integer, Integer> fileMapDotProduct = new HashMap<>();
		Map<Integer, Integer> fileMapMagnitude = new HashMap<>();
		
		long startTime = System.nanoTime(); //running time of program
		
		for (String s: files){
			es.execute(new FileParser(queue, dir, s));
			
			fileMapDotProduct.put(s.hashCode(), 0);
			fileMapMagnitude.put(s.hashCode(), 0);
		}
		
		//Start a single thread executor
		ExecutorService es1 = Executors.newSingleThreadExecutor();
		Future<ConcurrentHashMap<Integer, List<Index>>> completeMap = es1.submit(new ShingleTaker(queue, fileCount));
		
		//Start a single thread executor
		ExecutorService es2 = Executors.newSingleThreadExecutor();
		Future<ConcurrentHashMap<Integer, Integer>> queryMap = es2.submit(new QueryFileParser(queryFile, url));
        
        double queryMagnitude = 0;
		
		try {
			//FILE STRUCTURE
			for (int key : completeMap.get().keySet()) {
			    List<Index> list = completeMap.get().get(key);
			    
			    for (Index indexList: list){
			    	
					if(fileMapMagnitude.containsKey(indexList.getFilename())) {
						int magnitudeCount = fileMapMagnitude.get(indexList.getFilename());
						
						magnitudeCount += indexList.getFrequency();
						
						fileMapMagnitude.put(indexList.getFilename(), magnitudeCount);
					}
				}
			}
			
			for (int key : queryMap.get().keySet()) {
				Integer num = queryMap.get().get(key);
				
				queryMagnitude += num;
				
				if (queryMap.get().containsKey(key) && completeMap.get().containsKey(key)) {
					
					List<Index> list = completeMap.get().get(key);
					
					 for (Index indexList: list){
						 
						if(fileMapDotProduct.containsKey(indexList.getFilename())) {
							int dotProductCount = fileMapDotProduct.get(indexList.getFilename());
							
							//dotProductCount += num * indexList.getFrequency();
							
							if (num < indexList.getFrequency()){
								dotProductCount += num;
							}
							else {
								dotProductCount += indexList.getFrequency();
							}
							fileMapDotProduct.put(indexList.getFilename(), dotProductCount);
						}
					 }
				} //if
			} //for
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(queryFile + "\n");
		
		//COSINE CALCULATION
		for (String s : files) {
			String str = new CalculateCosine().calculateCosine(queryFile, s, fileMapDotProduct.get(s.hashCode()), 
					queryMagnitude, fileMapMagnitude.get(s.hashCode()));
			sb.append(str);
		}
		
		//running time
		System.out.println("\nRunning time (ms): " + (System.nanoTime() - startTime));
		final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Used memory: " + usedMem);
		
		new PrintResults().print(sb);
		
		es.shutdown();
		es1.shutdown();
		es2.shutdown();
	}
}
