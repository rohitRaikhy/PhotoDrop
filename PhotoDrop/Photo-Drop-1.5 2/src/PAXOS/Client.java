package PAXOS;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class represents a Client that is responsible for sending request to the server.
 */
public class Client implements Serializable{

    /**
     * Constructor for the client.
     * @throws RemoteException throws remote exception if an error occurs occur during
     *                                the execution of a remote method call.
     */
    public Client() throws RemoteException {
        super();
    }

    private final static Logger LOGGER = Logger.getLogger(Client.class.getName());
    private static String hostName;
    private static int portNumber;
    private static int cordPortNumber;
    private static String coordName;

    /**
     * Input Stream parser.
     *
     * @param filePath String file path.
     * @return returns input stream.
     * @throws FileNotFoundException throws file not found exception.
     */
    public static InputStream parseFilePath(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(filePath));
        return inputStream;
    }

    /**
     * Welcome message for the client.
     */
    public static void welcomeMessage(){
        System.out.println("******** Welcome to the client interface. ************\n\n" +
                "Please choose a command from upload, download or delete.\n" +
                "If you need help choosing, please enter the keyword help. \n" +
                "Enter quit to exit.");
        clientPrint("******** Welcome to the client interface. ************\n\n" +
                "Please choose a command from upload, download or delete.\n" +
                "If you need help choosing, please enter the keyword help. \n" +
                "Enter quit to exit.");
    }

    /**
     * help message for the client.
     */
    public static void helpMessage() {
        System.out.println("PhotoDrop can support the following commands:\n\n" +
                "Upload, Download, Delete \n" +
                "Enter one of these commands.");
        clientPrint("PhotoDrop can support the following commands:\n\n" +
                "Upload, Download, Delete \n" +
                "Enter one of these commands.");
    }

    /**
     * Checks if a string matches any of the valid commands
     *
     * @param inputString command string from an input or a response
     * @return            boolean - whether it is valid or not
     */
    public static boolean validateCommand(String inputString) {
        String[] validCommands = {"upload","download","delete", "help", "quit"};

        // This checks if the input command is upload, download, delete, help, quit
        return Arrays.stream(validCommands).anyMatch(inputString::contains);
    }

    /**
     * Checks if the file path is valid
     *
     * @param          filePath  path to file
     * @return         boolean - whether file path is valid
     */
    public static boolean validateFileExists(String command, String filePath){
        if (command.equals("upload")) {
            Path path = Paths.get(filePath);
            return Files.exists(path);
        } else {
            String[] pathArray = filePath.split("/");
            String concatPathWithoutFileName = "";
            for (int i = 0; i < pathArray.length-1; i++) {
                concatPathWithoutFileName = concatPathWithoutFileName + pathArray[i] + "/";
            }
            Path path = Paths.get(concatPathWithoutFileName);
            return Files.exists(path);
        }
    }

    /**
     * Checks if the file name is equivalent to the file name in the path for upload operation
     *
     * @param             filePath path to file
     * @param             file file name
     * @return            boolean - whether it is valid or not
     */
    public static boolean validateFileNameAndPath(String filePath, String file){
        String[] pathArray = filePath.split("/");
        String fileNameFromPath = pathArray[pathArray.length-1];
        if (fileNameFromPath.equals(file)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Format input and print it to the client log
     *
     * @param input string to format and print
     */
    public static void clientPrint(String input)
    {
        String printString = "\n" + java.time.LocalTime.now() + ": RMIClient: " + input;
        FileWriter fOut;
        try {
//      fOut = new FileWriter("clientLog.txt", true);
            fOut = new FileWriter("clientLog" + portNumber + ".txt", true);
            fOut.write(printString);
            fOut.close();
        } catch (IOException exc) {
            System.out.println(java.time.LocalTime.now() + ": RMIClient: Exception in writing to log file: " + exc.getMessage());
        }
        System.out.println(printString);

    }

    /**
     * Creates a local text file for logging
     */
    public static void startLogFile()
    {
        try {
//      File logFile = new File("clientLog.txt");
            File logFile = new File("clientLog" + portNumber + ".txt");
            if(logFile.exists()){
                logFile.delete();
            }
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException exc) {
            System.out.println(java.time.LocalTime.now() + ": RMIClient: Exception in log file " +
                    "creation: " + exc.getMessage());
        }

    }

    /**
     * Driver of the client.
     *
     * @param args user inputted args.
     * @throws IOException throws input output exception.
     */
    public static void main(String[] args) throws IOException {
        FileHandler fh = new FileHandler("myLogRmiClient.txt");
        fh.setLevel(Level.INFO);
        LOGGER.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        if (args.length < 2) {
            System.exit(1);
        }
        try {
            hostName = args[0].toString();
            portNumber = Integer.parseInt(args[1]);
            coordName = args[2].toString();
            cordPortNumber = Integer.parseInt(args[3]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.info("Illegal arguments.");
            System.out.println("Illegal arguments. (hostName, port number, file path, coordName)");
        }
        try {
            Registry registry = LocateRegistry.getRegistry(hostName, cordPortNumber);
            ICordinator coordinator = (ICordinator) registry.lookup(coordName);
            startLogFile();
            clientPrint("starting");
            while(true) {
                welcomeMessage();
                boolean validateCommand;
                Scanner in = new Scanner(System.in);
                String request = in.nextLine();
                request = request.trim();
                clientPrint("Request sent by client: " + request);
                validateCommand = validateCommand(request);
                if (!validateCommand){
                    continue;
                } else {
                    if(request.toLowerCase().equals("help")) {
                        helpMessage();
                        request = in.nextLine();
                    } else if (request.toLowerCase().equals("quit")) {
                        break;
                    }
                    switch (request.toLowerCase()) {
                        case "upload":
                            System.out.println("Enter a file path for to upload to server.");
                            clientPrint("Enter a file path for to upload to server.");
                            String filePath = in.nextLine();
                            clientPrint("File path entered by client: " + filePath);
                            filePath = filePath.trim();
                            boolean fileExistsUpload = validateFileExists("upload", filePath);
                            System.out.println("Now enter the fileName for the file.");
                            clientPrint("Now enter the fileName for the file.");
                            String fileName = in.nextLine();
                            clientPrint("File name entered by client: " + fileName);
                            fileName = fileName.trim();
                            boolean fileNameEqualsPathFileNameUpload =
                                    validateFileNameAndPath(filePath, fileName);
                            if (fileExistsUpload && fileNameEqualsPathFileNameUpload) {
                                InputStream file = parseFilePath(filePath);
                                try {
                                    long sTime = System.currentTimeMillis();
                                    coordinator.uploadImageRequest(filePath, fileName);
                                    long totalTime = System.currentTimeMillis()-sTime;
                                    if (totalTime <= 100000) {
                                        System.out.println("SUCCESS UPLOAD");
                                    } else {
                                        System.out.println("TIMED OUT");
                                        clientPrint("TIMED OUT");
                                        System.exit(0);
                                    }
                                    clientPrint("Success UPLOAD");
                                } catch (Error e) {
                                    e.printStackTrace();
                                    System.out.println("Failed UPLOAD!!! ");
                                    clientPrint("Success FAILED");
                                }
                            } else {
                                System.out.println("");
                                System.out.println("ERROR either the path is not valid or the " +
                                        "file name in the path do not match the file name given");
                                clientPrint("ERROR either the path is not valid or the " +
                                        "file name in the path do not match the file name given");
                            }
                            break;
                        case "download":
                            System.out.println("Please enter the fileName for the file to download.");
                            clientPrint("Please enter the fileName for the file to download.");
                            String downloadFileName = in.nextLine();
                            clientPrint("File name entered by client: " + downloadFileName);
                            downloadFileName = downloadFileName.trim();
                            System.out.println("Please enter the filePath to store on machine.");
                            clientPrint("Please enter the filePath to store on machine.");
                            String filePathDownload = in.nextLine();
                            clientPrint("File path entered by client: " + filePathDownload);
                            filePathDownload = filePathDownload.trim();
                            boolean fileExistsDownload = validateFileExists("download",
                                    filePathDownload);
                            if (fileExistsDownload) {
                                long sTime = System.currentTimeMillis();
                                coordinator.downloadImageRequest(downloadFileName, filePathDownload);
                                long totalTime = System.currentTimeMillis()-sTime;
                                if (totalTime <= 100000) {
                                    clientPrint("Success Download");
                                } else {
                                    System.out.println("TIMED OUT");
                                    clientPrint("TIMED OUT");
                                    System.exit(0);
                                }
                            } else{
                                System.out.println("");
                                System.out.println("ERROR the path is not valid");
                                clientPrint("ERROR the path is not valid");
                            }
                            break;
                        case "delete":
                            System.out.println("Enter a file name to delete from the server.");
                            clientPrint("Enter a file name to delete from the server.");
                            String deleteFileName = in.nextLine();
                            clientPrint("File name entered " + deleteFileName);
                            deleteFileName = deleteFileName.trim();
                            long sTime = System.currentTimeMillis();
                            coordinator.deleteImageRequest(deleteFileName);
                            long totalTime = System.currentTimeMillis()-sTime;
                            if (totalTime <= 100000) {
                                clientPrint("Success Delete");
                            } else {
                                System.out.println("TIMED OUT");
                                clientPrint("TIMED OUT");
                                System.exit(0);
                            }
                            break;
                    }
                }
            }
       } catch (NotBoundException e) {
            System.out.println("The coordinator is not bound. Please try again.");
            clientPrint("The coordinator is not bound. Please try again.");
        }
    }
}
