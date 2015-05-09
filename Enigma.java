///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Enigma Machine
// Files:            Enigma.java
// Semester:         CS302 Spring 2015
//
// Author:           Xiaobo Wang
// Email:            xwang532@wisc.edu
// CS Login:         xiaobo
// Lecturer's Name:  Deb Deppeler
// Lab Section:      325
//
// Pair Partner:    QuHarrison Terry 
// Email:			qterry@wisc.edu
// CS Login:        quharrison
// Lecturer's Name: Deb Deppler
// Lab Section:     321
//
//////////////////// STUDENTS WHO GET HELP FROM OTHER THAN THEIR PARTNER //////
//                   must fully acknowledge and credit those sources of help.
//                   Instructors and TAs do not have to be credited here,
//                   but tutors, roommates, relatives, strangers, etc do.
//
// Persons:          Identify persons by name, relationship to you, and email.
//                   Describe in detail the the ideas and help they provided.
//
// Online sources:   avoid web searches to solve your problems, but if you do
//                   search, be sure to include Web URLs and description of 
//                   of any information you find.
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.*;

/**
 * Simulate the encryption of messages that was performed by the World War
 * II-era German Enigma cipher machine.
 * 
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Enigma_machine" target="wiki">
 * http://en.wikipedia.org/wiki/Enigma_machine</a></li>
 * <li><a href="https://www.youtube.com/watch?v=G2_Q9FoD-oQ" target="youtube">
 * https://www.youtube.com/watch?v=G2_Q9FoD-oQ</a></li>
 * </ul>
 */
public class Enigma {

	/**
	 * User enter some initial configuration information and is then prompted
	 * for the lines of text to be encrypted.
	 * 
	 * Each line is encrypted according to the rotor configuration. 
	 * The encoded line is displayed to the user.
	 *
	 * @param args
	 *            UNUSED
	 */

	public static void main(String[] args) {

		// Scanner connected to keyboard for reading user input
		Scanner scnr = new Scanner(System.in);

		// Display welcome message and instructions
		System.out.println("Willkommen auf der Enigma-Maschine");
		System.out.println("Please enter a Rotor Configuration.");
		System.out.println("This must be a list of numbers in the range"
				+ " from 0 to " + (RotorConstants.ROTORS.length - 1)
				+ ", separated by spaces. ");
		System.out.println("Note that rotor 0 is the identity rotor.");

		// Variables
		String getRotors = scnr.nextLine(); // Store users input

		int[][] convertedRotors = setUpRotors(parseRotorIndices(getRotors)); 
		int[] rotorOffsets = new int[parseRotorIndices(getRotors).length]; 
		System.out.println("Enter lines of text to be encoded:");

		// Main Loop
		while (true) {
			// Stores User input
			String realInput = scnr.nextLine();

			// Capitalize letters entered
			realInput = realInput.toUpperCase();
			
			// Placeholder text for encoded result
			System.out.print("Encoded result: ");

			// Reinitalize String result

			for (int i = 0; i < realInput.length(); i++) {

				if ((realInput.charAt(i) - 'A' >= 0)
						&& (realInput.charAt(i) - 'A') <= 25) {
					
			char output = (char) encode(convertedRotors,
				convertRotor(RotorConstants.REFLECTOR), realInput.charAt(i));
					
					//Prints Encoded result 
					System.out.print(output);
					
			//"advance" after each printing
			advance(convertedRotors, rotorOffsets,
				getRotorNotches(parseRotorIndices(getRotors)));
				} 
		// can print things in input except letters like ',' or space
		else 
				{
					System.out.print(realInput.charAt(i));
				}
			}
			System.out.println();
		}
	}

	/**
	 * Rotates (advances) a single rotor by one position. This is done by
	 * removing the first value of the array, shifting all the other values
	 * towards the beginning of the array, and placing the first value back 
	 * into the array as the last value.
	 *
	 * @param rotor
	 *            The rotor that must be rotated (or shifted).
	 */
	
	public static void rotate(int[] rotor) {
		//rotate the rotor forward by one position.
		int i;
		int a = rotor[0];
		for (i = 0; i < rotor.length - 1; i++) 
		{
			rotor[i] = rotor[i + 1];
		}
		// the last position in this array get the former first value
		rotor[i] = a;
	}

