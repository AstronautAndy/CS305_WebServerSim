
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.*;

//This class represents the client application
/**
 * The client needs to be able to print out the webpage being requested. This means that the client will send 
 * URLs to the server for the server to retrieve.
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
        byte objReq = 2;
        byte[] objMessage = {objReq};
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
            if(transportLayer.requestOpening() == true){
                byte[] sendMessage = concatenate(objMessage, byteArray); //Concatenate object request message with the appropriate header
                transportLayer.send( sendMessage );
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
    
    /**
     * Method used to concatenate the object request header to the rest of the object request message 
     * (header will always go first)
     */
    static byte[] concatenate(byte[] a, byte[] b){
        byte[] newByte;
        newByte = new byte[a.length + b.length];
        System.arraycopy(a, 0, newByte,0,a.length);//Copy the header into the new byteArray
        System.arraycopy(b,0, newByte,a.length,b.length);
        return newByte;
    }
}
