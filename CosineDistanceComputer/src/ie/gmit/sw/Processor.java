package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Processor {
	
	private int fileCount;
	
	public void process(String dir, String queryFile) {
		File f = new File(dir);
		String [] files = f.list();
		fileCount = files.length; //count file
		BlockingQueue <Word> queue = new ArrayBlockingQueue<>(fileCount); //put in size also 
		//BlockingQueue <Word> QueryQueue = new ArrayBlockingQueue<>(1); //put in size also
		
		Map<String, Integer> fileMap = new HashMap<>();
		
		for (String s: files){
			System.out.println(s);
			new Thread(new FileParser(queue, dir+"/"+s)).start();
			
			fileMap.put(dir+"/"+s, 0);
		}
		
		//new Thread(new FileParser(queue, queryFile)).start();
		
		ShingleTaker st = new ShingleTaker(queue, fileCount);
		Thread t1 = new Thread(st); 
        t1.start(); 
        
        try {
			t1.join(); 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Map<String, List<Index>> completeMap = st.getDb();
        
        /*for (Index indexList: list){
			
		}*/
        
        /*for (String key : completeMap.keySet()) {
        	List<Index> list = completeMap.get(key);
        	
        	for (Index indexList: list){
				
			}
        }*/
        
        double result = 0;
        //int result2 = 0;
        //for (Map.Entry<String, List<Index>> entry : completeMap.entrySet()) {
        
        for (String key : completeMap.keySet()) {
            //String key = entry.getKey();
            List<Index> list = completeMap.get(key);
            
            for (Index indexList: list){
				if(indexList.getFilename().equals(dir+"/Test.txt")) {
					result += indexList.getFrequency();
				}
			}
            
            /*for (Index indexList: list){
				if(indexList.getFilename().equals("Hello.txt")) {
					result2 += indexList.getFrequency();
				}
			}*/
        }
        
        System.out.println("File M " + result);
        //System.out.println(result2);
        
        //======================
        /*new Thread(new FileParser(QueryQueue, queryFile)).start();
        
        ShingleTaker st2 = new ShingleTaker(QueryQueue, 1);
		Thread t2 = new Thread(st); 
        t2.start(); 
        
        try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Map<String, List<Index>> db2 = st2.getDb();
        
        for (String key : db.keySet()) {
            if (!db2.containsKey(key)) {
            	db.remove(key);
            }
        }
        
        System.out.println();*/
        
        BufferedReader br = null;
		String line = null;
		String sString = null;
		
		Map<String, Integer> map = new TreeMap<>();
		
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
		
		double count = 0;
		
		map.size();
		
		for (String key : map.keySet()) {
			Integer num = map.get(key);
    	
			count += num;
		}
		
		System.out.println("Query M " + count);
		
		double finalResult = 0;
		int countNum = 0;
		
		//Get unique words from both sequences
        //HashSet<String> intersection = new HashSet<>(map.keySet());
        //intersection.retainAll(completeMap.keySet());
		
		for (String key : map.keySet()) {
			if (map.containsKey(key) && completeMap.containsKey(key)) {
				countNum++;
				Integer num = map.get(key);
				List<Index> list = completeMap.get(key);
				
				int calculatedNum = 0;
				
				 for (Index indexList: list){
					 
					if(fileMap.containsKey(indexList.getFilename())) {
						int dotProductCount = fileMap.get(indexList.getFilename());
						
						if (num >= indexList.getFrequency()){
							dotProductCount += num;
						}
						else {
							dotProductCount += indexList.getFrequency();
						}
						fileMap.put(indexList.getFilename(), dotProductCount);
					}
				 }
			} //if
        } //for
		System.out.println("Dot " + fileMap.get(dir+"/Test.txt"));
		
		double dot = 0;
		/*double file = Math.sqrt(result);
		double q = Math.sqrt(count);
		double fin = 0;
		
		if (file > q) {
			fin = file;
		}
		else {
			fin = q;
		}*/
		
		dot = fileMap.get(dir+"/Test.txt") / (Math.sqrt(count * result));
		
		System.out.println("Cosine " + dot);
		
		System.out.println("Count " + countNum);
	}
}

