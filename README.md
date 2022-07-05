# MultithreadingConveyor
Multithreading exercise with packages moving on a conveyor similar to the Dining Philosopher's Problem.

Each conveyor is initialized with the parameters from the config.txt file. 
The first number is the amount of conveyors and routing stations to be created, and the following numbers are the amount of packages each routing station needs to move.
The conveyors are shared among the routing stations.

In order for a package to be moved by a routing station, the input conveyor and output conveyor must not be doing work. Locks are used to make sure that both
conveyors are not working in order to move a package. The routing station first checks if the input conveyor is free, and if so, reserves it by locking the conveyor.
Then the routing station checks the output conveyor, and if it is free, locks the conveyor and moves the package. If the output conveyor is not free, the routing
station unlocks the input conveyor.

Once a package has been moved, the routing station unlocks both conveyors and then begins to attempt to relock both conveyors.

When a routing station finishes moving all of the packages assigned to it, it unlocks any conveyors it has locked and does not attempt to lock another conveyor.

The program ends once all of the routing stations have moved all of their assigned packages.
