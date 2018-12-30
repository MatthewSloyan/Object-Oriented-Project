package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class Processor {
	
	private int fileCount;
	
	public void process(String dir, String queryFile) {
		File f = new File(dir);
		String [] files = f.list();
		fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount); //put in size also 
		
		//Start a thread pool of size poolSize
		ExecutorService es = Executors.newFixedThreadPool(10);
		
		Map<Integer, Integer> fileMapDotProduct = new HashMap<>();
		Map<Integer, Integer> fileMapMagnitude = new HashMap<>();
		
		long startTime = System.nanoTime(); //running time of program
		
		for (String s: files){
			es.execute(new FileParser(queue, dir, s));
			//new Thread(new FileParser(queue, dir, s)).start();
			
			fileMapDotProduct.put(s.hashCode(), 0);
			fileMapMagnitude.put(s.hashCode(), 0);
		}
		
		//Start a thread pool of size poolSize
		ExecutorService es1 = Executors.newSingleThreadExecutor();
		
		Future<ConcurrentSkipListMap<Integer, List<Index>>> completeMap = es1.submit(new ShingleTaker(queue, fileCount));

        Map<Integer, Integer> map = new HashMap<>();
        
        BufferedReader br = null;
		String line = null;
		String savedString = "";
		String stripptedString = null;
		Pattern pattern = Pattern.compile(" ");
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(queryFile)));
			
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
					
					if (map.containsKey(stripptedString.hashCode())){
						count = map.get(stripptedString.hashCode());
						count++;
					}
					
					map.put(stripptedString.hashCode(), count);
				}
				
				if (savedString != "") {
					int count = 1;
					
					if (map.containsKey(savedString.hashCode())){
						count = map.get(savedString.hashCode());
						count++;
					}
					map.put(savedString.hashCode(), count);
					savedString = "";
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		//Get unique words from both sequences
        //HashSet<String> intersection = new HashSet<>(map.keySet());
        //intersection.retainAll(completeMap.keySet());
        
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
			
			for (int key : map.keySet()) {
				Integer num = map.get(key);
				
				queryMagnitude += num;
				
				if (map.containsKey(key) && completeMap.get().containsKey(key)) {
					
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//COSINE CALCULATION
		for (String s : files) {
			new CalculateCosine().calculateCosine(queryFile, s, fileMapDotProduct.get(s.hashCode()), 
					queryMagnitude, fileMapMagnitude.get(s.hashCode()));
		}
		
		//running time
		System.out.println("\nRunning time (ms): " + (System.nanoTime() - startTime));
		final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Used memory: " + usedMem);
	}
}
