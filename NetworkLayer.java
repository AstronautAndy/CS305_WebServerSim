
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
        int pDelay = payload.length *  DelayData.PropagationDelay; //P. delay needs to scale
        //System.out.println("Now sending payload with header: " + payload[0]); //Uncomment this if it makes it easier to understand what's going on
        try{
            Thread.sleep(pDelay); 
            Thread.sleep(DelayData.transmissionDelay);
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
