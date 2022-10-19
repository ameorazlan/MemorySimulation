package MemorySimulation;

import java.util.ArrayList;
import java.util.HashMap;

//The Memory Class simulates the main memory. Non contigous segments can be allocated memory here.
public class Memory {
	//total size of memory
	private int total_size;
	
	//ArrayList simulating the memory
	//Each ArrayList index represents one byte of memory
	private ArrayList<Segment> memorySimulation;
	
	//ArrayList storing all the processes with segments in memory
	private ArrayList<Process> processTable;
	
	//ArrayList storing all segments that have been accessed
	//Stores the segment each time it is modified and when it is allocated memory 
	//Used to initialize the TLB Hashmap
	private ArrayList<Segment> TLBList = new ArrayList<Segment>();
	
	//Hashmap representing the TLB (Translation Look-Aside Buffer
	//stores segment ID and memory location of recently accessed segments
	//Approximate size of half of total segments in memory
	private HashMap<String, Integer> TLB = new HashMap<String, Integer>();
	
	//total size of OS segment
	private int OSSize;
	
	/**
	 * Main constructor for memory
	 * @param size
	 * represents the memory size
	 * 
	 * @param OSSize
	 * represents the OS segment size in memory
	 */
	public Memory(int size, int OSSize) {
		this.OSSize = OSSize;
		this.total_size = size;
		//initializes the memorySimulation and process Table ArrayList
		this.memorySimulation = new ArrayList<Segment>(size);
		this.processTable = new ArrayList<Process>();
		//calls the intializeMemory method and passes in OSSize as parameter
		this.initializeMemory(OSSize);
	}
	
	/**
	 * Method for the total size of the Main Memory
	 * @param none 
	 * @return actual size if it has been defined
	 * @return -1 if not defined
	 */
	public int getTotalSize() {
		return total_size;
	}
	
	/**
	 * Method for initializing the memory
	 * @param OSSize
	 */
	private void initializeMemory(int OSSize) {
		for (int i = 0; i < total_size; i++) {
			// Sets all bytes in the memory ArrayList to Holes
			Segment hole = new Segment("Hole", 1);
			hole.setBaseAddress(i);
			memorySimulation.add(hole);
		}
		//Adds the OS segment to memory
		this.allocateSegment(new Segment("OS", OSSize));
	}
	/**
	 * Method for getting total amount of segments in memory
	 * @return total amount of segments in memory
	 */
	private int getTotalSegments() {
		int totalSegments = 0;
		//Iterates through each process in process table and counts number of segments
		for (Process p : this.processTable) {
			for (Segment s : p.getAllSegments()) {
				totalSegments ++;
			}
		}
		return totalSegments;
	}
	
	/**
	 * Method for allocating memory for a process
	 * @param proc
	 */
	public void addProcess(Process proc) {
		//Iterates through each segment in the process and allocates memory for it
		for (Segment s : proc.getAllSegments()) {
			//Upon successful allocation, add the segment to the TLBList and add process to processTable
			if (this.allocateSegment(s) == true) {
				this.TLBList.add(s);
			} else {
				//Since memory cannot be allocated for this segment, we can skip its iteration
				continue;
			}
			this.allocateSegment(s);
			if (memorySimulation.contains(s) && !this.processTable.contains(proc)) {
				this.processTable.add(proc);
			}
		}
		}
	
	/**
	 * Method for adding an individual segment to memory
	 * @param proc
	 * @param number	(the segment ID of that process)
	 */
	public void addSegment(Process proc, int number) {
		//Upon successful allocation, add the segment to the TLBList and add process to processTable
		try {
			if (this.allocateSegment(proc.getSegment(number)) == true) {
				this.TLBList.add(proc.getSegment(number));
				if (!this.processTable.contains(proc)) {
					this.processTable.add(proc);
				}
			} else {
				//If memory cannot fit segment, then return
				return;
			}
			this.allocateSegment(proc.getSegment(number));
		} catch (NullPointerException e) {
			//Catch NullPointerException when segment to be added does not exist
			System.out.println(e +": Segment does not exist");
		}
	}
	
