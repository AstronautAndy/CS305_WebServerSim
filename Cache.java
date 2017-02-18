import java.util.*;
/**
 * Write a description of class Cache here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cache
{
    public static HashMap cache = new HashMap(); //The URL is the key and the string of HTML text as the value

    /**
     * Constructor for objects of class Cache
     */
    public Cache()
    {
        
    }
    
    /**
     * This method should be static so that the transportLayer will be able to add URLS and their 
     * respective data to the HashMap without having to instantiate a Cache within the TransportLayer
     */
    static void addToCache(String url, String value){
        cache.put(url, value);
    }
    
    
}
