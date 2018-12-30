package ie.gmit.sw;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PrintResults {
	
	private Scanner console = new Scanner(System.in);
	
	public void print(StringBuilder sb) {
		boolean isValid;
		String option;
		String fileName;
		
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
