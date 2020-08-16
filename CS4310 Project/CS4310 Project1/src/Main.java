import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This class will demonstrates all the required implementation
 * for project 1
 * @author Alan Huang
 */
public class Main {
	
	static float fCFSTotal = 0; // Add all 20 trials together
	static float sJFTotal = 0; // Add all 20 trials together
	static float rR2Total = 0; // Add all 20 trials together
	static float rR5Total = 0; // Add all 20 trials together
	static float fCFSEach = 0; //to copy each trial
	static float sJFEach = 0; //to copy each trial
	static float rR2Each = 0; //to copy each trial
	static float rR5Each = 0; //to copy each trial
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		System.out.println("Please enter 1 to test from txt file");
		System.out.println("       enter 2 to test random job length");
		
		//Create scanner to scan path for txt file
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		//For user input
		System.out.print(">> " );
		int userInput = input.nextInt();
		
		//Job from txt test file
		if (userInput == 1)
		{
			//Use Java utility LinkedList since it provide function for queue and stack
			LinkedList<Job> jobOrder = new LinkedList<Job>(); 
			int size = 1;
			
			System.out.println("Please enter input txt file path");
			System.out.print("Example: C:/Users/f4tw/OneDrive/Desktop/Job.txt \n" );
			@SuppressWarnings("resource")
			Scanner input2 = new Scanner(System.in);
			
			//For user input
			System.out.print(">> " );
			String filePath = input2.nextLine();
			
			while(size != 0)
			{
				System.out.println("Please enter job size (5, 10, 15) or 0 to quit");
				
				@SuppressWarnings("resource")
				Scanner input1 = new Scanner(System.in);
				//For user input
				System.out.print(">> " );
				size = input1.nextInt();
				
				if(size > 0)
				{
					//Read input txt file
					fileReader(jobOrder,  filePath, size );
					
					//Call first come first serve algorithm
					firstComeFirstServe( (LinkedList<Job>) jobOrder.clone(), 1 );
					//Shortest job first algorithm
					shortestJobFirst ((LinkedList<Job>) jobOrder.clone(), 1);
					//Round robin algorithm
					roundRobin ((LinkedList<Job>) jobOrder.clone(), 2, 1);			
					roundRobin ((LinkedList<Job>) jobOrder.clone(), 5, 1);
				}
				jobOrder.clear();
								
			}
			System.out.println("Program End");
		}
		//If user input is 2 then create random job 
		else if(userInput == 2)
		{
			//Use Java utility LinkedList since it provide function for queue and stack
			LinkedList<Job> jobOrder = new LinkedList<Job>();
			int size = 1;
			float [][] results = new float[4][20] ; // Use this 2D array to store each results
			
			while(size != 0)
			{
				System.out.println("Please enter random job size (5, 10, 15) or 0 to quit");
				System.out.println("The program will call total 20 trial to calcualte average turnaround time");
				
				@SuppressWarnings("resource")
				Scanner input1 = new Scanner(System.in);
				//For user input
				System.out.print(">> " );
				size = input1.nextInt();
				
	
				if(size > 0)
				{
					
					for(int i = 0; i < 20; i ++)
					{
						randomJob(jobOrder, size); //Call  randomJob to create random job task

						//Call first come first serve algorithm
						firstComeFirstServe( (LinkedList<Job>) jobOrder.clone(), 2 );
						//Shortest job first algorithm
						shortestJobFirst ((LinkedList<Job>) jobOrder.clone(), 2);
						//Round robin algorithm
						roundRobin ((LinkedList<Job>) jobOrder.clone(), 2, 2);			
						roundRobin ((LinkedList<Job>) jobOrder.clone(), 5, 2);
						
						//Use this for loop to store each result
						for(int j = 0; j < 4; j++)
						{
							if(j == 0)
							{
								results[j][i] = fCFSEach; 
							}
							else if(j == 1 )
							{
								results[j][i] = sJFEach; 
							}
							else if(j == 2 )
							{
								results[j][i] = rR2Each; 
							}
							else if(j == 3 )
							{
								results[j][i] = rR5Each; 
							}
						}
						//Reset result for each trial
						fCFSEach = 0;
						sJFEach = 0;
						rR2Each = 0;
						rR5Each = 0;
						
						jobOrder.clear();
					}
					
					//Print each trials and their average
					for(int i = 0; i < 20; i++)
					{
						System.out.println("FCFS trial " + (i+1) + " is " + String.format("%.3f", results[0][i]));
					}
					
					System.out.println("FCFS average over 20 trials is " + String.format("%.3f", (fCFSTotal/20)) +"\n" );
					
					for(int i = 0; i < 20; i++)
					{
						System.out.println("SJF trial " + (i+1) + " is " + String.format("%.3f", results[1][i]));
					}
					
					System.out.println("SJF average over 20 trials is " + String.format("%.3f", (sJFTotal/20)) +"\n" );
									
					for(int i = 0; i < 20; i++)
					{
						System.out.println("RR2 trial " + (i+1) + " is " + String.format("%.3f", results[2][i]));
					}
					
					System.out.println("RR2 average over 20 trials is " + String.format("%.3f", (rR2Total/20)) +"\n" );
					
					for(int i = 0; i < 20; i++)
					{
						System.out.println("RR5 trial " + (i+1) + " is " + String.format("%.3f", results[3][i]));
					}
					
					System.out.println("RR5 average over 20 trials is " + String.format("%.3f", (rR5Total/20)) +"\n" );
					
					//Reset average result
					fCFSTotal = 0;
					sJFTotal = 0;
					rR2Total = 0;
					rR5Total = 0;
				}
				
			}
			System.out.println("Program End");
		}
		
	}
	
	//My FCFS method
	public static void firstComeFirstServe(LinkedList<Job> jList , int token)
	{
		int total = jList.size();
		int startTime = 0;
		int endTime = 0;
		float turnTime = 0;
		
		//Token 1 is for txt job, and 2 is for random job
		if(token == 1)
		{
			System.out.println("(a) First Come First Service ");
			System.out.println("Job#" + "\t | " + " Start time" + "\t | " + " End Time" +"\t | " + "Job Completion ");
		}
		
		
		while( !jList.isEmpty())
		{
			endTime = endTime + jList.getFirst().getId();//Adding each end time together
			
			//Token 1 is for txt file test result display
			if(token == 1)
			{
				System.out.println( jList.getFirst().getName() + "\t |  " + startTime + "\t\t |  " 
						+ endTime + "\t\t | "  + jList.getFirst().getName() + " completed @ " + endTime);
			}
			
			// Calculation for the result display
			turnTime = turnTime + endTime;
			startTime = endTime;
			
			jList.remove();
		}
		//Token 1 is for txt file test result display
		if(token == 1)
		{		
			System.out.println("FCFS Average Turnaround Time is = " + String.format("%.3f", (turnTime/total)) +"\n" );
		}
		
		//Result for random job
		fCFSEach = turnTime/total;
		fCFSTotal = fCFSTotal +(turnTime/total);
	}
	
	//My SJF method
	public static void shortestJobFirst (LinkedList<Job> jList, int token )
	{
        //Use Java utility to sort the linked list with job length
		Collections.sort(jList, new ascendingComparator());
		
		int total = jList.size();
		int startTime = 0;
		int endTime = 0;
		float turnTime = 0;
		
		//Token 1 is for txt file test result display
		if(token == 1)
		{
			System.out.println("(b) Shortest Job First ");
			System.out.println("Job#" + "\t | " + " Start time" + "\t | " + " End Time" +"\t | " + "Job Completion ");
		}	
		
		while( !jList.isEmpty())
		{		
			endTime = endTime + jList.getFirst().getId();

			if(token == 1)
			{			
				System.out.println( jList.getFirst().getName() + "\t |  " + startTime + "\t\t |  " 
					+ endTime + "\t\t | "  + jList.getFirst().getName() + " completed @ " + endTime);
			}
			
			// Calculation for the result display
			turnTime = turnTime + endTime;	
			startTime = endTime;
				
			jList.remove();
		}
		
		//Token 1 is for txt file test result display
		if(token == 1)
		{	
			System.out.println("SJF Average Turnaround Time is = " + String.format("%.3f", (turnTime/total)) +"\n");
		}
		
		//Result for random job
		sJFEach = turnTime/total;
		sJFTotal = sJFTotal +(turnTime/total);
	}
	
	public static void roundRobin (LinkedList<Job> jList, int slices, int token)
	{
		int total = jList.size();
		int startTime = 0;
		int endTime = 0;
		float turnTime = 0;
		
		//Display for 2 slices
		if(slices == 2)
		{
			//Token 1 is for txt file test result display
			if(token == 1)
			{			
				System.out.println("(c) Round Robin with Time Slice = 2 "); 			
				System.out.println("Job#" + "\t | " + " Start time" + "\t | " + " End Time" +"\t | " + "Job Completion ");
			}
		}
		
		//Display for 5 slices
		else
		{
			//Token 1 is for txt file test result display
			if(token == 1)
			{	
				System.out.println("(d) Round Robin with Time Slice = 5 "); 
				System.out.println("Job#" + "\t | " + " Start time" + "\t | " + " End Time" +"\t | " + "Job Completion ");
			}
		}
		
		while( !jList.isEmpty())
		{
								
			// If current job remove 2 result is less than 0 then the job is done.
			if( (jList.getFirst().getId() - slices) <= 0)
			{
				endTime =  endTime + jList.getFirst().getId(); // the end time will be process time
				
				if(token == 1)
				{	
					System.out.println( jList.getFirst().getName() + "\t |  " + startTime + "\t\t |  " 
						+ endTime + "\t\t | "  + jList.getFirst().getName() + " completed @ " + endTime);
				}
				
				// Calculation for the result display
				turnTime = turnTime + endTime;	
				startTime = endTime;
				
				jList.remove();
			}
			else 
			{		
				
				endTime =  endTime + slices;	
				
				if(token == 1)
				{	
					System.out.println( jList.getFirst().getName() + "\t |  " + startTime + "\t\t |  " 
						+ endTime + "\t\t | " );
				}
				startTime = endTime;
				
				//Add head to last node and remove head
				jList.addLast(new Job(jList.getFirst().getName (), jList.getFirst().getId()-slices));				
				jList.remove();
			}
			
		}
		
		//Token 1 is for txt file test result display
		if(token == 1)
		{	
			System.out.println("Average Turnaround Time is = " + String.format("%.3f", (turnTime/total)) +"\n" );
		}
		
		//result for 2 slices
		if(slices == 2)
		{
			//Result for random job
			rR2Each = turnTime/total;
			rR2Total = rR2Total +(turnTime/total);
		}
		//result for 5 slices
		else
		{
			//Result for random job
			rR5Each = turnTime/total;
			rR5Total = rR5Total +(turnTime/total);
		}

	}
	
	//This method will read txt file and copy it to LinkedList
	public static void fileReader(LinkedList<Job> jList, String path, int size ) throws Exception 
	{	      
		//Scan txt file 
		Scanner sc = new Scanner(new BufferedReader(new FileReader(path)));
		String tempName = "";
		int tempLength = 0;
		int lineCount = 0;
     	    
		//Read while the scan still has next integers 
		while(size != 0) 
	    {       
			lineCount = lineCount + 1;
			
			if (lineCount % 2 != 0)
			{
				tempName = sc.next();
			}
			else
			{
				tempLength = sc.nextInt();
				jList.add( new Job(tempName, tempLength) );
				size--;
			}
 
	    }
		sc.close();
	}
	
	//This method will create random job and store them to LinkedList
	public static void randomJob(LinkedList<Job> jList, int size )
	{
		int Low = 1;
		int High = 20;
				
		for(int i = 0; i < size; i++)
		{
			jList.add( new Job("Job"+String.valueOf(i+1), (int) ((Math.random() * (High - Low)) + Low)) );
	    }
	}

}




