package ie.gmit.sw;

/**
* Poison class - used to signify the end of a file for the ShingleTaker class
*
* @author Matthew Sloyan
*/
public class Poison extends Word{

	/**
	* Constructor for class
	* 
	* @param int - the name of the file as a hashcode
	* @param int - three words as a hashcode
	*/
	public Poison(int book, int shingle) {
		super(book, shingle);
	}
}
