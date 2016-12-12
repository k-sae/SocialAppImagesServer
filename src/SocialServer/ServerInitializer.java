package SocialServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kemo on 12/12/2016.
 */
public abstract class ServerInitializer {
    @SuppressWarnings("InfiniteLoopStatement")
    protected ServerInitializer(int startPort)
    {
        startMainConnection(startPort);
    }
    private void startMainConnection(int startPort) {
        for (int i = startPort; i < startPort + 10; i++) {
            try {
                //create a server socket where the client will connect on the specified startPort
                ServerSocket serverSocket = new ServerSocket(i);
                //noinspection InfiniteLoopStatement
                while (true) { // loop where the server wait for client to start his connection may need to make these process in another thread
                    Socket client = serverSocket.accept();
                  onClientConnection(client);

                }
            } catch (IOException e) {
                //Error reporting 4 Debugging later will use log class

            }

        }
    }
    public abstract void onClientConnection(Socket client);
}
