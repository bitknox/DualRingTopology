package dk.johanf.dualring.nodes;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class AdjacentNode {
    InetAddress host;
    int port;

    public AdjacentNode(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getPort(){
        return port;
    }
    public Socket getSocket() throws UnknownHostException, IOException {
        return new Socket(host,port);
    }
}
