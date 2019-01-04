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
	* @param book the name of the file as a hashcode
	* @param shingle three words as a hashcode
	*/
	public Word(int book, int shingle) {
		super();
		this.book = book;
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
