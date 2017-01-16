package ImagesServer;

import FileManagment.FilesManager;
import SocialAppGeneral.Command;
import SocialSecondaryServer.GeneralServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import static ImagesServer.ImagesServer.imageID;

/**
 * Created by kemo on 23/10/2016.
 */

public class ClientConnection extends GeneralServer implements Runnable, SocialAppImages {

    static final String IMAGES_FOLDER = "Images\\";
     static final String IMAGES_ID_FILE = "ID.sasf";

    public ClientConnection(Socket clientSocket) {
        super(clientSocket);
    }
    @Override
    public void startConnection() {
      //no thing special need to do on start connection

    }

    private void sendImage(String ID) throws IOException {
//        File file = new File(IMAGES_FOLDER+ID);
//        BufferedImage bufferedImage=  ImageIO.read(file);
//       ImageIO.write(bufferedImage,"png", clientSocket.getOutputStream());
        FileInputStream fileInputStream = new FileInputStream(IMAGES_FOLDER+ID);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectOutputStream.writeObject(bytes);

    }
    private void receiveImage() throws IOException, ClassNotFoundException {
        int imageID = ImagesServer.imageID;
        sendImageID(imageID);
        byte[] bytes = (byte[]) new ObjectInputStream(clientSocket.getInputStream()).readObject();
        File file = new File(IMAGES_FOLDER+imageID);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        ImageIO.write(img,"jpg",file);
        increaseImageID();
    }
    private void sendImageID(int ID) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        Command command = new Command();
        command.setKeyWord("ImageID");
        command.setSharableObject(ID + "");
        dataOutputStream.writeUTF(command.toString());
    }
    private synchronized static void increaseImageID() throws IOException {

        BufferedWriter bufferedWriter = FilesManager.OpenToWrite(IMAGES_ID_FILE);
        imageID++;
        if (bufferedWriter != null) {
            bufferedWriter.write(imageID +"");
            bufferedWriter.close();
        }

    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            Command command = Command.fromString(dataInputStream.readUTF());
            if (command != null && command.getKeyWord().equals(DOWNLOADIMAGE))
            {
                sendImage(command.getObjectStr());
            }
            else if (command != null && command.getKeyWord().equals(UPLOADIMAGE))
            {
                receiveImage();
            }
            //TODO #kareem
            //if any errors introduced remove this
            clientSocket.close();
        } catch (IOException ignored) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}