	/** Parses (interprets) the rotor configuration string 
	 * of integer values and
	 * returns an integer array of those values.
	 * 
	 * @param rotorIndicesLine
	 * @return Array of rotor index values.
	 */
	public static int[] parseRotorIndices(String rotorIndicesLine) {
		// using a Scanner on the input string 
		java.util.Scanner input1 = new java.util.Scanner(rotorIndicesLine);
		int countNumber = 0;

		if (input1.hasNextInt()) {
			
		//calculating the number of spaces to get the length of the array
			for (int i = 0; i < rotorIndicesLine.length(); i++) 
			{
				if (rotorIndicesLine.charAt(i) == ' ') 
				{
					countNumber++;
				}
			}

			int[] parseRotorIndices = new int[countNumber + 1];

//using .split(" ") to convert the string to an array without any space inside
			String[] helpArray = rotorIndicesLine.split(" ");

			for (int t = 0; t < helpArray.length; t++) 
			{
				parseRotorIndices[t] = Integer.parseInt(helpArray[t]);
				int a = parseRotorIndices[t];

				if (a < 0 || a > RotorConstants.ROTORS.length - 1) 
				{	
					//Display if user entered an input that is not an integer
					System.out.println("Invalid rotor. You must enter"
							+ " an integer between 0 and "
							+ (RotorConstants.ROTORS.length - 1) + ".");
					System.exit(-1);
				}
			}

			//Input validation: check whether there are same rotor numbers.
			for (int s = 0; s < countNumber; s++) 
			{
				for (int k = s + 1; k < countNumber + 1; k++) 
				{
					if (parseRotorIndices[s] == parseRotorIndices[k]) 
					{
						System.out
						.println("You cannot use the same rotor twice.");
						System.exit(-1);
					}
				}
			}
			return parseRotorIndices;
		}

		//Input validation: no rotor number input is not valid.
		else 
		{
			System.out.println("You must specify at least one rotor.");
			System.exit(-1);
			return null;
		}
	}

	/** 
	 * * Creates and returns array of rotor ciphers, based on rotor indices.
     *
	 * @param rotorIndices
	 *            The indices of the rotors to use. Each value in this array
	 *            should be a valid index into the 
	 *            {@code RotorConstants.ROTORS} array.
	 * 
	 * @return The array of rotors, each of which is itself an array of ints
	 *         copied from {@code RotorConstants.ROTORS}.
	 */
	public static int[][] setUpRotors(int[] rotorIndices) {
		
//Creating current rotors array with the same length as the rotor length
	int[][] rotorsConverted = 
				new int[rotorIndices.length][RotorConstants.ROTOR_LENGTH];

	// creating the rotor ciphers
	for (int i = 0; i < rotorIndices.length; i++)
	{
		for (int j = 0; j < RotorConstants.ROTOR_LENGTH; j++) 
		{
			rotorsConverted[i][j] = 
					convertRotor(RotorConstants.ROTORS[rotorIndices[i]])[j];
			}
		}
		return rotorsConverted;
	}

/**
	* Creates and returns a 2D array containing the notch positions for each
	* rotor being used.
	* 
    * @param rotorIndices
    *            Indices of the rotors. Each value in this array should be a
	*            valid index into the {@code RotorConstants.ROTORS} array.
    * 
	* @return An array containing the notch positions for each selected rotor
	*/
	
	public static int[][] getRotorNotches(int[] rotorIndices) {
		//Gets the Rotor notches
		int[][] getRotorNotches = new int[rotorIndices.length][];
		
		// create the array based on the length of each line of notch.
		for (int i = 0; i < rotorIndices.length; i++) 
		{
			getRotorNotches[i] = 
				new int[RotorConstants.NOTCHES[rotorIndices[i]].length];
		}

		// convert the notch position to a 2D array.
		for (int i = 0; i < getRotorNotches.length; i++) 
		{
			for (int j = 0; j < getRotorNotches[i].length; j++) 
			{
				getRotorNotches[i][j] = 
						RotorConstants.NOTCHES[rotorIndices[i]][j];
			}
		}
		return getRotorNotches;
	}

	/**
	 *  Converts a rotor cipher from its textual representation into an 
	 * integer-based rotor cipher.
	 *
	 * @param rotorText
	 *            Text representation of the rotor. This will be like the
	 *            Strings in {@code RotorConstants.ROTORS}; that is, it will 
	 *            be a String containing all 26 upper-case letters.
	 * 
	 * @return array of values between 0 and 25, inclusive that represents 
	 *         the integer index value of each character.
	 */
	
