package PAXOS;

import org.bson.types.ObjectId;

import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This class represents a Server that is responsible for processing a clients requests.
 */
public class Server extends Crud implements Runnable, IServer, Serializable {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private  ICordinator cordinator;
    private String serverName;
    private String databaseName;
    private HashMap<String, ObjectId> cache = new HashMap<>();

    /**
     * Constructs a server object.
     *
     * @param cordinator coordinator object
     * @param serverName name of server
     * @param databaseName name of database
     */
    public Server(ICordinator cordinator, String serverName, String databaseName) throws RemoteException {
        this.cordinator = cordinator;
        this.serverName = serverName;
        this.databaseName = databaseName;
    }

    /**
     * launcherPrint just takes an input string and formats it to print with the required parameters
     *
     * @param input string to format and print
     */
    public static void serverPrint(String input)
    {
        String printString = "\n" + java.time.LocalTime.now() + ": RMIServer: " + input;
        FileWriter fout;
        try {
            fout = new FileWriter("serverLog.txt", true);
            fout.write(printString);
            fout.close();
        } catch (IOException exc) {
            System.out.println(java.time.LocalTime.now() + ": RMIServer: Exception in writing to log file: " + exc.getMessage());
        }
        System.out.println(printString);
    }

    @Override
    public void sendMessageToCoordinator(String message, String requestType, String filePath,
                                         String fileName) throws RemoteException {
        switch (requestType) {
            case "upload":
                try {
                    InputStream file = new FileInputStream(new File(filePath));
                    //upload(file, fileName, databaseName);
                    LOGGER.info("SUCCESS CAN UPLOAD FROM SERVER." + message);
                    serverPrint("SUCCESS CAN UPLOAD FROM SERVER." + message);
                    cordinator.sendBackMessageToCoordintor("OK");
                }  catch (Error | FileNotFoundException e) {
                    LOGGER.info("Aborted" + message);
                    serverPrint("Aborted" + message);
                    cordinator.sendBackMessageToCoordintor("ABORT");
                }
                break;
            case "download":
                break;
            case "delete":
                break;
        }
    }

    @Override
    public void sendMessageToCoordinatorForDelete(String message, String fileName) throws RemoteException {
        try {
            LOGGER.info("SUCCESS CAN UPLOAD FROM SERVER." + message);
            serverPrint("SUCCESS CAN UPLOAD FROM SERVER." + message);
            cordinator.sendBackMessageToCoordintor("OK");
        } catch (Error e) {
            LOGGER.info("Aborted" + message);
            serverPrint("Aborted" + message);
            cordinator.sendBackMessageToCoordintor("ABORT");
        }
    }


    @Override
    public String getServerName() throws RemoteException {
        return this.serverName;
    }

    @Override
    public void uploadToServer(String filePath, String fileName) throws RemoteException {
        try {
            InputStream file = new FileInputStream(new File(filePath));
            ObjectId id = upload(file, fileName, databaseName);
            cache.put(fileName, id);
            LOGGER.info("Success, uploaded the file to the server.");
            serverPrint("Success, uploaded the file to the server.");
        } catch (Error | FileNotFoundException e) {
            LOGGER.info("Could not upload the file to the server.");
            serverPrint("Could not upload the file to the server.");
            cache.remove(fileName);
        } catch(RemoteException e) {
            LOGGER.info("Could not upload the file to the server. Remote Exception.");
            serverPrint("Could not upload the file to the server. Remote Exception.");
            cache.remove(fileName);
        }
    }

    @Override
    public void deleteFromServer(String fileName) throws RemoteException {
        try {
            delete(cache.get(fileName), databaseName);
            LOGGER.info("Success, deleted the file from the server.");
            serverPrint("Success, deleted the file from the server.");
        } catch (Error e) {
            LOGGER.info("Could not delete the file from the server.");
            serverPrint("Could not delete the file from the server.");
        } catch (RemoteException e) {
            LOGGER.info("Could not delete the file from the server.");
            serverPrint("Could not delete the file from the server.");
        }
    }

    @Override
    public void downloadFromServer(String fileName, String downloadFilePath) throws RemoteException {
        try {
            download(fileName, downloadFilePath, databaseName);
            LOGGER.info("Success, downloaded from the server.");
            serverPrint("Success, downloaded from the server.");
        } catch (Error e) {
            LOGGER.info("Could not download the file from the server.");
            serverPrint("Could not download the file from the server.");
        } catch (RemoteException e) {
            LOGGER.info("Could not download the file from the server.");
            serverPrint("Could not download the file from the server.");
        }
    }

    @Override
    public void run() {
        execute();
    }

    /**
     * Run thread
     */
    public void execute() {

    }
}
