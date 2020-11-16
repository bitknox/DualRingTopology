package dk.johanf.dualring.clients;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import dk.johanf.dualring.messages.GetMessage;
import dk.johanf.dualring.messages.ResponseMessage;



/**
 * GetClient for recieving messages
 */
public class GetClient {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter host to connect to: ");
        String host = sc.nextLine();
        System.out.print("Enter port for host: ");
        int port = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter localport: ");
        int localPort = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter key: ");
        int key = sc.nextInt();
        sc.close();
        new GetClient(host, localPort, port, key);
    }

    public GetClient(String host, int localPort, int port, int key) {
        try {
            serverSocket = new ServerSocket(localPort);
            new Thread(new ConnectionListener()).start();
            Socket socket = new Socket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new GetMessage(host, localPort, key));
            objectOutputStream.flush();
            socket.close();
        } catch (IOException e) {
            
        }
    }

    private class ConnectionListener implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connection established");
                ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
                ResponseMessage res = (ResponseMessage) reader.readObject();
                System.out.println(res.getValue());
                socket.close();
                serverSocket.close();
            } catch (IOException | ClassNotFoundException e) {

            }
        }
    }
}