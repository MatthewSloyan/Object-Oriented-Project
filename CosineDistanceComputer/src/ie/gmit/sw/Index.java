package ie.gmit.sw;

public class Index {
	private int frequency;
	private String filename;

	//Constructor, gets and sets
	public Index(int frequency, String filename) {
		super();
		this.frequency = frequency;
		this.filename = filename;
	}

	public Index() {
		super();
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
