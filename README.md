# PhotoDrop

Implemented 4 algorithms (2 phase commit, timeout, data management replication) with multiple 
clients and multiple servers using Java Remote Method Invocation (RMI).

## Assumptions

* Program can be multi-threaded.
* Connection between server and client is made by Java RMI
* Assuming that the user or client is comfortable running code in the commandline.
* Assuming that the user or client is comfortable running dockerfile.
* Assume that the clients wants there data shared between all 5 servers.
* Multiple of the same file can be added to MongoDb
* The user wants data stored in Mongo Database
* We assume servers will not fail
* Photos taken are of 1 client and 5 servers
* Algorithm is only written for UPLOAD, DOWNLOAD and DELETE operation
* Assume a file is uploaded first before download and delete are executed
* If the time for the server to respond to the clients request takes more than 100000 milliseconds 
  the system will exit with a status of 0.
* The file extension will remain the same for a file for each operation
* Allow the coordinator to connect before the servers connect
* Multiple client.txt files possible depending on the number of clients connected to network
* If a client enters a command incorrectly; they will be prompted to reenter the commands 
  (UPLOAD DOWNLOAD OR DELETE) do not include help or quit. Only include all when you see the 
  welcome statement.

## Getting Started

* This project was built in IntelliJ.

* Download the Photo-Drop-1.5 package onto your local machine. Use the dockerfile to run the
  application.

## File Location
* The log files for the client can be found within the Photo-Drop-1.5 folder.
* The README markdown file is located within the Photo-Drop-1.5 directory.

### Building Java Program

Please see the Docker images inside a container for dependencies needed for code execution. </br>

### COMPILE and RUN CODE
* To compile and run the programs you have to be in the src folder. If you do not compile
  and run from this folder and decide to change directories into one of the packages you will get a
  file not found error.
* To compile the files locally on the command line it is as follows (must be in src folder):

    1. Compile all files:
    - javac -cp PAXOS/mongo-java-driver-3.11.2.jar: PAXOS/*.java

    2. Run RMIRegistry
        - rmiregistry (run rmiregistry on mac)

    3. Start Coordinator
        - java -cp PAXOS/mongo-java-driver-3.11.2.jar: PAXOS/StartCoordinator 2000

    4. Start Server where 1001 is port number; s1 server name; d1 is database name
        - java -cp PAXOS/mongo-java-driver-3.11.2.jar: PAXOS/StartServer 1001 s1 localhost localhost Coordinator db1 2000

    5. Start Client:
        - java -cp PAXOS/mongo-java-driver-3.11.2.jar: PAXOS/Client localhost 3000 Coordinator 2000

************REFERENCE SCREENSHOT that is called run_code_locally to see commands to enter in
commandline

* If you are using docker to run files:
  start.sh

NOTE: If the docker does not run please run locally.


## Entering Data
* Upload operation the file name entered should match the file name in the path
* Download operation the file name at end of path should be different from the file name entered 
  by the user.


### Current Known Bugs/Issues
* Violation of some SOLID principles.
* More descriptive statements for errors or failures.
* We hard coded the connection string for MongoDb Atlas.
* Can have more than 1 coordinator but servers will not maintain consistent state
* Must upload before download and delete operations
* Database is not consistent with servers if the servers are not restarted so not fault-tolerant
* Code needs to be more organized
* If client wants to delete a file that is not in the server the action will still state a success
* Not an issue or bug but note that changes were made to prompt after test photos were taken


### Future of the Project
* Design new architecture
