
package Utils;

/**
 *  This class represents a vector in the world
 * @author Lucas's Computer
 */
public class Vector2i 
{
    public final int x;
    public final int z;
    
    /**
     * Constructs a Vector2i
     * with x and y coordinates of 0
     */
    public Vector2i()
    {
        x = 0;
        z = 0;
    } 
    
    /**
     * Constructs a Vector2i
     * with x and y coordinates specified
     * @param x,y
     */
    public Vector2i(int x, int z)
    {
         this.x = x;
         this.z = z;
    }
    
    /**
     * Returns x
     * @return x
     */
    public int getX()
    {
        return x;
    }
    
    /**
     * Returns y
     * @return y
     */
    public int getY()
    {
        return z;
    }
}
