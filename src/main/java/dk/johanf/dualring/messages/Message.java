package dk.johanf.dualring.messages;

import java.io.Serializable;

import dk.johanf.dualring.Request;

public class Message implements Serializable {
    private static final long serialVersionUID = 3979703568978610948L;
    private Request request;
    
    public Message(Request request){
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
