package ie.gmit.sw;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class UI {
	private Scanner console = new Scanner(System.in);
	private boolean keepRunning = true;
	private boolean isValid;
	private boolean url;
	private String directory;
	private String queryFileURL;
	
	public void display() throws IOException {
			
 		//Running time: O(N);
		while(keepRunning) {
			System.out.println(" ======= Document Comparison Service =======");
			
			do {
				System.out.println("Enter Subject Directory");
				directory = console.next();
				
				File f = new File(directory);
				isValid = true;
				
				if (f.isDirectory()) {
					isValid = false;
				}
				else {
					System.out.println("File Directory does not exist, please try again.");
				}
			} while (isValid);
			
			do {
				System.out.println("\nPlease select an option:\n (1) Compare File\n (2) Compare URL");
				String option = console.next();
				
				isValid = true;
				
				if (Integer.parseInt(option) == 1) {
					do {
						System.out.println("Please Enter Path to Query File");
						queryFileURL = console.next();

						File f = new File(queryFileURL);
						isValid = true;
					
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
			
			new Processor().process(directory, queryFileURL, url);
			
			System.out.println("\nWould you like to start again? \n(1) Yes \n(2) No");
			String option = console.next();
			
			if (Integer.parseInt(option) != 1) {
				keepRunning = false;
			}
			
			//System.out.println("Please select an option:\n (1) Compare Text File\n (2) Compare Image\n (3) Exit Program\n");
			//String option = console.next();
			//process(option);
		}
	}
}
