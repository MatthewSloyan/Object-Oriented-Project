package ie.gmit.sw;

public class Index {
	private int frequency;
	private int filename;

	//Constructor, gets and sets
	public Index(int frequency, int filename) {
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

	public int getFilename() {
		return filename;
	}

	public void setFilename(int filename) {
		this.filename = filename;
	}
}
