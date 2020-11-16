package dk.johanf.dualring.messages;

import dk.johanf.dualring.Request;

public class PutMessage extends Message {
    private static final long serialVersionUID = -2898609665323194803L;
    private int key;
    private String value;

    public PutMessage(int key, String value) {
        super(Request.PUT);
        this.key = key;
        this.value = value;
    }

    public PutMessage(int key, String value, Request request) {
        super(request);
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
}