	/**
	 * Method for allocating memory to a Segment s
	 * @param segment
	 * @return false if Segment s is already in memory or not enough space in memory
	 * @return true if Segment s has been successfully allocated memory
	 */
	private boolean allocateSegment(Segment segment) {
		//return false if Segment s already in memoryu
		if (memorySimulation.contains(segment)) {
			return false;
		}
		//counter variable to keep track of the number of holes that are next to each other in memory
		int counter = 0;
		//iterate through each segment in memorySimulation
		for (Segment s : memorySimulation) {
			//if Segment s' ID is hole then add 1 to counter
			//else set counter back to 0
			if (s.getID().matches("Hole")) {
				counter +=1;
			} else {
				counter = 0;
				continue;
			}
			//if counter is equal to the limit the segment requires 
			if (counter == segment.getLimit()) {
				//Set the invalid-valid bit of segment to valid
				segment.setValidInvalid('v');
				//Set the base address of the segment to be where the first hole originally was
				int baseAddress = memorySimulation.indexOf(s) - counter + 1;
				segment.setBaseAddress(baseAddress);
				//Set all the indexes of the memorySimulation ArrayList to segment
				//starting from the base address until base addres + the segment's limit
				for (int i = baseAddress; i < baseAddress + segment.getLimit(); i++) {
					memorySimulation.set(i, segment);
				}
				//return true since segment has been allocated memory
				return true;
			}
		}
		//Print out that segment has not been allocated memory and return false
		System.out.println("Segment " + segment.getID() + " has not been allocated memory");
		return false;
	}
	
	/**
	 * Method for removing a segment from memory
	 * @param s
	 */
	private void removeSegment(Segment segment) {
		//Replaces all instances of that segment in the memorySimulation ArrayList with a Hole segment object
		for (int i = 0; i < memorySimulation.size(); i ++) {
			if (memorySimulation.get(i) == segment) {
				Segment hole = new Segment("Hole", 1);
				hole.setBaseAddress(i);
				memorySimulation.set(i, hole);
			}
		}
	}
	
	/**
	 * Modifies memory allocation for a processes' segments
	 * @param input
	 * Takes in input string as a parameter, which specifies the process ID, and the change in memory allocation
	 * for each segment
	 */
	public void modifyAllocation(String input) {
		//Parses input string into numbers and store it into an ArrayList
		Parser parser = new Parser();
		ArrayList<Integer>[] numbers;
		numbers = parser.parseInputString(input);
		Process proc = null;
		
		//Matches a process from processTable with the one to be modified using the Process ID
		for (Process p : processTable) {
			//Process ID is stored in index 0 of index 0 of the ArrayList
			if (p.getID() == numbers[0].get(0)) {
				proc = p;
			}
		}
		
		//if process doesn't exist or has no segments, then print error message
		if (proc == null || proc.getAllSegments().isEmpty()) {
			System.out.println("java.lang.IllegalArgumentException: Given process does not exist or has no segments");
			return;
		}
		
		//Modifies the limit of all segments within the process and allocates or deallocates the memory for segment
		for (int i = 1; i < numbers.length; ++i) {
			Segment s = proc.getSegment(i);
			//Adds or subtracts from segment limit depending on the original input
			try {
				s.modifyLimit(numbers[i].get(0));
			} catch (NullPointerException e) {
				//If returned segment is null, that means user input was too large
				//Continue by ignoring the extra part of user input
				continue;
			}
			//If the new limit is 0, then the segment is no longer requiring memory, and should therefore by removed
			if (proc.getSegment(i).getLimit() == 0) {
				this.removeSegment(proc.getSegment(i));
				proc.getSegment(i).setValidInvalid('i');
			}
			//Adds segment to TLBList as it is about to be modified/ has been accessed
			this.TLBList.add(s);
			//If the segment has asked for more memory, call the addMemory method
			//addMemory method allocates more memory to the segment
			if (s.getLimit() > s.getOldLimit()) {
				this.addMemory(s);
			} else if (s.getLimit() < s.getOldLimit()){
				//Otherwise, the process has asked for less memory, so call the removeMemory method
				//removeMemory method allocates less memory to the segment
				this.removeMemory(s);
			}
		}
	}
	
