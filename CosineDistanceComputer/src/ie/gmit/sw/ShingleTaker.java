package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;

public class ShingleTaker implements Runnable{
	
	private Map<Integer, List<Index>> db = new TreeMap<>();
	private BlockingQueue<Word> queue;
	private int fileCount;
	
	public Map<Integer, List<Index>> getDb() {
		return db;
	}

	public ShingleTaker(BlockingQueue<Word> q, int count){
		this.queue = q;
		this.fileCount = count;
	}

	public void run(){
		while(fileCount > 0) {
			Word w = new Word();
			int shingle = 0;
			boolean isInList = false;
			
			try {
				w = queue.take();
				
				if(w instanceof Poison){
					fileCount--;
				}
				else{
					shingle = w.getShingle();
					
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
						List<Index> list = new ArrayList<Index>();
						
						list.add(new Index(1, w.getBook())); //need to update 1
						db.put(shingle, list);	
					}
				}//outer else
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (fileCount == 0){
				System.out.println("Finished ST"); //completed
			}
		}
	}
}