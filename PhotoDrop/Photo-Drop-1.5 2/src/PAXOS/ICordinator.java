package PAXOS;

//import com.healthmarketscience.rmiio.RemoteInputStream;
//import com.healthmarketscience.rmiio.RemoteInputStreamServer;
//
//import javax.crypto.KeyAgreement;
import java.io.IOException;
//import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This class is an abstraction of a Coordinator.
 */
public interface ICordinator extends Remote {

    /**
     * Phase one of two phase commit upload.
     *
     * @param requestType String request type.
     * @param filePath String file path.
     * @param fileName String filename.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void phase_one(String requestType, String filePath, String fileName) throws RemoteException;


    /**
     * Phase one of the two phase commit delete.
     *
     * @param requestType String request type.
     * @param fileName String file path.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void phase_one_delete(String requestType, String fileName) throws RemoteException;

    /**
     * phase two of the two phase commit upload.
     *
     * @param requestType String request type.
     * @param filePath String file path.
     * @param fileName String filename.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void phase_two(String requestType, String filePath, String fileName) throws RemoteException;

    /**
     * phase two of the two phase commit delete.
     *
     * @param fileName String file path.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void phaseTwoDelete(String fileName) throws RemoteException;

    /**
     * upload image request.
     *
     * @param filePath String file path.
     * @param fileName String filename.
     * @throws IOException throws input or output exception.
     */
    public void uploadImageRequest(String filePath, String fileName) throws IOException;

    /**
     * download image request.
     *
     * @param fileName String file path.
     * @param filePathDownload String filePathDownload.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void downloadImageRequest(String fileName, String filePathDownload) throws RemoteException;

    /**
     * deletes image request
     *
     * @param deleteFileName name of file
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void deleteImageRequest(String deleteFileName) throws RemoteException;


    /**
     * Message sent back to client
     *
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void messageBackToClient() throws RemoteException;


    /**
     * Sends server name and host name
     *
     * @param serverName name of server
     * @param hostName name of host
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void sendServerNameAndHostName(String serverName, String hostName) throws RemoteException;

    /**
     * Send port number
     *
     * @param portNumber port number
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void sendPortNumber(Integer portNumber) throws RemoteException;

    /**
     * Send message to coordinator
     *
     * @param message message sent to coordinator
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void sendBackMessageToCoordintor(String message) throws RemoteException;
}
