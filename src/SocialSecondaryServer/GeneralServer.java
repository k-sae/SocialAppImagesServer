package SocialSecondaryServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kemo on 11/12/2016.
 */
public abstract class GeneralServer implements Connection {
    protected Socket clientSocket;
    public GeneralServer(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        sendVerificationCode();
        startConnection();
    }

    private void sendVerificationCode()
    {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataOutputStream.write(VERIFICATION.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
