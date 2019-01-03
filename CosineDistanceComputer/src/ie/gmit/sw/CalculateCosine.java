package ie.gmit.sw;

import java.util.Map;

/**
* Calculate Cosine distance using the counted up dot product and magnitudes
*
* @author Matthew Sloyan
*/
public class CalculateCosine {
	
	private String queryFile;
	private String[] files;
	private double queryMagnitude;
	private Map<Integer, Integer> fileMapDotProduct;
	private Map<Integer, Integer> fileMapMagnitude;
	
	/**
	* Constructor for class
	* 
	* @param String - query file string to print out file name rather than hashcode
	* @param String - array of all files in the subject directory
	* @param int - counted magnitude for the query file
	* @param Map<Integer, Integer> - Map of all files and the dot product count for each
	* @param Map<Integer, Integer> - Map of all files and the magnitude count for each
	*/
	public CalculateCosine(String queryFile, String[] files, double queryMagnitude,
			Map<Integer, Integer> fileMapDotProduct, Map<Integer, Integer> fileMapMagnitude) {
		super();
		this.queryFile = queryFile;
		this.files = files;
		this.queryMagnitude = queryMagnitude;
		this.fileMapDotProduct = fileMapDotProduct;
		this.fileMapMagnitude = fileMapMagnitude;
	}

	/**
	* Loop through each file and call calculateCosine and pass in values
	* A formated string of the returned with the results and appended to a string builder.
	* create an instance of PrintResults and call printToFile(sb) to ask the user if they want to print results to file.
	* Running time: Linear O(N)
	* 
	* @see #PrintResults
	* @see #calculateCosine
	*/
	public void print() {
		StringBuilder sb = new StringBuilder();
		sb.append(queryFile + "\n");
		
		for (String s : files) {
			String str = calculateCosine(s, queryMagnitude, fileMapDotProduct.get(s.hashCode()), fileMapMagnitude.get(s.hashCode()));
			sb.append(str);
		}
		
		new PrintResults().printToFile(sb);
	}
	
	/**
	* Method used in every iteration of print() to return the formatted string of the calculated Cosine Distance.
	* Running time: O(1)
	* 
	* @return String. Formatted string of query file, Cosine Distance and the subject file
	* @see #print
	*/
	private String calculateCosine(String file, double queryMagnitude, Integer dotProduct, Integer fileMagnitude) {
		double cosine = 0;
		
		//cosine distance formula
		cosine = dotProduct / (Math.sqrt(queryMagnitude * fileMagnitude));
		
		System.out.printf("Query file: %s is %.2f percent similar to %s %n", queryFile, cosine*100, file);
		
		return String.format("%.2f %s %n", cosine*100, file);
	}
}
