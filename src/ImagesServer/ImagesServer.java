package ImagesServer;

import FileManagment.FilesManager;
import SocialSecondaryServer.ServerInitializer;

import java.io.File;
import java.io.FileNotFoundException;
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
        new ServerInitializer(startPort) {
            @Override
            public void onClientConnection(Socket client) {
                new Thread(new ClientConnection(client)).start();
            }
        };
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
