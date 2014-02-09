
package World;

import java.util.ArrayList;

import com.jme3.math.Vector3f;

/**
 * WorldUpdateQueue updates the world
 * @author Lucas's Computer
 */
public class WorldUpdateQueue
{
   
    public static volatile ArrayList<Vector3f> updateQueue = new ArrayList<Vector3f>();
    
    public static volatile boolean readyToAdd = true;
    
    
    /**
     * Adds an update request to the update queue
     * @param r 
     */
    public static void addUpdateRequest(Vector3f r)
    {
        updateQueue.add(r);
    }
}
