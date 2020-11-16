package dk.johanf.dualring.clients;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import dk.johanf.dualring.messages.PutMessage;


/**
 * PutClient for inserting messages
 */
public class PutClient {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter host to put to: ");
        String host = sc.nextLine();
        System.out.print("Enter port for host: ");
        int port = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter key: ");
        int key = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter value: ");
        String value = sc.nextLine();
        sc.close();
        new PutClient(host, port, key, value);
    }

    public PutClient(String host, int port, int key, String value) {

        try {
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new PutMessage(key, value));
            objectOutputStream.flush();
            socket.close();
        } catch (IOException e) {
            
        }
    }
}