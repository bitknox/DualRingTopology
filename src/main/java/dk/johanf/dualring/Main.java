package dk.johanf.dualring;

import dk.johanf.dualring.clients.GetClient;
import dk.johanf.dualring.clients.PutClient;
import dk.johanf.dualring.nodes.Node;

/**
 * The purpose of the Main class is to quickly test the testcase given in the
 * assignment Once in a while it will produce a NullPointerException, this will
 * not happen when the nodes and clients are run manually. If the exception
 * occurs, just try it again.
 */
public class Main {
    static final int sleepvalue = 500;
    
    public static void main(String[] args) {
        try {
            Node node = new Node(1025);
            Node secondNode = new Node(1026);
            secondNode.join("localhost", 1025);
            Thread.sleep(sleepvalue);
            new PutClient("localhost", 1025, 1, "A");
            Thread.sleep(sleepvalue);
            new GetClient("localhost", 2048, 1025, 1);
            Thread.sleep(sleepvalue);
            new GetClient("localhost", 2049, 1026, 1);
            Thread.sleep(sleepvalue);
            new GetClient("localhost", 2049, 1025, 2);
            Thread.sleep(sleepvalue);
            Node thirdNode = new Node(1027);
            Thread.sleep(sleepvalue);
            thirdNode.join("localhost", 1026);
            Thread.sleep(sleepvalue);
            new GetClient("localhost", 2048, 1027, 1);
            Thread.sleep(sleepvalue);
            new PutClient("localhost", 1025, 2, "B");
            Thread.sleep(sleepvalue);
            node.closeForcefully();
            Thread.sleep(sleepvalue);
            new GetClient("localhost", 2048, 1026, 2); // recieve B            
        } catch (Exception e) {
            
        }

    }
}

