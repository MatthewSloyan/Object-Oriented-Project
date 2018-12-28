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


public class Processor {
	
	private int fileCount;
	
	public void process(String dir, String queryFile) {
		File f = new File(dir);
		String [] files = f.list();
		fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount); //put in size also 
		
		//Map<String, Integer> queryMap = new HashMap<>();
		
		Map<String, Integer> fileMapDotProduct = new HashMap<>();
		Map<String, Integer> fileMapMagnitude = new HashMap<>();
		
		long startTime = System.nanoTime(); //running time of program
		
		for (String s: files){
			new Thread(new FileParser(queue, dir, s)).start();
			
			fileMapDotProduct.put(s, 0);
			fileMapMagnitude.put(s, 0);
		}
		
		//new Thread(new FileParser(queryMap, queryFile)).start();
		
		/*FileParser fp = new FileParser(queryMap, queryFile);
		Thread t1 = new Thread(fp); 
		t1.start();*/
		
		/*ShingleTaker st = new ShingleTaker(queue, fileCount);
		Thread t2 = new Thread(st); 
        t2.start(); 
        
        //THREAD
        try {
			t1.join(); 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //Map<String, Integer> map = fp.getQueryMap(); */
		
		ShingleTaker st = new ShingleTaker(queue, fileCount);
		Thread t2 = new Thread(st); 
        t2.start(); 

        Map<String, Integer> map = new HashMap<>();
        
        BufferedReader br = null;
		String line = null;
		String sString = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(queryFile)));
			
			while((line = br.readLine()) != null) {
				String[] words = line.split(" ");
				
				for(String s: words){
					sString = s.toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");
					int count = 1;
					
					if (map.containsKey(sString)){
						count = map.get(sString);
						count++;
					}
					
					map.put(sString, count);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double queryMagnitude = 0;
		
		for (String key : map.keySet()) {
			Integer num = map.get(key);
    	
			queryMagnitude += num;
		}
        
		//THREAD
        try {
			t2.join(); 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Map<String, List<Index>> completeMap = st.getDb();
        
        //FILE STRUCTURE
        
        for (String key : completeMap.keySet()) {
            List<Index> list = completeMap.get(key);
            
            for (Index indexList: list){
            	
				if(fileMapMagnitude.containsKey(indexList.getFilename())) {
					int magnitudeCount = fileMapMagnitude.get(indexList.getFilename());
					
					magnitudeCount += indexList.getFrequency();
					
					fileMapMagnitude.put(indexList.getFilename(), magnitudeCount);
				}
			}
        }
        
		//Get unique words from both sequences
        //HashSet<String> intersection = new HashSet<>(map.keySet());
        //intersection.retainAll(completeMap.keySet());
		
		for (String key : map.keySet()) {
			if (map.containsKey(key) && completeMap.containsKey(key)) {
				
				Integer num = map.get(key);
				List<Index> list = completeMap.get(key);
				
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
		
		//COSINE CALCULATION
		for (String i : files) {
			String file = i;
			double cosine = 0;
    	
			cosine = calculateCosine(fileMapDotProduct.get(file), queryMagnitude, fileMapMagnitude.get(file));
			
			System.out.println("Query file: " + queryFile + " is " + cosine + "percent similar to " + file);
		}
		
		//running time
		System.out.println("\nRunning time (ms): " + (System.nanoTime() - startTime));
		final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Used memory: " + usedMem);
		
	}

	private double calculateCosine(Integer dotProduct, double magnitude1, Integer magnitude2) {
		double cosine = 0;
		
		cosine = dotProduct / (Math.sqrt(magnitude1 * magnitude2));
		
		return cosine;
	}
}
