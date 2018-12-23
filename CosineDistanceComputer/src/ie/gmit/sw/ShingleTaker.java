package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;

public class ShingleTaker implements Runnable{
	
	private Map<String, List<Index>> db = new TreeMap<>();
	private BlockingQueue<Word> queue;
	private int fileCount;
	private Boolean keepRunning = true;

	public ShingleTaker(BlockingQueue<Word> q, int count){
		this.queue = q;
		this.fileCount = count;
	}

	public void run(){
		while(fileCount > 0 && keepRunning) {
			Word w = null;
			String shingle = null;
			//int count = 0;
			ArrayList<Index> list = new ArrayList<Index>();
			
			try {
				w = queue.take();
				System.out.println("Book = " + w.getBook());
				System.out.println("Shingle= " + w.getShingle());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(w instanceof Poison){
				fileCount--;
			}
			else{
				shingle = w.getShingle();
				list = null;
			}
			
			if(!db.containsKey(shingle)){
				//list = db.get(shingle);
				System.out.println("In Contains Key " + w.getShingle());
				list.add(new Index(1, w.getBook())); //need to update 1
				db.put(shingle, list);	
				System.out.println(shingle + " " + list.size());
			}
			if (fileCount == 0){
				//finish(); //completes cosine distance
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
