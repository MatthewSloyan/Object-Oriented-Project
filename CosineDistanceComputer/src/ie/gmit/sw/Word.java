package ie.gmit.sw;

/**
* Word class - used to add words to the BlockingQueue
*
* @author Matthew Sloyan
*/
public class Word {
	private int book;
	private int shingle;

	/**
	* Constructor for class
	* 
	* @param int - the name of the file as a hashcode
	* @param int - three words as a hashcode
	*/
	public Word(int i, int shingle) {
		super();
		this.book = i;
		this.shingle = shingle;
	}

	public Word() {
		super();
	}

	//gets and sets
	public int getBook() {
		return book;
	}

	public void setBook(int book) {
		this.book = book;
	}

	public int getShingle() {
		return shingle;
	}

	public void setShingle(int shingle) {
		this.shingle = shingle;
	}
}