	/**
	 * Method for adding more memory for a Segment s
	 * @param s
	 */
	private void addMemory(Segment segment) {
		//initialize an ArrayList of unique segments in the memory
		ArrayList<Segment> uniqueSegments = new ArrayList<Segment>();
		//set the diff variable to be the difference between the new limit and the old limit
		int diff = segment.getLimit() - segment.getOldLimit();
		//Checks if there is enough space in memory to add memory for this process
		int holeCounter = 0;
		//counts the maximum number of holes after segment
		for (Segment s : memorySimulation) {
			if (s.getBaseAddress() > segment.getBaseAddress() && s.getID().matches("Hole")) {
				holeCounter ++;
			}
		}
		//if number of holes is less than the request amount of memory to be added to segment
		//count the maximum number of holes before the segment
		if (holeCounter < diff) {
			holeCounter = 0;
			for (Segment s : memorySimulation) {
				if (s.getBaseAddress() < segment.getBaseAddress() && s.getID().matches("Hole")) {
					holeCounter ++;
				} 
			}
			//If there is not enough space before and after the segment, then the segment cannot be allocated any more memory
			if (holeCounter < diff) {
				//Print that memory unable to add more memory
				System.out.println("Unable to allocate more memory for Segment " + segment.getID());
				//set the segment limit back to its old limit
				segment.modifyLimit(-diff);
				return;
			}
			
			//If there is enough space before the segment's base location
			//iterate through segments in memory
			for (Segment a : memorySimulation) {
				//add unique segments to uniqueSegments
				if (uniqueSegments.contains(a)) {
					continue;
				}
				uniqueSegments.add(a);
				//If the base address of Segment a is smaller than base address of Segment s, subtract diff to Segment a's base address
				//This is because Segment a's base address will be shifted due to Segment s requesting for more memory
				if (a.getBaseAddress() < segment.getBaseAddress() && !a.getID().matches("OS")) {
					a.setBaseAddress(a.getBaseAddress() - diff);
				}
			}
			//Set segment's base address to its base address minus diff
			segment.setBaseAddress(segment.getBaseAddress() - diff);
			//Call updateSegmentAllocation() method to update the memory to match each segments' new base address
			this.updateSegmentAllocation();
			return;
		}
		
		//If there is enough memory space after the segment's base location
		//iterate through segments in memory
		for (Segment a : memorySimulation) {
			//add unique segments to uniqueSegments
			if (uniqueSegments.contains(a)) {
				continue;
			}
			uniqueSegments.add(a);
			//If the base address of Segment a is larger than base address of Segment s, add diff to Segment a's base address
			//This is because Segment a's base address will be shifted due to Segment s requesting for more memory
			if (a.getBaseAddress() > segment.getBaseAddress() && !a.getID().matches("OS")) {
				a.setBaseAddress(a.getBaseAddress() + diff);
			}
		}
		//Call updateSegmentAllocation() method to update the memory to match each segments' new base address
		this.updateSegmentAllocation();
	}
	
	/**
	 * Method for removing memory from a Segment s
	 * @param s
	 */
	private void removeMemory(Segment segment) {
		//Set variable diff to be the difference between old limit and new limit
		int diff = segment.getOldLimit() - segment.getLimit();
		//iterate through the memory locations that Segment s does not require anymore
		for (int i =0; i < diff; i ++) {
			//Set the memory locations to be a new hole segment
			Segment hole = new Segment("Hole", 1);
			hole.setBaseAddress(segment.getBaseAddress() + segment.getLimit() + i);
			memorySimulation.set(segment.getBaseAddress() + segment.getLimit() + i, hole);
		}
	}
	
