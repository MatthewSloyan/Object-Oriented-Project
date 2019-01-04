package ie.gmit.sw;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
* Callable thread class that takes words from the queue and adds them to a ConcurrentHashMap
*
* @author Matthew Sloyan
*/
public class ShingleTaker implements Callable<ConcurrentHashMap<Integer, List<Index>>>{
	
	private ConcurrentHashMap<Integer, List<Index>> fileMap = new ConcurrentHashMap<>();
	private BlockingQueue<Word> queue;
	private int fileCount;

	/**
	* Constructor
	* 
	* @param q Queue of words to take from
	* @param count number of files in the subject directory
	*/
	public ShingleTaker(BlockingQueue<Word> q, int count){
		this.queue = q;
		this.fileCount = count;
	}

	/**
	* Callable thread that uses the constructed BlockingQueue and file count.
	* Infinite while loop till the queue is empty E.g fileCount is 0
	* On each iteration a word is taken from the queue, if it's an instance of Poison then fileCount is decrements meaning one file has completed.
	* If it's not an instance of Poison then the shingle is taken from the word and is checked to see if its already in the map.
	* If in map - The ArrayList is iterated to find the file, and the frequency is incremented.
	* If not in map - A new key, value pair is added to the map.
	* Running time: Quadratic O(N^2);
	* T(n) = n^2 + 14n
	* 
	* @return ConcurrentHashMap. This is the created map composed a shingle as the key and the value as an ArrayList of Index's
	* @see Poison
	* @see Index
	* @throws Exception if error occurs
	*/
	public ConcurrentHashMap<Integer, List<Index>> call() throws Exception{
		while(fileCount > 0) {
			Word w = new Word();
			int shingle = 0;
			boolean isInList = false;
			
			try {
				//take Word off queue
				w = queue.take();
				
				if(w instanceof Poison){
					fileCount--;
				}
				else{
					shingle = w.getShingle();
					
					if(fileMap.containsKey(shingle)){
						List<Index> list = new ArrayList<Index>();
						list = fileMap.get(shingle);
						
						isInList = false;
						
						//loop through arraylist to find where the book is, and increment the frequency
						for (Index indexList: list){
							if (indexList.getFilename() == w.getBook()){
								//Increase Frequency by 1
								indexList.setFrequency(indexList.getFrequency() + 1);
								isInList = true;
							}
						}
						
						//if the word is already in the map but it's for another book then add a new index for the new book
						if(isInList == false) {
							list.add(new Index(1, w.getBook()));
						}
						
						fileMap.put(shingle, list);
					}
					else {
						//If Shingle is not in map add a new element
						List<Index> list = new ArrayList<Index>();
						
						list.add(new Index(1, w.getBook()));
						fileMap.put(shingle, list);	
					}
				}//outer else
			} catch (InterruptedException e) {
				System.out.println("Error occured: " + e.getMessage());
			}
		}

		System.out.println("Completed");
		return fileMap;
	}
}