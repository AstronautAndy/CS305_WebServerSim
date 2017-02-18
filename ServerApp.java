import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;

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
        byte ack = 1; //Represents a simple TCP acknowledgement that the server has received an open request from the client
        byte[] ackMessage = {ack}; //A simple, one element array of bytes
        byte found = 3;
        byte[] foundMessage = {found};
        byte notFound = 4;
        byte[] notFoundMessage = {notFound};
        
        while( true )
        {
            //receive message from client, and send the "received" message back.
            byte[] byteArray = transportLayer.receive();

            //if client disconnected
            if(byteArray==null)
                break;
            
            if(byteArray[0] == 0){
                System.out.println("processed open connection request: sending acknowledgement"); 
                transportLayer.send(ackMessage);
            }
            else if(byteArray[0] == 2){ //Server needs to obtain the rest of the byte array after the header
                byteArray = obtainMessage(byteArray); //Only save the URL request part of the message
                String url = new String(byteArray); //Save the url for the object we want to search for
                
                try{
                    BufferedReader in = new BufferedReader(new FileReader(url));
                    Scanner scanner = new Scanner(in);
                    while(scanner.hasNextLine() ){ //Send lines of text over the connection
                        String textLine = scanner.nextLine();
                        byteArray = textLine.getBytes();
                        byteArray = concatenate(foundMessage, byteArray);
                        transportLayer.send(byteArray);
                    }
                }catch(FileNotFoundException ex){
                    //System.out.println("Exception has been caught");
                    transportLayer.send(notFoundMessage);//Send 404 message to client
                    ex.printStackTrace();
                }
            }
            //Removed the default code because it isn't necessary
            //String str = new String ( byteArray );
            //System.out.println( str );
            //String line = "received"; //Debating whether I need to keep this
            //byteArray = line.getBytes();
            //transportLayer.send( byteArray );
            
        }
    }
    
    /**
     * Method used to concatenate the object request header to the rest of the object request message 
     * (header will always go first)
     * In the case of this method, byte[] a is the header, and b is the rest of the message
     */
    static byte[] concatenate(byte[] a, byte[] b){
        byte[] newByte;
        newByte = new byte[a.length + b.length];
        System.arraycopy(a, 0, newByte,0,a.length);//Copy the header into the new byteArray
        System.arraycopy(b,0, newByte,a.length,b.length);
        return newByte;
    }
    
    /**
     * A method used to obtain the data in an incoming message that come after the message 
     */
    static byte[] obtainMessage(byte[] input){
        byte[] message = new byte[input.length-1];
        System.arraycopy(input,1,message,0,input.length-1);
        return message;
    }
    
    //Server app now needs some sort of simple method to "read" the "html" file
    
}
