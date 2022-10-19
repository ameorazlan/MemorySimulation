package MemorySimulation;

import java.util.ArrayList;
//This class represents a process. Processes can keep track of their segments here.
public class Process {
	//Process ID
	private int ID;
	
	//Numbers generated when parser object is called on user input
	private ArrayList<Integer>[] numbers;
	
	//ArrayList storing all segments belonging to this process
	private ArrayList<Segment> segments = new ArrayList<Segment>();
	
	//Segment Table containing information on this processes' segments
	private ArrayList<ArrayList<String>> segmentTable = new ArrayList<ArrayList<String>>();
	
	//User input
	private String input;
	
	/**
	 * Constructor which stores user input to input, and calls setup method
	 * @param input
	 */
	public Process(String input) {
		this.input = input;
		this.setUp();

	}
	
	/**
	 * Method for setting up the process and its segments
	 * Parses the input string into numbers which correlate to process ID, and segment length
	 */
	private void setUp() {
		//Parses input string 
		Parser parser = new Parser();
		this.numbers = parser.parseInputString(input);
		//Sets process ID to the first number from the parsed input string
		try {
			this.ID = this.numbers[0].get(0);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("java.lang.IllegalArgumentException: Unclear input given");
			return;
		}
		
		//iterates through numbers from parsed input string
		for (int i = 1; i < numbers.length; ++i) {
			//creates a new segment with numbers from parsed input string
			String input = "P" + String.valueOf(this.ID) + " S" + String.valueOf(i);
			Segment s = new Segment(input, numbers[i].get(0));
			//If there are more numbers stored in the numbers ArrayList, then these are the permissions
			//Set the process' permissions with the 3 numbers
			if (numbers[i].size() > 1) {
				s.setPermissions(numbers[i].get(1), numbers[i].get(2), numbers[i].get(3));
			}
			//Add the newly created segment to the segments ArrayList
			segments.add(s);
		}
	}

	public int getID() {
		return ID;
	}
	
	/**
	 * 
	 * @param index
	 * @return the size of the segment located at the index
	 */
	public int getSegmentSize(int index) {
		return this.segments.get(index).getLimit();
	}
	
	/**
	 * 
	 * @param index
	 * @return the segment located at the index 
	 */
	public Segment getSegment(int index) {
		try {
			return this.segments.get(index - 1);
		} catch(IndexOutOfBoundsException e) {
			//if user input string is too large, then inform user and only use the first part of input
			System.out.println("User input is unclear, attempting to proceed");
			return null;
		}
	}
	
	public ArrayList<Segment> getAllSegments() {
		return this.segments;
	}
	
	/**
	 * Method for printing out the segment table of this process
	 * This method creates and prints out the segment table
	 * @return
	 */
	public String printSegmentTable() {
		//clears segment table
		segmentTable.clear();
		
		//For each segment, create a new ArrayList storing the segment's information
		for (Segment s : segments) {
			//if limit == 0 then the segment doesn't exist anymore and should not be in segment table
			if (s.getLimit() == 0) {
				continue;
			}
			ArrayList<String> info = new ArrayList<String>();
			//add segment ID
			info.add(s.getID());
			//add segment base address, if base address is -1 that means segment is not in main memory
			if (s.getBaseAddress() == -1) {
				info.add(String.valueOf('-'));
			} else {
				info.add(String.valueOf(s.getBaseAddress()));
			}
			//add segment's limit and its valid-invalid bit
			info.add(String.valueOf(s.getLimit()));
			info.add(String.valueOf(s.getValidInvalid()));
			//if segment has permissions, then add its permissions
			if (s.hasPermissions()) {
				info.add(s.getPermissions());
			}
			//add the segment's info ArrayList to the segmentTable ArrayList
			segmentTable.add(info);
		}
		//For each ArrayList in segmentTable add it to the toReturn String
		String toReturn = "Segment Table of Process " + this.getID() +": ";
		for (ArrayList<String> a : segmentTable) {
			toReturn += a.toString();
		}
		//Return toReturn
		return toReturn;
	}
	
	/**
	 * Removes Segment s from this.segments
	 * @param s
	 */
	public void removeSegment(Segment segment) {
		this.segments.remove(segment);
	}

}
