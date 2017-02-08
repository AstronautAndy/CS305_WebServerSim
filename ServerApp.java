import java.io.BufferedReader;
import java.io.InputStreamReader;

//This class represents the server application
public class ServerApp
{
    /**
     * args[0] will be used for the Propagation delay, and args[1] will be used for the transmission delay
     */
    public static void main(String[] args) throws Exception
    {
        //create a new transport layer for server (hence true) (wait for client)
        int delay = 0;
        DelayData.setPropagationDelay( Integer.parseInt(args[0]) ); 
        DelayData.setTransmissionDelay( Integer.parseInt(args[1]) );
        TransportLayer transportLayer = new TransportLayer(true, delay);
        while( true )
        {
            //receive message from client, and send the "received" message back.
            byte[] byteArray = transportLayer.receive();
            
            //if client disconnected
            if(byteArray==null)
                break;
            String str = new String ( byteArray );
            System.out.println( str );
            String line = "received";
            byteArray = line.getBytes();
            transportLayer.send( byteArray );

        }
    }
}
