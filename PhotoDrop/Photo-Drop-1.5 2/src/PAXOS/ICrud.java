package PAXOS;

//import com.healthmarketscience.rmiio.RemoteInputStream;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.rmi.RemoteException;

/**
 * This class is an abstraction of a CRUD operations.
 */
public interface ICrud extends java.rmi.Remote{

    /**
     * Uploads an image to the database.
     *
     * @param inputStream Inputstream file.
     * @param fileName String file name.
     * @return ObjectId of the file uploaded.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public ObjectId upload(InputStream inputStream, String fileName, String databaseName) throws RemoteException;

    /**
     * Downloads an image from the database.
     *
     * @param fileName String filename.
     * @param outputString String output file path.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void download(String fileName, String outputString, String databaseName) throws RemoteException;

    /**
     * Deletes an image from the database.
     *
     * @param objectId Objectid of image.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                          the execution of a remote method call.
     */
    public void delete(ObjectId objectId, String databaseName) throws RemoteException;
}
