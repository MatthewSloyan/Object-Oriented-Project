package ie.gmit.sw;

public class CalculateCosine {
	
	public void calculateCosine(String queryFile, String file, Integer dotProduct, double magnitude1, Integer magnitude2) {
		double cosine = 0;
		
		cosine = dotProduct / (Math.sqrt(magnitude1 * magnitude2));
		
		System.out.printf("Query file: %s is %.2f percent similar to %s %n", queryFile, cosine*100, file);
	}
}
