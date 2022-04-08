import java.util.Scanner;
//-----------------------------------------------------
//Assignment 3
//Written by: Cristian Gasparesc 40209205 Amirhossein Tavakkoly 40203604
//Due Date: Due 11:59 PM – Friday, March 25, 2022
//-----------------------------------------------------

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

/**
* @author Cristian Gasparesc, Amirhossein Tavakkoly
* @version 16/03/22
*/
public class Driver {
	static PrintWriter ex=null;
	/**
	 * Main Method
	 * @param args run code
	 */
	public static void main(String[] args) {
		
	//variable declaration
	//scanner declaration
	Scanner kb = new Scanner(System.in); 
	Scanner integer = new Scanner(System.in); 
	int numberFiles;
	String[] files;
	File nameOfCSVFile;
	
	//user input number of files
	System.out.println("How many files would you like to read: ");
	numberFiles = integer.nextInt();
	
	System.out.println("You have " + numberFiles + " files");
	files = new String [numberFiles];
	
	//if number of files 0 or less
	if(numberFiles == 0 || numberFiles <0) {
		System.out.println("invalid number of Files, system will now terminate. Bye Bye");
		System.exit(0);   
	}
	
	//user input for name of files according to the number of files
	for(int i=0; i < numberFiles; i++) 
	{
	System.out.println("Enter name of file number " + (i+1) + " that will be converted:");	
	nameOfCSVFile = new File(kb.nextLine());
	
	files[i] = nameOfCSVFile.getName();
	
	String[] converter = (nameOfCSVFile.getName()).split("[.]"); //splits at every . and append each term to array
	
	//testing if array is empty
	//for (int j=0; j<converter.length; j++) {
		//System.out.println(converter[j].toString());
	//}
	
	String nameOfHTMLFile = converter[0] + ".html"; //replace the .csv by .html
	
	
	System.out.println("your file converted to html: " + nameOfCSVFile);
	
	File htmlfile = new File(nameOfHTMLFile);
	
    Scanner sc = null;
	PrintWriter pw = null;
	
	//opening exception log (even if no exception occurs it must be open)
	try {
		ex = new PrintWriter(new FileOutputStream("Exceptions.log", true));
	}
	catch(FileNotFoundException e) {
	 System.out.println("could not create Exceptions log");
	}
	
	//attempt to read the CSV file
	try {
		sc = new Scanner(new FileInputStream(nameOfCSVFile.getName()));
		
	}
	catch (FileNotFoundException e){
		ex.print("Could not open input file " + nameOfCSVFile.getName() + " for reading.Please check that the file exists and is readable. This program will terminate after closing any opened files.");
		System.out.println("Could not open input file " + nameOfCSVFile.getName() + " for reading.Please check that the file exists and is readable. This program will terminate after closing any opened files.");
		
	   if(sc != null) {
		sc.close();
	   }
	
	   if(ex != null) {
		ex.close();
	   }
	
	   System.exit(0);
	
	}
	
	//attempting to create the output HTML file
	try {
		pw = new PrintWriter(new FileOutputStream(htmlfile.getName()));
	}
	catch (FileNotFoundException e){
		
		System.out.println("Could not open output file " + htmlfile.getName() + " for reading. Please check that the file exists and is readable. This program will terminate after closing any opened files.");
	    System.exit(0);
	
	}
	
	
	
	//attempting to convert the CSV file(s) to HTML using our converting method
	try {
		ConvertCSV2HTML(nameOfCSVFile, pw, sc, files);

	} catch (CSVAttributeMissing e) {
		System.out.print(e.getMessage());
		ex.print(e.getMessage());
		
		if(sc != null) {
			sc.close();
			htmlfile.delete();
		}
		if(pw != null) {
			pw.close();
			htmlfile.delete();
		}
		System.exit(0);
	}
	
	
	}
	
    
	
	System.out.println("HTML files were made successfully");
	
	
	BufferedReader br = null;
	
	//asking user for the name of html he wants to create
	System.out.println("\nPlease enter the name of the html file you would like to open: ");
	File htmlbuffer = new File(kb.nextLine());
	System.out.println();

	//attempting to use Buffered reader to read through the HTML file (given by user)
	try {
		br = new BufferedReader(new FileReader(htmlbuffer.getName()));
		while (br.readLine() != null) {
			String s = br.readLine();
			System.out.println(s); 

		}
		br.close();
	}

	// catch exception and we then offer to the user a second chance(last chance)
	catch (FileNotFoundException e) {
		ex.println("Could not open input file " + htmlbuffer.getName() + " for reading.");
		ex.println("Please check that the file exists and is readable. The program will terminate after closing any opened files.");
		System.out.println("Could not open file " + htmlbuffer.getName() + ".html for reading.");
		System.out.println("Please check that the file exists and is readable.");
		System.out.println("\nPlease enter the name of the html file you would like to open: ");
		BufferedReader br1 = null;
		File htmlbuffer1 = new File(kb.nextLine());
		//second chance
		try {
			br1 = new BufferedReader(new FileReader(htmlbuffer1.getName()));
			while (br1.readLine() != null) {
				String s = br1.readLine();
				System.out.println(s);
			}
			br1.close();
		}

		catch (FileNotFoundException e1) {
			ex.println("Could not open input file " + htmlbuffer1.getName() + " for reading.");
			ex.println("Please check that the file exists and is readable. The program will terminate after closing any opened files.");
			System.out.println("Could not open file " + htmlbuffer1.getName() + ".html for reading.");
			System.out.println("Please check that the file exists and is readable.");
			ex.close();
			System.out.println("This program will terminate after closing any opened files.");
			System.exit(0);
		}

		catch (IOException e1) {
			e1.printStackTrace();
		}
	} catch (IOException e) {
		e.printStackTrace();
	}

	//close objects
	ex.close();
	kb.close();
	integer.close();

}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param file file parameter
	 * @param pw prinwriter parameter
	 * @param sc scanner parameter
	 * @param files Array of files parameter
	 * @throws CSVAttributeMissing Exception(Missing Attribute)
	 */
	public static void ConvertCSV2HTML(File file, PrintWriter pw, Scanner sc, String[] files) throws CSVAttributeMissing
	{
		
		int d =0;
		int count = 0; 
		String str;
		String[] attr;
		String[] data;
		
		//adding HTML and CSS to file
		while(count<files.length) {
			
				pw.println("<!DOCTYPE html>");
				pw.println("<html>");
				pw.println("<style>");
				pw.println("table {font-family: arial, sans-serif;border-collapse: collapse;}");
				pw.println("td, th {border:1px solid #000000;text-align: center;padding: 8px");
				pw.println("tr:nth-child(even) {background-color: #dddddd;}");
				pw.println("span{font-size:small}");
				pw.println("</style>");
				pw.println("<body>");
				pw.println("<table>");
				
				str = sc.nextLine();
				pw.println("<caption>");
				pw.println(str);
				pw.println("</caption>");
				pw.println("<tr>");
				
				str = sc.nextLine();
				
				//check for missing attribute
				if(str.contains(", ,")){
					throw new CSVAttributeMissing("in file " + file.getName() + " missing attribute, not converted to "
							+ " html.");
				}
				//nothing wrong, code applies
				else {
					 attr = str.split(",");
					
					for(int c = 0; c< attr.length; c++) {
						pw.println("<th>" + attr[c] + "</th>");
					}
					
					
				}
				
				pw.println("</tr>");
				
				while(str != null) {
				
					str = sc.nextLine();
					//check for missing data
					try {
					if(str.contains(", ,")) 
					{
						throw new CSVDataMissing("in file " + file.getName() + " missing data at line " + d + ", not converted to "
								+ " html.");
					}}
					catch(CSVDataMissing e) {
						System.out.print(e.getMessage());
						ex.print(e.getMessage());
						continue;
					}
					//formatting as we want in HTML
					if(str.contains("Note:")) {
						pw.println("</table>\n");
						pw.println("<span>" + str + "</span>\n");
						pw.println("</body>\n");
						pw.println("</html>\n");
						str=null;
						
					}
					else {
						pw.print("<tr>\n");
						data = str.split(",");
						for(int c =0; c<data.length; c++) {
							pw.print("<td>" + data[c] + "</td>\n");
						}
						pw.print("</tr>\n");
					}
					
					
					
				d++;}
					
		
			
		pw.close();
		sc.close();
		count++;}
	
	}

}