package PAXOS;

import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class starts the coordinator.
 */
public class StartCoordinator {

    /**
     * startLogFile creates a local text file for logging
     */
    public static void startLogFile()
    {
        try {
            File logFile = new File("coordinatorLog.txt");
            if(logFile.exists()){
                logFile.delete();
            }
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException exc) {
            System.out.println(java.time.LocalTime.now() + ": RMICoordinator: Exception in log " +
                    "file creation: " + exc.getMessage());
        }
    }

    /**
     * Driver method for starting the coordinator.
     *
     * @param args String user arguments.
     * @throws RemoteException throws remote exception.
     */
    public static void main(String [] args) throws RemoteException, AlreadyBoundException {

        startLogFile();
        if(args.length < 1) {
            System.exit(1);
        }
        try {
            int portNumber = Integer.parseInt(args[0]);
            Coordinator coordinator = new Coordinator();
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.bind("Coordinator", coordinator);
            System.out.println("SUCCESS CONNECTED TO THE COORDINATOR.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Arguments not in the right format.");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Coordinator gave a remote exception.");
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
            System.out.println("Coordintor gave an already bound exception.");
        }
    }
}
