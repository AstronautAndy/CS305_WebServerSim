
/**
 * The transport layer needs to implement a 3-way handshake
 */
public class TransportLayer
{
    byte ack = 1; //Represents a simple TCP acknowledgement that the server has received an open request from the client
    byte[] ackMessage = {ack}; //A simple, one element array of bytes
    private NetworkLayer networkLayer;
    
    //server is true if the application is a server (should listen) or false if it is a client (should try and connect)
    public TransportLayer(boolean server) //Include a pointer to the delay from the Client/Server app
    {
        networkLayer = new NetworkLayer(server);
    }
    
    /**
     * Whenever the program sends a messages, it needs to send a TCP message, wait for
     * an acknowledgement, then send a request message.
     */
    public void send(byte[] payload)
    {
        //Enable delay period
        int pDelay = payload.length *  DelayData.PropagationDelay; //P. delay needs to scale
        //System.out.println(pDelay);
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
        switch(payload[0]){ //If the program is acting fishy, remove this block
           case 0: System.out.println("processed open connection request: sending acknowledgement"); send(ackMessage); // Send the acknowledgement message back to the requesting tLayer
           default: System.out.println("Default case"); return payload;
        }
        //return payload;
    }
}
