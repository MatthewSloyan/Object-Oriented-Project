package ie.gmit.sw;

/**
* Index class - used to create an arraylist in the ShingleTaker class
*
* @author Matthew Sloyan
*/
public class Index {
	private int frequency;
	private int filename;

	/**
	* Constructor for class
	* 
	* @param int - the number of times the word occurs
	* @param int - the name of the file as a hashcode
	*/
	public Index(int frequency, int filename) {
		super();
		this.frequency = frequency;
		this.filename = filename;
	}

	public Index() {
		super();
	}

	//Gets and sets 
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
