package MemorySimulation;

import java.util.ArrayList;

//Parse the process input string into components

public class Parser {

	/**
	 * Parser Constructor
	 * @param none
	 */
	public Parser() {
		
	}

	/**
	 * Method to extract the Process id and a list of Segment size with some parameters 
	 * @param process_string of the form pid, sid1_size, sid2_size ...
	 *        example: 1, 100, 200
	 * @param process_string of the form pid, [sid1_size; size1_arg1; size1_arg2, ...], ...
	 *        example: 1, [100; 0], [200; 1], [300; 0; 2; 3]
	 * @return comp and ArrayList of components from process_string
	 *        example comp[0] = pid and comp[1] = {100, 0}       
	 */
	public ArrayList<Integer>[] parseInputString (String process_string){
		
		int size, index;
		ArrayList<Integer>[] comp;
		
		//number of components to look at, first being the pid
		size = process_string.split(",").length;
		
		//process id, followed by segments and their arguments
		comp = new ArrayList[size];
		
		//init all
		for (int i =0; i<size; i++){
			comp[i] = new ArrayList<Integer>();
		}
		
	
		//split into each component of process_string
		index = 0;
		for (String part:process_string.split(",")) {
			//remove space
			part = part.replace(" ", "");	
			
			//see if options are declared by looking at [ and ]
			if (part.contains("[") && part.contains("]")){
				part = part.replace(" ", "").replace("[", "").replace("]", "");
				
				//split into options: read-write-execute bit
				for (String split:part.split(";")) {
					try {
						comp[index].add(Integer.parseInt(split));
					} catch (Exception e){
						//If statements for each possible case of segment permissions done by user input
						//1 means has permission, 0 means no permission, -1 means invalid combination of permissions given, -2 means unclear user input given
						switch (split) {
						case "rwx":
							comp[index].add(1);
							comp[index].add(1);
							comp[index].add(1);
						case "r--":
							comp[index].add(1);
							comp[index].add(0);
							comp[index].add(0);
						case "-w-":
							comp[index].add(0);
							comp[index].add(1);
							comp[index].add(0);
						case "r-x":
							comp[index].add(1);
							comp[index].add(0);
							comp[index].add(1);
						case "rw-":
							comp[index].add(1);
							comp[index].add(1);
							comp[index].add(0);
						case "-wx":
							comp[index].add(-1);
							comp[index].add(-1);
							comp[index].add(-1);
						default:
							//default case is unclear user input given
							comp[index].add(-2);
							comp[index].add(-2);
							comp[index].add(-2);
						}
					}
				}
			}else {
				try {
					//no options presented
					comp[index].add(Integer.parseInt(part));
				} catch (NumberFormatException e) {
					System.out.println("java.lang.IllegalArgumentException: Unclear input given");
				}
			}
			index++;
		}
		//return all the parts separated by , and ; from process_string
		return comp;
	}
	
}

