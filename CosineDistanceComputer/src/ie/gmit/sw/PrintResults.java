package ie.gmit.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
* Option to print the results to file
*
* @author Matthew Sloyan
*/
public class PrintResults {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;
	private String option;
	private String fileName;
	
	/**
	* Ask the user if they would like to print the results to a file (Could be used for comparision later)
	* If 1 then ask for file name (must include file extension E.g .txt) and then write to file.
	*/
	public void printToFile(StringBuilder sb) {
		do {
			System.out.println("\nWould you like to save the results to a file?\n (1) Yes\n (2) No");
			option = console.next();
			
			isValid = true;
			
			if (Integer.parseInt(option) == 1) {
				System.out.println("Please Enter File Name");
				fileName = console.next();
				
				isValid = false;
				
				try {
					//print the passed stringbuilder to a file
					BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
					writer.write(sb.toString());
					
					//close the file
					writer.close();
				} catch (IOException e) {
					System.out.println("Could not save to file, please try again.");
				}
			} 
			else if (Integer.parseInt(option) == 2) {
				isValid = false;
			} 
			else {
				System.out.println("Invalid option, please try again.");
			} 
		} while (isValid);
	}
}
