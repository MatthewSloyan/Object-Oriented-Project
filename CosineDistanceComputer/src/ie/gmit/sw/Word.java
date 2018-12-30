package ie.gmit.sw;

public class Word {
	private int book;
	private int shingle;

	//Constructor & Gets/Sets
	public Word(int i, int shingle) {
		super();
		this.book = i;
		this.shingle = shingle;
	}

	public Word() {
		super();
	}

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
