package ie.gmit.sw;
import java.io.IOException;

/**
* Includes the main method to run the program
* Creates an instance of UI and calls display()
* Running time: O(1)
*
* @author Matthew Sloyan
* @see #UI
*/

public class Runner {

	public static void main(String[] args) throws IOException {
		new UI().display();
	}
}
