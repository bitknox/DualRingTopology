package dk.johanf.dualring.messages;

import dk.johanf.dualring.Request;

public class ConnectMessage extends Message {
    private static final long serialVersionUID = 1L;
    private int port;

    public ConnectMessage(Request request, int port) {
        super(request);
        this.port = port;
    }  
    
    public int getPort(){
        return port;
    }
}
