package dk.johanf.dualring.nodes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import dk.johanf.dualring.Request;
import dk.johanf.dualring.UnknownRequestException;
import dk.johanf.dualring.Util.Util;
import dk.johanf.dualring.messages.ConnectMessage;
import dk.johanf.dualring.messages.GetMessage;
import dk.johanf.dualring.messages.Message;
import dk.johanf.dualring.messages.PutBackupMessage;
import dk.johanf.dualring.messages.PutMessage;
import dk.johanf.dualring.messages.ResponseMessage;


public class Node {
    int localPort;
    ServerSocket server;
    AdjacentNode rightNeighbour;
    AdjacentNode leftNeighbour;

    Map<Integer, String> data;
    Set<String> receivedMessages;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter port for this node: ");
        int port = sc.nextInt();
        sc.nextLine();
        Node node = new Node(port);

        System.out.print("Enter host (keep empty if it's the first node): ");
        String host = sc.nextLine();

        // Evaluates false if the node is the first node in the structure
        if (!host.equals("")) {
            System.out.print("Enter port for node to connect to: ");
            int nodePort = sc.nextInt();
            sc.close();
            node.join(host, nodePort);
        }
    }

    public Node(int localPort) {
        this.localPort = localPort;
        data = new HashMap<>();
        receivedMessages = new HashSet<>();
        try {
            server = new ServerSocket(localPort);
            listen();
        } catch (IOException e) {

        }
    }

    public void join(String host, int port) {
        try {
            System.out.println("localport" + localPort + " is connecting to " + port);
            Socket socket = new Socket(host, port);
            Util.writeMessage(socket, new ConnectMessage(Request.NEWCON, localPort));
        } catch (IOException e) {

        }
    }

    public void closeForcefully() throws IOException {
        server.close();
    }

    private void listen() {
        new Thread(() -> {
            while (!server.isClosed()) {
                Socket con;
                try {
                    con = server.accept();
                    new Thread(new ConnectionHandler(con)).start();
                } catch (IOException e) {

                }
            }
        }).start();
    }

    private class ConnectionHandler implements Runnable {
        Socket socket;

        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) input.readObject();
                switch (message.getRequest()) {
                    case NEWCON:
                        ConnectMessage connectMessage = (ConnectMessage) message;
                        int port = localPort;
                        if (rightNeighbour != null) {
                            port = rightNeighbour.getPort(); // Change port to old neighbour port
                        }
                        rightNeighbour = new AdjacentNode(socket.getInetAddress(), connectMessage.getPort());
                        Util.writeMessage(rightNeighbour.getSocket(), new ConnectMessage(Request.CONNECT, port));
                        Util.writeMessage(rightNeighbour.getSocket(),
                                new ConnectMessage(Request.CONNECTREPLY, server.getLocalPort()));
                        break;
                    case CONNECT:
                        ConnectMessage conMessage = (ConnectMessage) message;
                        rightNeighbour = new AdjacentNode(socket.getInetAddress(), conMessage.getPort());
                        Util.writeMessage(rightNeighbour.getSocket(),
                                new ConnectMessage(Request.CONNECTREPLY, server.getLocalPort()));
                        break;
                    case CONNECTREPLY:
                        ConnectMessage conMsg = (ConnectMessage) message;
                        leftNeighbour = new AdjacentNode(socket.getInetAddress(), conMsg.getPort());
                        break;
                    case GET:
                        GetMessage getMessage = (GetMessage) message;
                        int key = getMessage.getKey();
                        String value = data.get(key);
                        if (value != null) {
                            Socket socket = new Socket(getMessage.getHost(), getMessage.getPort());
                            Util.writeMessage(socket, new ResponseMessage(value));
                            socket.close();
                        } else if (!receivedMessages.contains(getMessage.getId())) {
                            receivedMessages.add(getMessage.getId());
                            // Send both ways if the direction isn't set
                            if (getMessage.sendLeft() == null) {
                                getMessage.setSendLeft(false);
                                Util.writeMessage(rightNeighbour.getSocket(), getMessage);
                                getMessage.setSendLeft(true);
                                Util.writeMessage(leftNeighbour.getSocket(), getMessage);
                            } else if (getMessage.sendLeft() == true) { // Send left
                                Util.writeMessage(leftNeighbour.getSocket(), getMessage);
                            } else { // Send right
                                Util.writeMessage(rightNeighbour.getSocket(), getMessage);
                            }
                        } else { // Message has traversed the entire circle
                            Socket socket = new Socket(getMessage.getHost(), getMessage.getPort());
                            Util.writeMessage(socket, new ResponseMessage("does not exist"));
                            socket.close();
                        }
                        break;
                    case PUT:
                        PutMessage putMessage = (PutMessage) message;
                        data.put(putMessage.getKey(), putMessage.getValue());
                        Util.writeMessage(rightNeighbour.getSocket(),
                                new PutBackupMessage(putMessage.getKey(), putMessage.getValue()));
                        break;
                    case PUTBACKUP:
                        PutBackupMessage putBackupMessage = (PutBackupMessage) message;
                        data.put(putBackupMessage.getKey(), putBackupMessage.getValue());
                        break;
                    default:
                        System.out.println(message.getRequest());
                        throw new UnknownRequestException("Unknown request type");
                }
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Node died");
            }
        }
    }
}