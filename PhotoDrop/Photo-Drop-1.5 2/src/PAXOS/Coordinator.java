package PAXOS;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class represents a coordinator that is responsible for distributing transactions across
 * servers.
 */
public class Coordinator extends java.rmi.server.UnicastRemoteObject implements ICordinator, Serializable {

    private final static Logger LOGGER = Logger.getLogger(Coordinator.class.getName());
    private ArrayList<String> serverNames = new ArrayList<>();
    private ICrud crudOperations;
    private ArrayList<String> hostNames = new ArrayList<>();
    private ArrayList<Integer> portNumbers = new ArrayList<>();
    private ArrayList<IServer> servers = new ArrayList<>();
    private final String UPLOAD = "upload";
    private final String DOWNLOAD = "download";
    private final String DELETE = "delete";
    private ArrayList<String> messages = new ArrayList<>();

    /**
     * Constructs a coordinator object and creates a crud object.
     */
    public Coordinator() throws RemoteException {
        super();
        crudOperations = new Crud();
    }

    /**
     * launcherPrint just takes an input string and formats it to print with the required parameters
     *
     * @param input string to format and print
     */
    public static void coordinatorPrint(String input)
    {
        String printString = "\n" + java.time.LocalTime.now() + ": RMICoordinator: " + input;
        FileWriter fout;
        try {
            fout = new FileWriter("coordinatorLog.txt", true);
            fout.write(printString);
            fout.close();
        } catch (IOException exc) {
            System.out.println(java.time.LocalTime.now() + ": RMICoordinator: Exception in " +
                    "writing to log file: " + exc.getMessage());
        }
        System.out.println(printString);
    }

    @Override
    public void phase_one(String requestType, String filePath, String fileName) throws RemoteException{
        for(int i = 0; i < serverNames.size(); i ++ ) {
            try{
                Registry registry = LocateRegistry.getRegistry(hostNames.get(i), portNumbers.get(i));
                IServer server = (IServer) registry.lookup(serverNames.get(i));
                servers.add(server);
            } catch (NotBoundException e) {
                e.printStackTrace();
                System.out.println("Registry is not bound from the coordinator phase one.");
                coordinatorPrint("Registry is not bound from the coordinator phase one.");
            } catch(RemoteException e) {
                e.printStackTrace();
                System.out.println("Coordinator throws a remote exception from phase one.");
                coordinatorPrint("Coordinator throws a remote exception from phase one.");
            }
        }
        for(IServer server : servers) {
            server.sendMessageToCoordinator("From server: " + server.getServerName(),
                    UPLOAD, filePath, fileName);
        }
        if(messages.contains("ABORT") || messages.size() != portNumbers.size()) {
            LOGGER.info("PHASE 1 WAS ABORTED.");
            coordinatorPrint("PHASE 1 WAS ABORTED.");
            messages.clear();
            servers.clear();
        } else {
            LOGGER.info("PHASE 1 WAS SUCCESS.");
            coordinatorPrint("PHASE 1 WAS SUCCESS.");
            phase_two(requestType, filePath, fileName);
            messages.clear();
            servers.clear();
        }
    }

    @Override
    public void phase_one_delete(String requestType, String fileName) throws RemoteException {
        for(int i = 0; i < serverNames.size(); i ++ ) {
            try{
                Registry registry = LocateRegistry.getRegistry(hostNames.get(i), portNumbers.get(i));
                IServer server = (IServer) registry.lookup(serverNames.get(i));
                servers.add(server);
            } catch (NotBoundException e) {
                e.printStackTrace();
                System.out.println("Registry is not bound from the coordinator phase one.");
                coordinatorPrint("Registry is not bound from the coordinator phase one.");
            } catch(RemoteException e) {
                e.printStackTrace();
                System.out.println("Coordinator throws a remote exception from phase one.");
                coordinatorPrint("Coordinator throws a remote exception from phase one.");
            }
        }
        for(IServer server : servers) {
            server.sendMessageToCoordinatorForDelete("From server: " + server.getServerName(),
                    fileName);
        }
        if(messages.contains("ABORT") || messages.size() != portNumbers.size()) {
            LOGGER.info("PHASE 1 WAS ABORTED.");
            coordinatorPrint("PHASE 1 WAS ABORTED.");
            messages.clear();
            servers.clear();
        } else {
            LOGGER.info("PHASE 1 WAS SUCCESS.");
            coordinatorPrint("PHASE 1 WAS SUCCESS.");
            phaseTwoDelete(fileName);
            messages.clear();
            servers.clear();
        }
    }

    @Override
    public void phase_two(String requestType, String filePath, String fileName) throws RemoteException{
        switch (requestType) {
            case UPLOAD:
                for(IServer server: servers) {
                    try {
                        server.uploadToServer(filePath, fileName);
                    } catch (Error | IOException e) {
                        LOGGER.info("Send upload request to the server failed.");
                        coordinatorPrint("Send upload request to the server failed.");
                        // TODO: NEED TO ADD A ROLL BACK FEATURE IN CASE A SERVER FAILS AFTER PHASE 1
                        break;
                    }
                }
                break;
            case DOWNLOAD:
                break;
            case DELETE:
                break;
        }
    }

    @Override
    public void phaseTwoDelete(String fileName) throws RemoteException {
        for (IServer server : servers) {
            try {
                server.deleteFromServer(fileName);
            } catch (Error | IOException e) {
                LOGGER.info("Send upload request to the server failed.");
                coordinatorPrint("Send upload request to the server failed.");
                // TODO: NEED TO ADD A ROLL BACK FEATURE IN CASE A SERVER FAILS AFTER PHASE 1
                break;
            }
        }
    }


    @Override
    public void uploadImageRequest(String filePath, String fileName) throws IOException {
        phase_one(UPLOAD, filePath, fileName);
    }

    @Override
    public void downloadImageRequest(String fileName, String downloadImagePath) throws RemoteException {
        for(int i = 0; i < serverNames.size(); i ++ ) {
            try{
                Registry registry = LocateRegistry.getRegistry(hostNames.get(i), portNumbers.get(i));
                IServer server = (IServer) registry.lookup(serverNames.get(i));
                server.downloadFromServer(fileName, downloadImagePath);
                System.out.println("SUCCESS DOWNLOADED FROM SERVER");
                coordinatorPrint("SUCCESS DOWNLOADED FROM SERVER");
                break;
            } catch (NotBoundException e) {
                e.printStackTrace();
                System.out.println("Registry is not bound from the coordinator phase one.");
                coordinatorPrint("Registry is not bound from the coordinator phase one.");
                continue;
            } catch(RemoteException e) {
                e.printStackTrace();
                System.out.println("Coordinator throws a remote exception from phase one.");
                coordinatorPrint("Coordinator throws a remote exception from phase one.");
                continue;
            }
        }
    }

    @Override
    public void deleteImageRequest(String deleteFileName) throws RemoteException {
        phase_one_delete(DELETE, deleteFileName);
    }

    @Override
    public void messageBackToClient() throws RemoteException {

    }

    @Override
    public void sendServerNameAndHostName(String serverName, String hostName) throws RemoteException {
        serverNames.add(serverName);
        hostNames.add(hostName);
    }

    @Override
    public void sendPortNumber(Integer portNumber) throws RemoteException {
        portNumbers.add(portNumber);
    }

    @Override
    public void sendBackMessageToCoordintor(String message) throws RemoteException {
        messages.add(message);
    }
}
