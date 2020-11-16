package dk.johanf.dualring.messages;

import dk.johanf.dualring.Request;

public class ResponseMessage extends Message {
    private static final long serialVersionUID = 1906010143921118760L;
    private String value;

    public ResponseMessage(String value){
        super(Request.RESPONSE);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
