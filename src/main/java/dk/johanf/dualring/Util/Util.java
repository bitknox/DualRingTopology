package dk.johanf.dualring.Util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dk.johanf.dualring.messages.Message;



public class Util {
    public static void writeMessage(Socket socket, Message message) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); 
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            
        }
    }
}
