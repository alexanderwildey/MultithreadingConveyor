/*
    Name: Alexander Wildey
    Course: CNT 4714 Spring 2022
    Assignment Title: Project 2 - Multi-threaded programming in Java
    Date: February 13, 2022

    Class: Station
*/

package shippingsimulator;

import java.util.Random;

public class Station implements Runnable {

    private Random ran = new Random();

    // Input conveyor has the same number as the station.
    // Output conveyor has the previous number as the station.
    private Conveyor inputC;
    private Conveyor outputC;
    private int workloadCounter;
    private int stationNumber;
    private int hasBothLocks;

    // Constructor method.
    public Station (int stationNum, int workload, Conveyor inC, Conveyor outC) {

        this.stationNumber = stationNum;
        this.workloadCounter = workload;
        this.inputC = inC;
        this.outputC = outC;
    }

    // Print the Conveyor initialization information for each station. Includes
    // input conveyor number, output conveyor number, and total workload.
    public void displayStationConveyors() {

        System.out.println("\n%%%%%ROUTING STATION " + stationNumber + " Coming Online - Initializing Conveyors%%%%%\n");

        System.out.println("Routing Station " + stationNumber 
                + ": Input conveyor set to conveyor number C" + inputC.getNumber() + ".");
        System.out.println("Routing Station " + stationNumber
                 + ": Output conveyor set to conveyor number C" + outputC.getNumber() + ".");
        System.out.println("Routing Station " + stationNumber + ": Workload set. Station "
                 + stationNumber + " has a total of " + workloadCounter 
                 + " package groups to move.\n");

        System.out.println("Routing Station " + stationNumber + ": Now Online.\n\n");
    }

    // Have the thread hold the locks for the two conveyors for a random
    // amount of time to simulate work being done.
    public void doWork() {

        System.out.println("\n******Routing Station " + stationNumber
                + ":****CURRENTLY HARD AT WORK MOVING PACKAGES******\n");
        System.out.println("Routing Station " + stationNumber
                + ": successfully moves packages into station on input conveyor C" + inputC.getNumber() + ".");
        System.out.println("Routing Station " + stationNumber
                + ": successfully moves packages out of station on output conveyor C" + outputC.getNumber() + ".\n");
        
        workloadCounter--;
        System.out.println("Routing Station " + stationNumber + ": has " + workloadCounter
                + " package groups left to move.\n");

        goToSleep();

        if (workloadCounter == 0) {
            System.out.println("####Routing Station " + stationNumber + ": WORKLOAD SUCCESSFULLY COMPLETED###Routing Station "
                + stationNumber + " releasing locks and going offline#####\n\n");
        }
    }

    // Put thread to sleep for a random amount of time.
    public void goToSleep() {

        try {
            Thread.sleep(ran.nextInt(500));
        } catch (InterruptedException interuptEx) {
            interuptEx.printStackTrace();
        }
    }

    public void run() {

        // Show the conveyor initialization information for the station.
        displayStationConveyors();
        
        // Main loop for running the stations.
        while (workloadCounter > 0) {
            
            System.out.println("Routing Station " + stationNumber + ": Entering Lock Acquisition Phase.\n");

            hasBothLocks = 0;

            // If the station doesn't have the locks for both of its Conveyors, run this.
            while (hasBothLocks == 0) {

                // Attempt to get the input conveyor lock.
                if (inputC.lockConveyor()) {

                    System.out.println("Routing Station " + stationNumber
                             + ": holds lock on input conveyor C" + inputC.getNumber() + ".");

                    // Attempt to get the output conveyor lock.
                    if (outputC.lockConveyor()) {
                        
                        hasBothLocks = 1;

                        System.out.println("Routing Station " + stationNumber
                                + ": holds lock on output conveyor C" + outputC.getNumber() + ".");

                        System.out.println("\n*****Routing Station " + stationNumber + ": holds locks on both input conveyor C"
                                + inputC.getNumber() + " and output conveyor C" + outputC.getNumber() + "*****\n\n");

                        doWork();

                        // After the "work" has been done by the station, release both locks.
                        System.out.println("Routing Station " + stationNumber + ": Entering Lock Release Phase.");
                        System.out.println("Routing Station " + stationNumber + ": unlocks/releases input conveyor C"
                                + inputC.getNumber() + ".");
                        
                        inputC.unlockConveyor();

                        System.out.println("Routing Station " + stationNumber + ": unlocks/releases output conveyor C" 
                                + outputC.getNumber() + ".\n");
                        
                        outputC.unlockConveyor();

                    }
                    else {

                        // If the output conveyor lock could not be acquired, release the input conveyor lock and 
                        // make the station thread sleep for a bit.
                        System.out.println("Routing Station " + stationNumber + ": unable to lock output conveyor C"
                                + outputC.getNumber() + ", unlocks input conveyor C" + inputC.getNumber() + ".\n");

                        inputC.unlockConveyor();
                        
                        goToSleep();
                    }
                }
            }
        }

        System.out.println("\n\n@@@@@@@Routing Station " + stationNumber + ": OFF LINE@@@@@@@\n\n");
    }
}