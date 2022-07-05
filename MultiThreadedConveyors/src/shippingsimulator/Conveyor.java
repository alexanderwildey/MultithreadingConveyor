/*
    Name: Alexander Wildey
    Date: February 13, 2022
    Project: Conveyor Simulation with Multithreading

    Class: Conveyor
*/

package shippingsimulator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Conveyor {
    
    // Identification number of the conveyor and its lock.
    private int number;
    private Lock accessLock = new ReentrantLock();

    // Constructor method.
    public Conveyor(int conveyorNum) {
        this.number = conveyorNum;
    }

    // Attempt to lock the Conveyor. Returns true if the lock 
    // is available, and returns false if the conveyor is already 
    // locked.
    public boolean lockConveyor() {
        return accessLock.tryLock();
    }

    // Unlock the Conveyor so another station can use it.
    public void unlockConveyor() {
        accessLock.unlock();
    }

    // Get the number for the Conveyor.
    public int getNumber() {
        return this.number;
    }

    // For debugging.
    public void printInfo() {
        System.out.println("Conveyor #" + this.number);
    }
}