	public static int[] convertRotor(java.lang.String rotorText) {
		int[] reflectorRotor = new int[rotorText.length()];

		// convert each letter in rotor to the corresponding number.
		for (int i = 0; i < rotorText.length(); i++) 
		{
			reflectorRotor[i] = (char) (rotorText.charAt(i) - 'A');
		}
		return reflectorRotor;
	}

	/**
	 * Encodes a single uppercase character according to the current state 
	 * of the Enigma encoding rotors.
	 * 
	 * @param reflector
	 *            The special reflector rotor in integral rotor cipher form.
	 * 
	 * @param input
	 *            The character to be encoded. Must be an upper-case letter. 
	 *            DO NOT CONVERT TO UPPERCASE HERE.
	 * 
	 * @return The result of encoding the input character. ALL encoded
	 *         characters are upper-case.
	 */
	
	public static char encode(int[][] rotors, int[] reflector, char input) {
		//Encodes a char based on rotors and reflector
		int findInput = input - 'A';
		int idex0Rotor[] = convertRotor(RotorConstants.ROTORS[0]);

		for (int i = 0; i < rotors.length; i++)
		{
			findInput = rotors[i][findInput];
		}

		findInput = reflector[findInput];

		//reversed order of rotor
		for (int i = rotors.length - 1; i >= 0; i--) 
		{
			int a = rotors[i].length - 1;
			while (rotors[i][a] != idex0Rotor[findInput]) 
			{
				a--;
			}
			findInput = idex0Rotor[a];
			input = (char) (idex0Rotor[a] + 'A');
		}

		return input;

	}

	/**
	 *  Advances the rotors. 
	 *  
	 * @param rotors
	 *         The array of rotor ciphers in their current configuration.The
	 *         rotor at index 0 is the first rotor to be considered for
	 *         advancement. It will always rotate exactly once. The remaining
	 *         rotors may advance when notches on earlier rotors are reach.
	 *         Later rotors will not not advance as often unless there is a
	 *         notch at each index. (Tip: We try such a configuration during
	 *         testing). The data in this array will be updated to show the
	 *         rotors' new positions.
	 * 
	 * @param rotorOffsets
	 *         The current offsets by which the rotors have been rotated.
	 *         These keep track of how far each rotor has rotated since the
	 *         beginning of the program. The offset at index i will
	 *         correspond to rotor at index 0 of rotors. e.g. offset 0
	 *         pertains to the 0th rotor cipher in rotors. Will be updated
	 *         in-place to reflect the new offsets after advancing. The
	 *         offsets are compared to notches to know when next rotor must
	 *         also be advanced.
	 * 
	 * @param rotorNotches
	 *         The positions of the notches on each of the rotors. Each row
	 *         of this array represents the notches of a certain rotor. The
	 *         ith row will correspond to the notches of the ith rotor cipher
	 *         in rotors. Only when a rotor advances to its notched position
	 *         does the next rotor in the chain advance.
	 * @return
	 */
	public static void advance
	(int[][] rotors, int[] rotorOffsets, int[][] rotorNotches) 
	{

	     int k = 0;

	        while(true)
	        {       
	        	//Rotates rotors 
	            int [] rotorsHold = new int [rotors[k].length];

	            for (int j = 0; j < rotors[k].length; j++)
	            {
	                rotorsHold[j] = rotors[k][j];
	            }
	                rotate(rotorsHold);
	                
	           //Gets the rotated rotors
	           for(int j = 0; j < rotors[k].length; j++)
	                {
	                    rotors[k][j] = rotorsHold[j];
	                }  
	           
	            // Transfers current position to
	            // rotorOffsets and refreshes every 26 characters  
	           if (k < rotorOffsets.length)
	            {
	                rotorOffsets[k] = (rotorOffsets[k] + 1) % 26; 
	            }
	            
	            int i = 0;

	            for(int j = 0; j < rotorNotches[k].length; j++) 
	            {
	            	//Compares rotorOffsets position to rotorNotches
	            	//Advances the position if it's the rotor is the same
	                if(rotorOffsets[k] != rotorNotches[k][j]) 
	                {
	                    i=i+1;
	                }
	            }

	            if(( k + 1 ) >= rotors.length || i == rotorNotches[k].length)
	            {
	               break;
	            }
	            
	            k = k + 1;
	         }
	    }


} // end of Enigma class


