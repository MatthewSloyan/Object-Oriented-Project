package ie.gmit.sw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* Calculate the dot product and magnitudes of the maps to get the cosine distance for each file.
*
* @author Matthew Sloyan
*/
public class CalculateDotProductAndMagnitude{
	
	private ConcurrentHashMap<Integer, List<Index>> fileMap;
	private ConcurrentHashMap<Integer, Integer> queryMap;
	private String queryFile;
	private String [] files;
	private long startTime;
	
	/**
	* Constructor for class
	* 
	* @param ConcurrentHashMap<Integer, List<Index>> - Map of all unique words/files of the subject directory
	* @param ConcurrentHashMap<Integer, Integer> - Map of all unique words and counts of the query file
	* @param String - query file string to print out file name rather than hashcode
	* @param String - array of all files in the subject directory
	* @param long - running time of program
	*/
	public CalculateDotProductAndMagnitude(ConcurrentHashMap<Integer, List<Index>> fileMap,
			ConcurrentHashMap<Integer, Integer> queryMap, String queryFile, String[] files, long startTime) {
		super();
		this.fileMap = fileMap;
		this.queryMap = queryMap;
		this.queryFile = queryFile;
		this.files = files;
		this.startTime = startTime;
	}

	/**
	* Create two maps, one for the subject directory dot product and the second for the magnitude (initalize with loop)
	* Loop through the fileMap to count up the magnitude
	* Loop through the queryMap and if both maps contain the same word then add to the count
	* Create an instance of CalculateCosine and pass in the two new maps to calculate and print the Cosine Distance 
	* Running time: Quadratic O(N^2);
	* T(n) = 2n^2 + 4
	* 
	* @see #CalculateCosine
	*/

	public void calculate() {
		double queryMagnitude = 0;
		
		Map<Integer, Integer> fileMapDotProduct = new HashMap<>();
		Map<Integer, Integer> fileMapMagnitude = new HashMap<>();
		
		//initalize maps
		for (String s: files){
			fileMapDotProduct.put(s.hashCode(), 0);
			fileMapMagnitude.put(s.hashCode(), 0);
		}
		
		//count up magnitude for each file in the the subject directory
		for (int key : fileMap.keySet()) {
		    List<Index> list = fileMap.get(key);
		    
		    //iterate over file names in list
		    for (Index indexList: list){
		    	
				if(fileMapMagnitude.containsKey(indexList.getFilename())) {
					int magnitudeCount = fileMapMagnitude.get(indexList.getFilename());
					
					magnitudeCount += indexList.getFrequency();
					
					fileMapMagnitude.put(indexList.getFilename(), magnitudeCount);
				}
			}
		}
		
		for (int key : queryMap.keySet()) {
			//count up query file magnitude
			Integer num = queryMap.get(key);
			queryMagnitude += num;
			
			//count up dot product only if word in both maps
			if (queryMap.containsKey(key) && fileMap.containsKey(key)) {
				
				List<Index> list = fileMap.get(key);
				
				//iterate over file names in list
				for (Index indexList: list){
					 
					if(fileMapDotProduct.containsKey(indexList.getFilename())) {
						int dotProductCount = fileMapDotProduct.get(indexList.getFilename());
						
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
		
		//running time
		System.out.println("\nRunning time (ms): " + (System.nanoTime() - startTime));
		final long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.println("Used memory: " + usedMem + "\n");
		
		new CalculateCosine(queryFile, files, queryMagnitude, fileMapDotProduct, fileMapMagnitude).print();
	}
}
