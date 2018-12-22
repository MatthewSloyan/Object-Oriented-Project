package ie.gmit.sw;

import java.io.IOException;
import java.util.Scanner;

public class UI {
	private Scanner console = new Scanner(System.in);
	boolean keepRunning = true;
	
	public void display() throws IOException {
		System.out.println(" ======= Document Comparison Service =======");
 		
 		//Running time: O(N);
		while(keepRunning) {
			System.out.println("Please select an option:\n (1) Compare Text File\n (2) Compare Image\n (3) Exit Program\n");
			String option = console.next();
			process(option);
		}
	}

	private void process(String option) {
		try {
			int selection = Integer.parseInt(option);
			
			switch (selection)
			{
				case 1:
					break;
				case 2:
					break;
				default:
					keepRunning = false;
			} // menu selection switch
		}
		
		catch (Exception e) {
			System.err.println(e.getMessage()); //print out exception message
		}
	}

}
