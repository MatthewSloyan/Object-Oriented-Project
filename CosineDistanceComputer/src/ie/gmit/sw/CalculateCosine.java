package ie.gmit.sw;

import java.util.Map;

public class CalculateCosine {
	
	private String queryFile;
	private String[] files;
	private double queryMagnitude;
	private Map<Integer, Integer> fileMapDotProduct;
	private Map<Integer, Integer> fileMapMagnitude;
	
	public CalculateCosine(String queryFile, String[] files, double queryMagnitude,
			Map<Integer, Integer> fileMapDotProduct, Map<Integer, Integer> fileMapMagnitude) {
		super();
		this.queryFile = queryFile;
		this.files = files;
		this.queryMagnitude = queryMagnitude;
		this.fileMapDotProduct = fileMapDotProduct;
		this.fileMapMagnitude = fileMapMagnitude;
	}

	public void calculate() {
		StringBuilder sb = new StringBuilder();
		sb.append(queryFile + "\n");
		
		//COSINE CALCULATION
		for (String s : files) {
			String str = calculateCosine(s, queryMagnitude, fileMapDotProduct.get(s.hashCode()), fileMapMagnitude.get(s.hashCode()));
			sb.append(str);
		}
		
		new PrintResults().print(sb);
	}
	
	private String calculateCosine(String file, double queryMagnitude, Integer dotProduct, Integer fileMagnitude) {
		double cosine = 0;
		
		cosine = dotProduct / (Math.sqrt(queryMagnitude * fileMagnitude));
		
		System.out.printf("Query file: %s is %.2f percent similar to %s %n", queryFile, cosine*100, file);
		
		return String.format("%.2f %s %n", cosine*100, file);
	}
}
