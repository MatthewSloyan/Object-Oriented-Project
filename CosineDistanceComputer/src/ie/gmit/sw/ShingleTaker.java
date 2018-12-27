package ie.gmit.sw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;

public class ShingleTaker implements Runnable{
	
	private Map<String, List<Index>> db = new TreeMap<>();
	private BlockingQueue<Word> queue;
	private int fileCount;
	
	public Map<String, List<Index>> getDb() {
		return db;
	}

	public ShingleTaker(BlockingQueue<Word> q, int count){
		this.queue = q;
		this.fileCount = count;
	}

	public void run(){
		while(fileCount > 0) {
			Word w = new Word();
			String shingle = null;
			boolean isInList = false;
			//int count = 0;
			
			try {
				w = queue.take();
				
				if(w instanceof Poison){
					fileCount--;
				}
				else{
					shingle = w.getShingle();
					//list = null;
					
					if(db.containsKey(shingle)){
						List<Index> list = new ArrayList<Index>();
						list = db.get(shingle);
						
						isInList = false;
						
						for (Index indexList: list){
							if (indexList.getFilename().equals(w.getBook())){
								//Increase freq by 1
								indexList.setFrequency(indexList.getFrequency() + 1);
								isInList = true;
							}
						}
						
						if(isInList == false) {
							list.add(new Index(1, w.getBook()));
						}
						
						db.put(shingle, list);
					}
					else {
						//list = db.get(shingle);
						//System.out.println("In Contains Key " + w.getShingle());
						
						List<Index> list = new ArrayList<Index>();
						//Word w = db.get(shingle);
						list.add(new Index(1, w.getBook())); //need to update 1
						db.put(shingle, list);	
						
						//System.out.println(shingle + " " + list.size());
					}
				}
				//System.out.println("Book = " + w.getBook());
				//System.out.println("Shingle= " + w.getShingle());
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Hello");
			}
			
			if (fileCount == 0){
				//finish(); //completes cosine distance
				System.out.println("Finished ST");
			}
		}
	}
}

/*package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;

public class ShingleTaker implements Runnable{
	
	private Map<String, Index> db = new TreeMap<>();
	private BlockingQueue<Word> queue;
	private int fileCount;
	private Boolean keepRunning = true;

	public ShingleTaker(BlockingQueue<Word> q, int count){
		this.queue = q;
		this.fileCount = count;
	}

	public void run(){
		while(fileCount > 0 && keepRunning) {
			Word w = queue.take();
			String shingle;
			int count;
			
			if(w instanceof Poison){
				fileCount--;
			}
			else{
				shingle = w.getShingle();
				List<Index> list = null;
			}
			
			if(!db.containsKey(shingle)){
				List<Index> list = new ArrayList<Index>();
				list = db.get(shingle);
				list.add(new Index(count++, w.getBook())); //need to update 1
				db.put(shingle, list);	
			}
			if (fileCount == 0){
				//finish(); //completes cosine distance
			}
		}
	}
}*/
