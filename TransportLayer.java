
public class TransportLayer
{

    private NetworkLayer networkLayer;
    //server is true if the application is a server (should listen) or false if it is a client (should try and connect)
    public TransportLayer(boolean server, int delay) //Include a pointer to the delay from the Client/Server app
    {
        networkLayer = new NetworkLayer(server);
    }

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
