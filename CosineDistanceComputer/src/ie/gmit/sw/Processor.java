package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
		for (String s: files){
			System.out.println(s);
			new Thread(new FileParser(queue, s)).start();
		}
		
		ShingleTaker st = new ShingleTaker(queue, fileCount);
		Thread t1 = new Thread(st); 
        t1.start(); 
        
        /*try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        //Map<String, List<Index>> db = st.getDb();
        
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
        
        /*BufferedReader br = null;
		String line = null;
		String sString = null;
		
		Map<String, Integer> map = new TreeMap<>();
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\Pictures\\College\\College\\Third Year\\OOP\\CosineDistanceComputer\\text\\" + queryFile)));
			
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
		
		System.out.println();*/
	}
}

