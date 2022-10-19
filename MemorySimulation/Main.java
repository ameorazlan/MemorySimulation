package MemorySimulation;

public class Main {

	
	public static void main(String[] args) {	
		//Start of Component B.1
		System.out.println("Start B.1");
		Process p1 = new Process("1, 100, 200, 300");
		Process p2 = new Process("2, 20, 30, 40, 50");
		Process p3 = new Process("3, 10");
		Process p4 = new Process("4, 15, 27, 33, 13, 54");
		Process p5 = new Process("5, 300, 280");
		Memory M = new Memory(1024, 124);
		System.out.println(M.getMemoryState());
		System.out.println();


		//adding P1.S1 to memory
		System.out.println("Adding Segment P1.S1 to Main Memory from Process " + p1.getID());
		M.addSegment(p1, 1);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//adding P1 to Memory
		System.out.println("Adding to Main Memory the Process " + p1.getID());
		M.addProcess(p1);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//adding P2 to Memory
		System.out.println("Adding to Main Memory the Process " + p2.getID());
		M.addProcess(p2);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Modifying P1 Memory Allocation
		System.out.println("Modifying Main Memory allocation for Process " + p1.getID() + "(increasing memory for segment 1, decreasing memory for segment 2, and removing segment 3 from memory");
		M.modifyAllocation("1, 50, -70, -300");
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Testing invalid argument for memory allocation
		System.out.println("Attempting to make an invalid memory allocation change ");
		M.modifyAllocation("1, 1000, 0, 0");
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Printing Segment Tables for P1, P2, P3
		System.out.println("Printing segment tables for Processes 1, 2, 3, 4, 5");
		System.out.println(p1.printSegmentTable());
		System.out.println(p2.printSegmentTable());
		System.out.println(p3.printSegmentTable());
		System.out.println(p4.printSegmentTable());
		System.out.println(p5.printSegmentTable());
		System.out.println();
		
		//Adding P3 to Memory
		System.out.println("Adding to Main Memory the Process " + p3.getID());
		M.addProcess(p3);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Adding P4.S2 to Memory
		System.out.println("Adding Segment P4.S2 to Main Memory from Process " + p4.getID());
		M.addSegment(p4, 2);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Adding P4 to Memory
		System.out.println("Adding to Main Memory the Process " + p4.getID());
		M.addProcess(p4);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Trying to add oversized process P5 to Memory
		System.out.println("Attempting to add Process " + p5.getID() + " containing segments too big for the main memory to allocate in its current form");
		M.addProcess(p5);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Removing P1 from Memory
		System.out.println("Removing from Main Memory the Process " + p1.getID());
		M.modifyAllocation("1, -150, -130");
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Removing P2 from Memory
		System.out.println("Removing from Main Memory the Process " + p2.getID());
		M.modifyAllocation("2, -20, -30, -40, -50");
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Removing P4.S5 from Memory
		System.out.println("Removing P4.S5 from Main Memory the Process " + p4.getID());
		M.modifyAllocation("4, 0, 0, 0, 0, -54");
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Adding P5 to Memory
		System.out.println("Adding to Main Memory the Process " + p5.getID());
		M.addProcess(p5);
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Removing all Processes from Memory
		System.out.println("Removing all processes from main memory");
		M.modifyAllocation("3, -10");
		M.modifyAllocation("4, -15, -27, -33, -13");
		M.modifyAllocation("5, -300, -280");
		System.out.println(M.getMemoryState());
		System.out.println();
		
		//Testing invalid argument for process creation
		System.out.println("Attempting to make an invalid process and add it to memory");
		p1 = new Process("a");
		M.addSegment(p1,1);
		System.out.println();
		
		System.out.println("End B.1");
		System.out.println("...................................................................");
		
		//Start B2.1
		System.out.println("Start B.2.1");
		System.out.println("Defined in File Segment Line 62, used in File Memory Lines 159, 233, 441");
		Memory M2 = new Memory(500, 50);
		Process p6 = new Process("6, 40, 30, 70");
		Process p7 = new Process("7, 380, 30");
		System.out.println(M2.getMemoryState());
		System.out.println();
		
		//Adding Processes 6 and 7 to Memory
		System.out.println("Adding to Main Memory the Process " + p6.getID());
		M2.addProcess(p6);
		System.out.println(M2.getMemoryState());
		System.out.println();
		
		System.out.println("Adding to Main Memory the Process " + p7.getID());
		M2.addProcess(p7);
		System.out.println(M2.getMemoryState());
		System.out.println();
		
		//Printing out segment tables for P6 and P7
		System.out.println("Printing segment tables for Processes 6, 7");
		System.out.println(p6.printSegmentTable());
		System.out.println(p7.printSegmentTable());
		System.out.println();
		
		//Removing segment P6.S3 and P7.S2 from Memory
		System.out.println("Removing Segment P6.S3 to Main Memory from Process " + p6.getID());
		M2.modifyAllocation("6, 0, 0, -70");
		System.out.println("Removing Segment P7.S2 to Main Memory from Process " + p7.getID());
		M2.modifyAllocation("7, 0, -30");
		System.out.println(M2.getMemoryState());
		System.out.println(p6.printSegmentTable());
		System.out.println(p7.printSegmentTable());
		System.out.println();
		
		//Adding segment P7.S1 to Memory
		System.out.println("Adding Segment P7.S1 to Main Memory from Process " + p7.getID());
		M2.addSegment(p7, 1);
		System.out.println(M2.getMemoryState());
		System.out.println(p7.printSegmentTable());
		
		System.out.println("End B.2.1");
		System.out.println("...................................................................");
		

		//Start B2.2
		System.out.println("Start B.2.2");
		System.out.println("Defined in File Segment Line 85, used in File Process Line 55");
		Memory M3 = new Memory(500, 50);
		Process p8 = new Process("8, [40; r--], [30; -w-], [70; rwx]");
		System.out.println();
		
		//Adding Process 8 to Memory and Printing Segment Table
		System.out.println("Adding to Main Memory the Process " + p8.getID());
		M3.addProcess(p8);
		System.out.println(M3.getMemoryState());
		System.out.println(p8.printSegmentTable());
		System.out.println();
		
		//Making a process with a segment with invalid permissions(Has execute but no read)
		System.out.println("Making a process with segments with invalid permissions (Has execute but no read)");
		Process p9 = new Process("9, [380; -wx], [30; --x]");
		System.out.println(p9.printSegmentTable());
		System.out.println();
		
		System.out.println("End B.2.2");
		System.out.println("...................................................................");
		
		
		//Start B2.3
		System.out.println("Start B.2.3");
		System.out.println("Defined in File Memory Lines 342, 370");
		Process p10 = new Process("1, 100, 200, 300");
		Process p11 = new Process("2, 20, 30, 40, 50");
		Memory M4 = new Memory(2000, 248);
		System.out.println("Adding process 1 to Memory");
		M4.addProcess(p10);
		System.out.println("Adding process 2 to Memory");
		M4.addProcess(p11);
		System.out.println(M4.getMemoryState());
		System.out.println();
		
		//Demonstrating TLB Miss
		System.out.println("Demonstrating TLB miss");
		System.out.println(M4.searchTLB(1, 3));
		System.out.println();
		
		//Demonstrating TLB working
		System.out.println("Demonstrating TLB working");
		System.out.println(M4.searchTLB(2, 2));
		System.out.println();
		
		System.out.println("End B.2.3");
		System.out.println("...................................................................");
		
		
		//Start B2.4
		System.out.println("Start B.2.4");
		System.out.println("Defined in File Memory Line 409");
		System.out.println();
		
		//Example 1
		System.out.println("Compaction Example 1");
		Memory M5 = new Memory(1000, 100);
		p1 = new Process("1, 100, 200, 300");
		System.out.println(M5.getMemoryState());
		M5.addProcess(p1);
		System.out.println(M5.getMemoryState());
		p2 = new Process("2, 100");
		M5.addProcess(p2);
		System.out.println(M5.getMemoryState());
		M5.modifyAllocation("1, -100, 0, -100");
		System.out.println(M5.getMemoryState());
		p3 = new Process("3, 150, 250");
		System.out.println("Attempting to add process 3");
		M5.addProcess(p3);
		System.out.println(M5.getMemoryState());
		System.out.println("Compacting Memory and attempting to add process 3 again");
		M5.compaction();
		System.out.println(M5.getMemoryState());
		M5.addProcess(p3);
		System.out.println(M5.getMemoryState());
		System.out.println();
		
		//Example 2
		System.out.println("Compaction Example 2 (by me)");
		Memory M6 = new Memory(800, 50);
		p1 = new Process("1, 100, 300, 50");
		p2 = new Process("2, 100, 50, 375");
		System.out.println(M6.getMemoryState());
		System.out.println("Adding Process 1 to memory");
		M6.addProcess(p1);
		System.out.println(M6.getMemoryState());
		System.out.println("Adding Process 2 to memory");
		M6.addProcess(p2);
		System.out.println(M6.getMemoryState());
		System.out.println("Modifying Process 1 segments");
		M6.modifyAllocation("1, 0, -300, -50");
		System.out.println(M6.getMemoryState());
		System.out.println("Attempting to add P2 S3 again");
		M6.addSegment(p2, 3);
		System.out.println(M6.getMemoryState());
		System.out.println("Compacting memory and attempting to add P2 S3 again");
		M6.compaction();
		System.out.println(M6.getMemoryState());
		M6.addSegment(p2, 3);
		System.out.println(M6.getMemoryState());
		System.out.println();
		
		System.out.println("End B.2.4");
		System.out.println("...................................................................");
		
	}
}
