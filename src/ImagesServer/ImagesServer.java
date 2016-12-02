package ImagesServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kemo on 28/11/2016.
 */

public class ImagesServer {
    public static void main(String... args) {
        final int startPort = 6010;
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
}
