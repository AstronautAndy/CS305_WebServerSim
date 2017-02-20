
public class NetworkLayer
{

    private LinkLayer linkLayer;

    public NetworkLayer(boolean server)
    {
        linkLayer = new LinkLayer(server);

    }
    public void send(byte[] payload)
    {
        //Enable delay period
        int tDelay = (payload.length*8) / DelayData.transmissionDelay;
        //System.out.println("Now sending payload with header: " + payload[0]); //Uncomment this if it makes it easier to understand what's going on
        try{
            Thread.sleep(DelayData.PropagationDelay); 
            Thread.sleep(tDelay);
        }catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        linkLayer.send( payload );
    }

    public byte[] receive()
    {
        byte[] payload = linkLayer.receive();
        return payload;
    }
}
