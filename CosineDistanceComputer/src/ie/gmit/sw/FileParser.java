package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

/**
* Parse the files in the directory line by line into three word shingles and add them to the queue
*
* @author Matthew Sloyan
*/
public class FileParser implements Runnable{
	
	private BlockingQueue<Word> queue;
	private String dir;
	private String file;
	
	/**
	* Constructor
	* 
	* @param queue Queue of words to add to
	* @param dir file directory
	* @param file name of file
	*/
	public FileParser(BlockingQueue<Word> queue, String dir, String file){
		this.queue = queue;
		this.dir = dir;
		this.file = file;
	}

	/**
	* Runnable thread that uses the input directory + the file name and parses it line by line.
	* Each line is stripped of everything but words and spaces.
	* Three word shingles are taken from this line, and any remainders are added as single or double shingles (E.g thank you, you)
	* Each shingle is put on the BlockingQueue as an instance of Word (hashcode of file, hashcode of shingle) for speed
	* An instance of Poison is put on the queue to signify the file has been fully parsed.
	* 
	* For simplicity and SRP this class only parses the directory files as it deals with queues.
	* However the QueryFileParser class deals with query files/urls as it uses a smaller map and returns it's directly
	* Running time: Quadratic O(N^2);
	* T(n) = n^2 + 6n
	* 
	* @see Word
	* @see Poison
	*/
	public void run() {
		BufferedReader br = null;
		String line = null;
		String savedString = "";
		String stripptedString = null;
		Pattern pattern = Pattern.compile(" "); //pattern used for additional speed on split
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(dir+"/"+file)));
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			while((line = br.readLine()) != null) {
				String[] words = pattern.split(line.toUpperCase().replaceAll("[^A-Za-z0-9 ]", ""));
				
				int arrayLength = words.length;
				
				//if the remainder is 1 then there is one extra single word to be put on the queue separately.
				if (arrayLength % 3 == 1) {
					savedString = words[arrayLength - 1];
					arrayLength -= 1;
				}
				else if (arrayLength % 3 == 2) {
					savedString = words[arrayLength - 2] + " " + words[arrayLength - 1];
					arrayLength -= 2; 
				}
				
				//Loop by three to get three word shingles
				for (int i = 0; i < arrayLength; i+=3) { 
					stripptedString = words[i];
					stripptedString += " " + words[i+1];
					stripptedString += " " + words[i+2];
					
					queue.put(new Word (file.hashCode(), stripptedString.hashCode()));
				}
				
				//if savedString is not equal to "" then there must be a remainder words
				if (savedString != "") {
					queue.put(new Word (file.hashCode(), savedString.hashCode()));
					savedString = "";
				}
			}
			queue.put(new Poison(file.hashCode(), stripptedString.hashCode())); //finishes
			br.close();
		} catch (IOException | InterruptedException e) {
			System.out.println("Error occured: " + e.getMessage());
		}
	}
}
