package ImagesServer;

import FileManagment.FilesManager;
import SocialAppGeneral.Command;
import SocialAppGeneral.Connection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by kemo on 23/10/2016.
 */

class ClientConnection implements Connection , SocialAppImages {
    private Socket clientSocket;
    private final String IMAGES_FOLDER = "Images\\";
    private static final String IMAGES_ID_FILE = "ID.sasf";
    private static int imageID;
    ClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
        FilesManager.CreateFolder(IMAGES_FOLDER);
        File file = new File("ID.ISSF");
        try {
            Scanner scanner = new Scanner(file);
            imageID = scanner.nextInt();
        } catch (FileNotFoundException e) {
            imageID = 0;
        }

        sendVerificationCode();
        startConnection();
    }
    //TODO #kareem
    //Check for user input info

    private void sendVerificationCode()
    {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataOutputStream.write(VERIFICATION.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startConnection() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendImage(String ID) throws IOException {
        File file = new File(IMAGES_FOLDER+ID);
        BufferedImage bufferedImage=  ImageIO.read(file);
       ImageIO.write(bufferedImage,"png", clientSocket.getOutputStream());

    }
    private void receiveImage() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(clientSocket.getInputStream());
        int imageID = ClientConnection.imageID;
        sendImageID(imageID);
        File file = new File(IMAGES_FOLDER+imageID);
        ImageIO.write(bufferedImage,"jpg",file);
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
        if (bufferedWriter != null) {
            bufferedWriter.write((imageID++)+"");
            bufferedWriter.close();
        }

    }
}



