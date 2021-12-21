package PAXOS;

//import com.healthmarketscience.rmiio.RemoteInputStream;
//import com.healthmarketscience.rmiio.RemoteInputStreamServer;
//
//import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This class is an abstraction of a Server.
 */
public interface IServer extends Remote {

    /**
     * Send message to coordinator
     *
     * @param message message sent to coordinator
     * @param requestType upload download or delete operation
     * @param fileName name of file
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void  sendMessageToCoordinator(String message, String requestType,
                                          String filePath, String fileName) throws RemoteException;

    /**
     * Send message to coordinator for delete
     *
     * @param message message sent to coordinator for delete
     * @param fileName name of file
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void sendMessageToCoordinatorForDelete(String message, String fileName) throws RemoteException;

    /**
     * Return server name
     *
     * @return return server name
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public String getServerName() throws RemoteException;

    /**
     * Upload file to server
     *
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void uploadToServer(String filePath, String fileName) throws RemoteException;

    /**
     * Delete file from server
     *
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void deleteFromServer(String fileName) throws RemoteException;

    /**
     * Download file from server
     *
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void downloadFromServer(String fileName, String downloadImagePath) throws RemoteException;

}
