package dk.johanf.dualring.messages;

import java.util.UUID;

import dk.johanf.dualring.Request;

public class GetMessage extends Message {
    private static final long serialVersionUID = -2489963237066305417L;
    private String host;
    private int port;
    private int key;
    private UUID id;
    private Boolean left;

    public GetMessage(String host, int port, int key) {
        super(Request.GET);
        this.host = host;
        this.port = port;
        this.key = key;
        id = UUID.randomUUID();
        left = null;
    }

    public int getKey() {
        return key;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getId() {
        return id.toString();
    }

    public void setSendLeft(boolean left) {
        this.left = left;
    }

    public Boolean sendLeft() {
        return left;
    }
}
