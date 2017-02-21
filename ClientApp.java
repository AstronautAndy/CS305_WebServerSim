
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.io.FileReader;
import java.util.*;
import java.lang.*;

//This class represents the client application
/**
 * The client needs to be able to print out the webpage being requested. This means that the client will send 
 * URLs to the server for the server to retrieve.
 */
public class ClientApp
{
    static TransportLayer transportLayer = new TransportLayer(false);
    static byte objReq = 2;
    static byte[] objMessage = {objReq};
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
        boolean usesCache = false;
		boolean inCache = false;
        /*
        byte objReq = 2;
        byte[] objMessage = {objReq};
        */
        DelayData.setPropagationDelay( Integer.parseInt(args[0]) ); 
        DelayData.setTransmissionDelay( Integer.parseInt(args[1]) );
        
        HashMap cache = new HashMap();
        
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        int code = 0; //Change the code upon reading certain signals from the server
        
        if(args[2].equals("1") ){
          System.out.println("Now opening a new persistent connection");
          persistent = true;  
          transportLayer.requestOpening(); //Request an open connection with the server.
        } else{
            System.out.println("Now opening a new non-persistent connection");
            persistent = false;
        }
        
        if(args[3].equals("1")){//Handle the condition determining whether the sim will use a cache or not
            usesCache = true;
        }
        else{
            usesCache = false;
        }
        
        //while line is not empty
        while( line != null && !line.equals("") )
        {
            //convert lines into byte array, send to transport layer and wait for response
            byte[] byteArray = line.getBytes();
            startTime = System.currentTimeMillis();
            if(usesCache == true){ //check if the cache has the value you're loooking for
                if(cache.containsKey(line) ){ //if so, obtain the value from the cache rather than from the server
                    System.out.println("URL found in Cache");
                    byteArray = (byte[]) cache.get(line);
					inCache = true;
                }
                else{//Otherwise, perform the standard object request procedure
                        System.out.println("Url not found in Cache");
						inCache = false;
                        if(persistent == true){ //Send messages without having to request a new conncetion with the client each time
                            byte[] sendMessage = concatenate(objMessage, byteArray); //Concatenate object request message with the appropriate header
                            transportLayer.send( sendMessage );
                        }
                        else{ //If the client is non-persistent, we want it to send an open connection request each time it requests an object
                            if(transportLayer.requestOpening() == true){
                                byte[] sendMessage = concatenate(objMessage, byteArray); //Concatenate object request message with the appropriate header
                                transportLayer.send( sendMessage );
                            }
                            else{
                                System.out.println("Connection with Server has been refused.");
                            }
                        }
                        byteArray = transportLayer.receive();
                }
           }
           else{ //If the program does not use a cache, send a URL request as default
                   System.out.println("Beginning standard procedure (Non-cache)");
                   if(persistent == true){ //Send messages without having to request a new conncetion with the client each time
                        byte[] sendMessage = concatenate(objMessage, byteArray); //Concatenate object request message with the appropriate header
                        transportLayer.send( sendMessage );
                    }
                   else{ //If the client is non-persistent, we want it to send an open connection request each time it requests an object
                        if(transportLayer.requestOpening() == true){
                            byte[] sendMessage = concatenate(objMessage, byteArray); //Concatenate object request message with the appropriate header
                            transportLayer.send( sendMessage );
                        }
                        else{
                            System.out.println("Connection with Server has been refused.");
                        }
                    }
                   byteArray = transportLayer.receive();
           }
            //byteArray = transportLayer.receive();
            endTime = System.currentTimeMillis();
            String str = obtainMessage( byteArray );
			
			if (inCache == true) {
				BufferedReader in = new BufferedReader(new FileReader(line));
            	Scanner scanner = new Scanner(in);
            	while(scanner.hasNextLine() ){ //Send lines of text over the connection
            		String textLine = scanner.nextLine();
					System.out.println(textLine);
					System.out.println(str);
					if (textLine.equals(str)) {
						byteArray[0] = 5;
					} else {
						str = textLine;
						byteArray[0] = 3;
					}
            	}
            }

			// Request Message:
			System.out.println("");
			if (persistent == false) {
				// Non-per
				System.out.print("HTTP/1.0 ");
			} else {
				// Pers
				System.out.print("HTTP/1.1 ");
			}
			printError(byteArray[0]);
			Timestamp modified = new Timestamp(System.currentTimeMillis());
			System.out.println("Date: " + modified);
			Timestamp startStamp = new Timestamp(startTime);
			System.out.println("Last-Modified: " + startStamp);
            if(byteArray[0] == 3){//If the received value is an object, add it to the map
                cache.put(line,byteArray);
            }
            RenderHTML(str); //Render the HTML code that we received from the server
            timeToReceive = endTime - startTime;
			System.out.println("");
            System.out.println("Runtime: " + timeToReceive + " ms");
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
    
    /**
     * A method used to obtain the data in an incoming message that come after the message 
     */
    static String obtainMessage(byte[] input){
        byte[] message = new byte[input.length-1];
        System.arraycopy(input,1,message,0,input.length-1);
        String sMessage = new String(message);
        return sMessage;
    }
    
    /**
     * Added this method to clean up the code a little bit
     */
    static void printError(byte code){
        if(code == 3){
                System.out.println("200 OK");
        } else if(code == 4){
                System.out.println("404 Not Found");
        } else if(code == 5) {
				System.out.println("304 Not Modified");
        }
        return;
    }
    
    /**
     * This method needs to retrieve a nested txt file in addition to printing the text found
     */
    static void RenderHTML(String htmlCode){
        Scanner htmlScanner = new Scanner(htmlCode);
        String token;
        byte[] NewRequest;
        while( htmlScanner.hasNext() ){
            token = htmlScanner.next();
            if(token.equals("<br>") ){
                System.out.println();
            }
            else if(token.equals("#")){ //Handle nested value
                token = htmlScanner.next(); //Token is now the URL of the embedded file we want to access
                try{
                    BufferedReader in = new BufferedReader(new FileReader(token));
                    Scanner scanner = new Scanner(in);
                    String lineFound = scanner.nextLine();
                    System.out.println(lineFound);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            else{
                System.out.print( token + " " );
            }
        }
        System.out.println();
    }
}
