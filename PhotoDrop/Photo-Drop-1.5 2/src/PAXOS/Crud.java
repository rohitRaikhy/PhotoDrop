package PAXOS;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This class is an implementation of CRUD that is responsible for upload, download, and delete
 * operations.
 */
public class Crud extends java.rmi.server.UnicastRemoteObject implements ICrud, Serializable {

    /**
     * Constructs a crud object.
     */
    public Crud() throws RemoteException {
        super();
    }

    @Override
    public ObjectId upload(InputStream inputStream, String fileName, String databaseName) throws RemoteException {
        System.out.println("Calling upload...");
        String con = String.format("mongodb+srv://testUser:testUser@server1." +
                "w54b6.mongodb.net/%s?retryWrites=true&w=majority", databaseName);
        MongoClient mongoClient = MongoClients.create(con);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        ObjectId fileId = null;
        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            GridFSBucket gridBucket = GridFSBuckets.create(database);
            GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024).
                    metadata(new Document("type", "image").append("upload_date", format.parse("2016-09-01T00:00:00Z")).
                            append("content_type", "image/jpg"));
            fileId = gridBucket.uploadFromStream(fileName, (InputStream) inputStream, uploadOptions);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }
        return fileId;
    }

    @Override
    public void download(String fileName, String outputString, String databaseName) throws RemoteException {
        System.out.println("Calling download...");
        String con = String.format("mongodb+srv://testUser:testUser@server1." +
                "w54b6.mongodb.net/%s?retryWrites=true&w=majority", databaseName);
        MongoClient mongoClient = MongoClients.create(con);

        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            GridFSBucket gridBucket = GridFSBuckets.create(database);

            FileOutputStream fileOutputStream = new FileOutputStream(outputString);
            gridBucket.downloadToStream(fileName, fileOutputStream);
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }
    }

    @Override
    public void delete(ObjectId objectId, String databaseName) throws RemoteException {
        System.out.println("Calling delete...");
        String con = String.format("mongodb+srv://testUser:testUser@server1." +
                "w54b6.mongodb.net/%s?retryWrites=true&w=majority", databaseName);
        MongoClient mongoClient = MongoClients.create(con);
        try {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            GridFSBucket gridBucket = GridFSBuckets.create(database);
            gridBucket.delete(objectId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }
    }
}
