package ImagesServer;

import FileManagment.FilesManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by kemo on 28/11/2016.
 */

public class ImagesServer {
    public static int imageID;
    public static void main(String... args) {
        final int startPort = 6010;
        loadData();
        startMainConnection(startPort);
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private static void startMainConnection(int startPort) {
        for (int i = startPort; i < startPort + 10; i++) {
            try {
                //create a server socket where the client will connect on the specified startPort
                ServerSocket serverSocket = new ServerSocket(i);
                while (true) { // loop where the server wait for client to start his connection may need to make these process in another thread
                    Socket client = serverSocket.accept();
                    new Thread(new ClientConnection(client)).start();

                }
            } catch (IOException e) {
                //Error reporting 4 Debugging later will use log class

            }

        }
    }
    private static void loadData()
    {
        FilesManager.CreateFolder(ClientConnection.IMAGES_FOLDER);
        File file = new File(ClientConnection.IMAGES_ID_FILE);
        try {
            Scanner scanner = new Scanner(file);
            imageID = scanner.nextInt();
        } catch (FileNotFoundException e) {
            imageID = 0;
        }
    }
}
