import java.io.BufferedReader;
import java.io.InputStreamReader;

//This class represents the server application
public class ServerApp
{
    /**
     * The standard for inputs will be as follows:
     * args[0] = Propagation Delay
     * args[1] = Transmission Delay
     * args[2] = Version of HTTP used 
     * args[3] = URL to the web page we want to access
     */
    public static void main(String[] args) throws Exception
    {
        //create a new transport layer for server (hence true) (wait for client)
        int delay = 0;
        DelayData.setPropagationDelay( Integer.parseInt(args[0]) ); 
        DelayData.setTransmissionDelay( Integer.parseInt(args[1]) );
        TransportLayer transportLayer = new TransportLayer(true);
        
        while( true )
        {
            //receive message from client, and send the "received" message back.
            byte[] byteArray = transportLayer.receive();

            //Place code here that does different things with the different typed of byte encodings received
            /*
            switch(byteArray[0]){ //USe a switch statement to handle each of the potential header values
                case 0: transportLayer.send(ackMessage);
            }
            */
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
