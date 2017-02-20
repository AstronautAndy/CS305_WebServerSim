
/**
 * The transport layer needs to implement a 3-way handshake
 */
public class TransportLayer
{
    byte syn = 0;
    byte ack = 1; //Represents a simple TCP acknowledgement that the server has received an open request from the client
    byte objReq = 2;
    byte[] synMessage = {syn};
    byte[] ackMessage = {ack}; //A simple, one element array of bytes
    byte[] objMessage = {objReq};
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
        System.out.println("Client is requesting opening.");
        send(synMessage);
        byte[] response = receive();
        if(response[0] == 1){
            System.out.println("Connection request has been approved by the server. Send Object request now.");
            return true;
        }
        else{
            System.out.println("Connection request has been denied by the server.");
            return false;
        }
        
    }
    
    /**
     * Basic function used to send messages between Client and Server
     */
    public void send(byte[] payload)
    {
        networkLayer.send( payload );
    }

    public byte[] receive()
    {
        byte[] payload = networkLayer.receive(); 
        return payload;
    }
}
