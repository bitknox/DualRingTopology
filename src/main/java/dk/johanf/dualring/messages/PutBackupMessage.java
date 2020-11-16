package dk.johanf.dualring.messages;

import dk.johanf.dualring.Request;

public class PutBackupMessage extends PutMessage {
    private static final long serialVersionUID = -6466408493867065633L;

    public PutBackupMessage(int key, String value) {
        super(key, value, Request.PUTBACKUP);
    }
}
