/*
    Name: Alexander Wildey
    Course: CNT 4714 Spring 2022
    Assignment Title: Project 2 - Multi-threaded programming in Java
    Date: February 13, 2022

    Class: PackageFacilityManager
*/

package shippingsimulator;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PackageFacilityManager {

    public static int maxNumStations = 10;
    public static int numStations = 0;

    // Open the config file and read its contents. Assign the number of threads to be
    // used to the numStations variable and add the workload for each station to the 
    // configArray int array.
    public static ArrayList<Integer> gatherConfigInfo() throws FileNotFoundException, IOException{
        
        File ifp = new File("res/config.txt");
        Scanner sc = new Scanner(ifp);
        String scan = ""; // Holds the current line from the config.txt file.

        // Get the first line of the file to determine the total number of 
        // stations needed for this simulation
        scan = sc.nextLine();
        numStations = Integer.parseInt(scan);

        // Make an ArrayList to hold the workload values and add 
        // those values from the config.txt file.
        ArrayList<Integer> configArray = new ArrayList<Integer>();

        while (sc.hasNextLine()) {

            scan = sc.nextLine();

            configArray.add(Integer.parseInt(scan));         
        }

        // Print the gathered workload values for each of the stations.
        for (int i = 0; i < numStations; i++) {
             System.out.println("Routing Station " + i + " has Total Workload of "
                     + configArray.get(i));
        }

        System.out.println("\n");
        
        sc.close();
        return configArray;
    }

    public static void main(String[] args) throws FileNotFoundException{

        // Create ArrayList to hold the workload values for each of the Stations,
        // and clear the ArrayList to remove variable not used warning as 
        // the variable assignment is in the try{} block below.
        ArrayList<Integer> workload = new ArrayList<Integer> ();
        workload.clear();

        System.out.println("**********PACKAGE MANAGEMENT FACILITY SIMULATION START**********\n");

        // Call gatherConfigInfo() to read the config.txt file and 
        // get the number of stations for this simulation and the
        // workload for each station.
        try {
            workload = gatherConfigInfo();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Create an array of Conveyors to link the Stations to two Conveyors.
        Conveyor conveyorArray[] = new Conveyor[numStations];

        for (int i = 0; i < numStations; i++) {
            conveyorArray[i] = new Conveyor(i);
        }

        // Create the thread pool and make all of the Station objects with the
        // two linked Conveyors. First Conveyor (same number as station), is the
        // input conveyor, second Conveyor (previous number as station) is the 
        // output conveyor.
        ExecutorService application = Executors.newFixedThreadPool(maxNumStations);

        for (int i = 0; i < numStations; i++) {
            application.execute(new Station(i, workload.get(i), conveyorArray[i],
                    conveyorArray[(((i - 1) + numStations) % numStations)]));
        }

        // End all of the threads and shutdown the application.
        application.shutdown();
        while (!application.isTerminated()) {

        }

        System.out.println("\n**********ALL WORKLOADS COMPLETE***PACKAGE MANAGEMENT FACILITY SIMULATION END**********\n");
    }
}
