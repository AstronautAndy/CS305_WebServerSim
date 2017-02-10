
import java.io.BufferedReader;
import java.io.InputStreamReader;

//This class represents the client application
/**
 * The client needs to be able to print out the webpage being requested
 */
public class ClientApp
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
        //create a new transport layer for client (hence false) (connect to server), and read in first line from keyboard
        int delay = 0;
        DelayData.setPropagationDelay( Integer.parseInt(args[0]) ); 
        DelayData.setTransmissionDelay( Integer.parseInt(args[1]) );
        TransportLayer transportLayer = new TransportLayer(false);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        int code = 0; //Change the code upon reading certain signals from the server
        
        //while line is not empty
        while( line != null && !line.equals("") )
        {
            //convert lines into byte array, send to transport layer and wait for response
            byte[] byteArray = line.getBytes();
            transportLayer.send( byteArray );
            byteArray = transportLayer.receive();
            String str = new String ( byteArray );
            System.out.println( str );
            //read next line
            line = reader.readLine();
        }
    }
}
