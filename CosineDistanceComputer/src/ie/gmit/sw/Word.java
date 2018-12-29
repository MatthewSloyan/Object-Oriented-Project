package ie.gmit.sw;

public class Word {
	private String book;
	private int shingle;

	//Constructor & Gets/Sets
	public Word(String book, int shingle) {
		super();
		this.book = book;
		this.shingle = shingle;
	}

	public Word() {
		super();
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public int getShingle() {
		return shingle;
	}

	public void setShingle(int shingle) {
		this.shingle = shingle;
	}
}
