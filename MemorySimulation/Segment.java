package MemorySimulation;

import java.util.ArrayList;
//Class representing a segment
public class Segment {
	
	//Segment ID
	private String ID;
	
	//Base Address in memory 
	//Default base address of -1 means process not in memory
	private int baseAddress = -1;
	
	//limit of the segment
	private int limit;
	
	//old limit of the segment
	private int oldLimit;
	
	//valid-invalid bit
	private char validInvalid = 'i';
	
	//read-write-execute permissions
	private ArrayList<Character> permissions = new ArrayList<Character>();

	/**
	 * Constructor for segment class, taking in SegmentID and segment limit as parameters
	 * @param input
	 * @param limit
	 */
	public Segment(String input, int limit) {
		this.ID = input;
		this.limit = limit;
	}

	public String getID() {
		return ID;
	}

	public int getLimit() {
		return limit;
	}
	
	public int getBaseAddress() {
		return baseAddress;
	}
	
	public void setBaseAddress(int baseAddress) {
		this.baseAddress = baseAddress;
	}

	public int getOldLimit() {
		return oldLimit;
	}


	public char getValidInvalid() {
		return validInvalid;
	}


	public void setValidInvalid(char validInvalid) {
		this.validInvalid = validInvalid;
	}
	
	/**
	 * Sets oldLimit variable to current limit
	 * Adds parameter number to current limit
	 * @param number
	 */
	public void modifyLimit(int number) {
		oldLimit = limit;
		limit+= number;
	}
	/**
	 * Sets segment permissions using parameters read, write, execute
	 * Parameters are numbers with 1 representing has permission, 0 representing has no permission, -1 representing invalid combination of permissions, and -2 representing unclear user input
	 * Note that this method prints out the exceptions rather than throwing it, so that the program does not stop running
	 * Note that if invalid combination of permissions are entered, then the permissions will not be set 
	 * @param read
	 * @param write
	 * @param execute
	 * @throws IllegalArgumentException
	 */
	public void setPermissions(int read, int write, int execute){
		//If any of the variables are -1, then print that there is an invalid combination of permissions
		//If any of the variables are -2, then print unclear user input
		if (read == -1 || write == -1 || execute == -1) {
			System.out.println("IllegalArgumentException: Segment cannot be executed without read permission");
			return;
		} else if (read == -2 || write == -2 || execute == -2) {
			System.out.println("IllegalArgumentException: Unclear segment permissions assignment");
			return;
		}
		//If read==1, then add r, else add -
		if (read == 1) {
			permissions.add('r');
		} else {
			permissions.add('-');
		}
		//If write==1, then add w, else add -
		if (write == 1) {
			permissions.add('w');
		} else {
			permissions.add('-');
		}
		//If execute==1, add x, else add -
		if (execute == 1) {
			permissions.add('x');
		} else {
			permissions.add('-');
		}
	}
	/**
	 * Method for determining if this segment has its permissions set
	 * @return true if segment has permissions
	 * @return false if segment has no permissions
	 */
	public boolean hasPermissions() {
		return permissions.size() > 0;
	}
	/**
	 *
	 * @return segments' permissions
	 */
	public String getPermissions() {
		String toReturn = "";
		for (char c : permissions) {
			toReturn += String.valueOf(c);
		}
		return toReturn;
	}
	
	
}
