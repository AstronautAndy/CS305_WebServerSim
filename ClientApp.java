
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.*;

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
     * args[2] = binary value representing whether the client will be non-persistent or persistent  
     */
    public static void main(String[] args) throws Exception
    {
        //create a new transport layer for client (hence false) (connect to server), and read in first line from keyboard
        int delay = 0;
        long timeToReceive;
        long startTime;
        long endTime;
        boolean persistent = false; //Will be non-persistent by default
        DelayData.setPropagationDelay( Integer.parseInt(args[0]) ); 
        DelayData.setTransmissionDelay( Integer.parseInt(args[1]) );
        if(args[2] == "1"){
          persistent = true;  
        } else{
            persistent = false;
        }
        
        TransportLayer transportLayer = new TransportLayer(false);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        int code = 0; //Change the code upon reading certain signals from the server
        
        //while line is not empty
        while( line != null && !line.equals("") )
        {
            //convert lines into byte array, send to transport layer and wait for response
            byte[] byteArray = line.getBytes();
            startTime = System.currentTimeMillis();
            if(transportLayer.requestOpening() == true){ //Remove this block if it doesn't work
                transportLayer.send( byteArray ); //If the client is successfully able to make a connection w/ server
            }
            else{
                System.out.println("Connection with Server has been refused.");
            }
            
            byteArray = transportLayer.receive();
            endTime = System.currentTimeMillis();
            String str = new String ( byteArray );
            System.out.println( str );
            timeToReceive = endTime - startTime;
            System.out.println(timeToReceive + " ms");
            line = reader.readLine();
        }
    }
}
