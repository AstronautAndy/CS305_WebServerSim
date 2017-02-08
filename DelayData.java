
/**
 * Write a description of class DelayData here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DelayData
{
    static int PropagationDelay;
    static int transmissionDelay;
    
    /**
     * Constructor for objects of class DelayData
     */
    public DelayData()
    {
        // initialise instance variables
        
    }
    
    public static void setPropagationDelay(int d){
        PropagationDelay = d;
        //System.out.println("Propagation Delay has been set");
    }
    
    public static void setTransmissionDelay(int d){
        transmissionDelay = d;
        //System.out.println("Transmission Delay has been set");
    }

}
