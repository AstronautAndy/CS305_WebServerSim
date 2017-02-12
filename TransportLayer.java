
/**
 * The transport layer needs to implement a 3-way handshake
 */
public class TransportLayer
{
    byte sys = 0;
    byte ack = 1; //Represents a simple TCP acknowledgement that the server has received an open request from the client
    byte[] sysMessage = {sys};
    byte[] ackMessage = {ack}; //A simple, one element array of bytes
    private NetworkLayer networkLayer;
    boolean acknowledgement = false;
    
    //server is true if the application is a server (should listen) or false if it is a client (should try and connect)
    public TransportLayer(boolean server) //Include a pointer to the delay from the Client/Server app
    {
        networkLayer = new NetworkLayer(server);
    }
    
    /**
     * Method that the client application layer will use to open a connection. Forms the first part of the 
     * 3-way handshake, and returns "true" should the second part (receiving the 'ack' message from the 
     * server) succeed.
     */
    public boolean requestOpening(){
        System.out.println("Client is requesting opening");
        send(sysMessage);
        if(acknowledgement == true){
            acknowledgement = false; //set acknowledgement state back to false (default)
            return true;
        }
        else{
            return false;
        }
        
        //Now needs a means of listening for the ack message and returning true if the Transport Layer receives said message
        
    }
    
    /**
     * Whenever the program sends a messages, it needs to send a TCP message, wait for
     * an acknowledgement, then send a request message.
     */
    public void send(byte[] payload)
    {
        //Enable delay period
        int pDelay = payload.length *  DelayData.PropagationDelay; //P. delay needs to scale
        System.out.println("Now sending payload with header: " + payload[0]);
        try{
            Thread.sleep(pDelay); 
            Thread.sleep(DelayData.transmissionDelay);
        }catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        networkLayer.send( payload );
    }

    public byte[] receive()
    {
        byte[] payload = networkLayer.receive(); 
        System.out.println("Now receiving payload with header: " + payload[0]); 
        switch(payload[0]){ //If the program is acting fishy, remove this block
           case 0: System.out.println("processed open connection request: sending acknowledgement"); send(ackMessage); return payload; // Send the acknowledgement message back to the requesting tLayer
           case 1: System.out.println("Acknowledgement has been received. Send object request"); acknowledgement = true; return payload;
           default: System.out.println("Default case"); return payload;
        }
        //return payload;
    }
}