	/**
	 * Method for intializing the TLB Hashmap
	 */
	private void initializeTLB() {
		//Calculate the size of the TLB by dividing the number of total segments in half and rounding up if it contains a decimal
		int iterations = 0;
		if (this.getTotalSegments() % 2 == 0) {
			iterations = this.getTotalSegments() / 2;
		} else {
			iterations = Math.round(this.getTotalSegments() / 2) + 1;
		}
		//iterate through the TLBList 
		for (int i = 1; i < iterations+1; i ++) {
			//Get the segment from the back of the TLBList
			Segment s = this.TLBList.get(this.TLBList.size() - i);
			//If the segment Limit is 0, then remove it from TLBList and continue
			if (s.getLimit() == 0) {
				this.TLBList.remove(this.TLBList.size() - i);
				continue;
			}
			//Put the the segment ID and its base address in the TLB Hashmap
			this.TLB.put(s.getID(), s.getBaseAddress());
		}
	}
	/**
	 * Method for searching for a segment' memory location using the process ID and segment ID
	 * @param PID
	 * @param SID
	 * @return memory location of the segment
	 * @return -1 if segment memory location cannot be found
	 */
	public int searchTLB(int PID, int SID) {
		//call initializeTLB method to initialize the TLB
		this.initializeTLB();
		//convert the PID and SID parameters into a string called SegmentID
		String SegmentID = "P" +String.valueOf(PID) + " S" + String.valueOf(SID);
		int memoryLocation = -1; 
		//try searching the TLB for the memory location of the segment
		//catch a NullPointerException which is when the TLB does not contain information about the segment
		try {
			memoryLocation = this.TLB.get(SegmentID);
			//print out memory location of segment
			System.out.print("Memory Location of " + SegmentID + ": ");
			return memoryLocation;
		} catch (NullPointerException e){
			//print that a TLB miss occurred
			System.out.println("TLB miss: now searching through segment table of P" + String.valueOf(PID));
			//Now search for segment within the processes in processTable
			for (Process p : this.processTable) {
				//match the process ID's
				if (p.getID() == PID) {
					for (Segment s : p.getAllSegments()) {
						//match the segment ID's
						if (s.getID().matches(SegmentID)) {
							//print out memory location of segment
							memoryLocation = s.getBaseAddress();
							System.out.print("Memory Location of " + SegmentID + ": ");
							return memoryLocation;
						}
					}
				}
			}
			//print out that the segment's memory location cannot be found
			System.out.print("Unable to find process " + PID + " segment ");
			return SID;
		}
	}
	/**
	 * Method for removing external fragmentation by allocating every segment in memory again
	 */
	public void compaction() {
		//iterate through the memory and store each unique segment(excluding holes and the OS segment) in an ArrayList called segmentsToBeAdded
		ArrayList<Segment> segmentsToBeAdded = new ArrayList<Segment>();
		for (Segment s : memorySimulation) {
			if (s.getID() == "Hole" || s.getID() == "OS") {
				continue;
			}
			if (segmentsToBeAdded.contains(s)){
				continue;
			}
			segmentsToBeAdded.add(s);
		}
		//clear the memorySimulation ArrayList
		this.memorySimulation.clear();
		//call the initializeMemory method passing in the OSSize as a parameter to set up the memory again
		this.initializeMemory(this.OSSize);
		//for each segment in segmentsToBeAdded, call the allocateSegment method
		for (Segment s : segmentsToBeAdded) {
			this.allocateSegment(s);
		}
	}
	/**
	 * Method for updating the memorySimulation ArrayList
	 * Puts all segments in their correct positions
	 */
	private void updateSegmentAllocation() {
		//Iterates through all processes in processTable
		//Note that this does not allocate hole segments. Allocating memory for holes will be done in the loop after this
		for (Process p : processTable) {
			//Iterates through all segments within the process
			for (Segment s : p.getAllSegments()) {
				//Skip if memorySimulation cannot allocate space for that segment or if segment is set to invalid bit
				if (s.getBaseAddress() + s.getLimit() > memorySimulation.size() || s.getValidInvalid() == 'i') {
					continue;
				}
				//Iterates through memorySimulation to set segment to its respective indexes
				for (int i = 0; i < s.getLimit(); i ++) {
					memorySimulation.set(s.getBaseAddress() + i, s);
				}
			}
		}
		//Iterates through all segments in memorySimulation
		//This is done so that all hole segments will be in their correct position
		for (Segment s : memorySimulation) {
			//Skip if memorySimulation cannot allocate space for that segment
			if (s.getBaseAddress() + s.getLimit() > memorySimulation.size()) {
				continue;
			}
			//Iterates through memorySimulation to set segment to its respective indexes
			for (int i = 0; i <s.getLimit(); i++) {
				memorySimulation.set(s.getBaseAddress() + i, s);
			}
		}
	}
	
	/**
	 * Method for printing out the memory state
	 * @return a string simulating the memory
	 */
	public String getMemoryState() {
		//Calls the updateSegmentAllocation method to ensure all segments are in their correct spots
		this.updateSegmentAllocation();
		//Initialize the toReturn variable
		String toReturn = "";
		//Initialize curr, prev, and counter variables which are used to store the current segment's ID, previous segment's ID, and counter for how much memory space the segment takes up
		String curr = null;
		String prev = null;
		int counter = 0;
		
		//iterates through each segment in memorySimulation
		for (int i  = 0; i <memorySimulation.size(); i ++) {
			Segment s = memorySimulation.get(i);
			
			//sets curr variable to this segment's ID and increase counter
			curr = s.getID();
			counter++;
			
			//If previous is null, that means this segment is the first segment in memory, so we can continue
			if (prev == null) {
				prev = s.getID();
				continue;
			}
			
			//If curr does not match previous, that means we have reached a new segment
			//Print out the previous segments' information and set counter to 1
			if (!curr.matches(prev)) {
				toReturn += "[" + prev + ": " + String.valueOf(counter - 1) + "]";
				counter = 1;
			}
			
			//If i is the last index in memorySimulation, print out the information for the last segment
			if (i == memorySimulation.size() - 1) {
				toReturn += "[" + curr + ": " + String.valueOf(counter) + "]";
			}
			
			//set prev to this segment's ID
			prev = s.getID();
		}
		//return the String
		return toReturn;
	}
}
