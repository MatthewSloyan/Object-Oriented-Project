package ie.gmit.sw;

import java.io.IOException;
import java.util.Scanner;

public class UI {
	private Scanner console = new Scanner(System.in);
	private boolean keepRunning = true;
	
	public void display() throws IOException {
			
 		//Running time: O(N);
		//while(keepRunning) {
			System.out.println(" ======= Document Comparison Service =======");
			System.out.println("Enter Subject Directory");
			String directory = console.next();
			
			System.out.println("Enter Query File or URL");
			String queryFile = console.next();
			
			new Processor().process(directory, queryFile);
			
			/*System.out.println("Would you like to start again? \n(1) Yes \n(2) No");
			String option = console.next();
			
			if (Integer.parseInt(option) != 1) {
				keepRunning = false;
			}*/
			
			//System.out.println("Please select an option:\n (1) Compare Text File\n (2) Compare Image\n (3) Exit Program\n");
			//String option = console.next();
			//process(option);
		//}
	}

	/*private void process(String option) {
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
	}*/

}
