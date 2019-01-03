package ie.gmit.sw;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

/**
* Displays and handles UI options for program
*
* @author Matthew Sloyan
*/
public class UI {
	private Scanner console = new Scanner(System.in);
	private boolean keepRunning = true;
	private boolean isValid;
	private boolean url;
	private String directory;
	private String queryFileURL;
	
	/**
	* Displays menu options to users
	* Allows the user to enter a subject directory (Checks if exists)
	* Allows the user to enter a query file or URL to compare against directory files (Checks if exists)
	* Create instance of Processor class to process inputs
	* Running time: O(1)
	*
	* @see #Processor
	* @throws Exception if input is invalid
	* @exception IOException
	*/
	public void display() throws IOException {
			
		while(keepRunning) {
			System.out.println(" ======= Document Comparison Service =======");
			
			do {
				System.out.println("Enter Subject Directory");
				directory = console.nextLine();
				
				File f = new File(directory);
				isValid = true;
				
				//check if directory exists, keeps asking till it is valid
				if (f.isDirectory()) {
					isValid = false;
				}
				else {
					System.out.println("File Directory does not exist, please try again.");
				}
			} while (isValid);
			
			do {
				System.out.println("\nPlease select an option:\n (1) Compare File\n (2) Compare URL");
				String option = console.nextLine();
				
				isValid = true;
				
				if (Integer.parseInt(option) == 1) {
					do {
						System.out.println("Please Enter Path to Query File");
						queryFileURL = console.nextLine();

						File f = new File(queryFileURL);
						isValid = true;
					
						//check if file exists, keeps asking till it is valid
						if (f.exists()) {
							isValid = false;
							url = false;
						} else {
							System.out.println("File does not exist, please try again.");
						}
					} while (isValid);
				} else if (Integer.parseInt(option) == 2) {
					do {
						System.out.println("Please Enter URL");
						queryFileURL = console.next();

						isValid = true;

						//check if URL exists, keeps asking till it is valid
						try {
							new URL(queryFileURL).toURI();
							isValid = false;
							url = true;
						} catch (URISyntaxException e) {
							isValid = true;	
						}
					} while (isValid);
				} else {
					System.out.println("Invalid option, please try again.");
				} 
			} while (isValid);
			
			System.out.println("\n==========================");
			System.out.println("Processing, please wait.");
			System.out.println("==========================\n");
			
			//Create instance of Processor class and pass in user input details
			new Processor().process(directory, queryFileURL, url);
			
			System.out.println("\nWould you like to start again? \n(1) Yes \n(2) No");
			String option = console.next();
			
			if (Integer.parseInt(option) != 1) {
				keepRunning = false;
			}
		}
	}
}
