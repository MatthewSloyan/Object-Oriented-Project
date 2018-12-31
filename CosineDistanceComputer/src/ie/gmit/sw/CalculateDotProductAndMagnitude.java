package ie.gmit.sw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CalculateDotProductAndMagnitude {
	
	private BlockingQueue <Word> queue;
	private String queryFile;
	private String [] files;
	private boolean url;
	private long startTime;
	
	public CalculateDotProductAndMagnitude(BlockingQueue<Word> queue, String queryFile, String[] files, boolean url, long startTime) {
		super();
		this.queue = queue;
		this.queryFile = queryFile;
		this.files = files;
		this.url = url;
		this.startTime = startTime;
	}

	public void calculate() {
		//Start a single thread executor
		ExecutorService es1 = Executors.newSingleThreadExecutor();
		Future<ConcurrentHashMap<Integer, List<Index>>> fileMap = es1.submit(new ShingleTaker(queue, files.length));
		
		//Start a single thread executor
		ExecutorService es2 = Executors.newSingleThreadExecutor();
		Future<ConcurrentHashMap<Integer, Integer>> queryMap = es2.submit(new QueryFileParser(queryFile, url));
		
		double queryMagnitude = 0;
		Map<Integer, Integer> fileMapDotProduct = new HashMap<>();
		Map<Integer, Integer> fileMapMagnitude = new HashMap<>();
		
		for (String s: files){
			fileMapDotProduct.put(s.hashCode(), 0);
			fileMapMagnitude.put(s.hashCode(), 0);
		}
		
		//FILE STRUCTURE
		try {
			for (int key : fileMap.get().keySet()) {
			    List<Index> list = fileMap.get().get(key);
			    
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
				
				if (queryMap.get().containsKey(key) && fileMap.get().containsKey(key)) {
					
					List<Index> list = fileMap.get().get(key);
					
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//running time
		System.out.println("\nRunning time (ms): " + (System.nanoTime() - startTime));
		final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Used memory: " + usedMem + "\n");
		
		new CalculateCosine(queryFile, files, queryMagnitude, fileMapDotProduct, fileMapMagnitude).calculate();
		
		es1.shutdown();
		es2.shutdown();
	}
}
