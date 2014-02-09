
package World;

import PerlinNoise.PerlinNoise;
import Utils.Vector2i;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 * A world is a node that represents the visable world seen in game
 * @author Lucas's Computer
 * 
 */
public class World extends Node
{
    /**
     * Enum for world size
     */
    public static enum Size
    {
        LARGE,
        MEDIUM,
        SMALL
    }
    /**
     * Size of large world
     */
    public static int[] largeWorld = {400, 1, 400};
    
    /**
     * Size of medium world
     */
    public static int[] mediumWorld = {200, 1, 200};
    
    /**
     * Size of small world
     */
    public static int[] smallWorld = {50, 1, 50};
    
    /**
     * Size of this world
     */
    private Size worldSize;
    
    /**
     * 2d array of Chunks
     * Holds world
     */
    public Chunk[][] world;
    
    
    /**
     * Perlin Noise for Map
     */
    public static float[][] perlinNoise;
    

   
    /**
     * Creates a new world of Enum Size
     * @param worldSize 
     */
    public World(Size worldSize)
    {
        this.worldSize = worldSize;
        switch(worldSize)
        {
            case LARGE:
                world = new Chunk[largeWorld[0]][largeWorld[2]];
                break;
                
            case MEDIUM:
                world = new Chunk[mediumWorld[0]][mediumWorld[2]];
                break;
                
            case SMALL:
                world = new Chunk[smallWorld[0]][smallWorld[2]];
                break; 
        }
        
        perlinNoise = PerlinNoise.generatePerlinNoise(PerlinNoise.generateWhiteNoise(), 8);
    }
    
    /**
     * Adds a chunk to the world
     * @param x
     * @param y
     * @param z 
     */
    public void addChunk(int x, int z)
    {      
        Chunk c = new Chunk(new Vector2i(x, z));
        c.setLocalTranslation(x*16, .5f, z*16);
        attachChild(c);
        world[x][z] = c;
    }
    
    /**
     * Get world dimentions
     * @return int[] world dimentions
     */
    public int[] getWorldBounds()
    {
        switch(worldSize)
        {
            case LARGE:
                return largeWorld;
                
            case MEDIUM:
                return mediumWorld;
                
            case SMALL:
                return smallWorld;
            
            default:
                return smallWorld;
        }
    }
    
    /**
     * Get the specific chunk based on world coordinates
     * @param int x, int z
     * @return chunk at x, z
     */
    public Chunk getChunk(int x, int z)
    {
       return world[(int)(x/16.0)][(int)(z/16.0)];
    }
    
    /**
     * Get the specific bloack based on world coordinates
     * @param int x, int y, int z
     * @return specific block based on world coordinates
     */
    public Block getBlock(int globalX, int globalY, int globalZ)
    {
        return world[globalX/16][globalZ/16].getBlock(globalX%16, globalY, globalZ%16);       
    }
    
    /**
     * Chooses the block corresponding to location of mouse click
     * @param globalX
     * @param globalY
     * @param globalZ
     * @param direction
     * @return Block that is clicked
     */
    public Block chooseClickedBlock(int globalX, int globalY, int globalZ, Vector3f direction)
    {
        Chunk c = getChunk(globalX, globalZ);
        Vector3f local = globalToLocal(globalX, globalY-1, globalZ);
        System.out.println(local);
        try
        {
            if(direction.x == 1)
            {
                return c.getBlock((int)local.x, (int)local.y, (int)local.z);
            }
            if(direction.x == -1)
            {
                return c.getBlock((int)local.x+1, (int)local.y, (int)local.z);
            }
            if(direction.y == 1)
            {
                return c.getBlock((int)local.x, (int)local.y, (int)local.z);
            }
            if(direction.y == -1)
            {
                return c.getBlock((int)local.x, (int)local.y+1, (int)local.z);
            }
            if(direction.z == 1)
            {
                return c.getBlock((int)local.x, (int)local.y, (int)local.z);
            }
            if(direction.z == -1)
            {
                return c.getBlock((int)local.x, (int)local.y, (int)local.z+1);
            }
        }
        catch(IndexOutOfBoundsException ex){}
        
        
        return null;
        
        
    }
    
    /**
     * Converts x,y,z coordinates to a local Vector3f object
     * @param x
     * @param y
     * @param z
     * @return Vector3f
     */
    public Vector3f globalToLocal(int x, int y, int z)
    {
        return new Vector3f((x)%16, y, (z)%16);
    }
    
    /**
     * Converts x,y,z coordinates to a local Vector2i object
     * @param x
     * @param y
     * @param z
     * @return Vector2i
     */
    public Vector2i globalToLocal(int x, int z)
    {
        return new Vector2i((x)%16, (z)%16);
    }
    
    /**
     * Gets a list of blocks within range of user
     * @param globalX
     * @param globalZ
     * @return ArrayList of blocks within range
     */
    public ArrayList<Block> getInRangeBlocks(int globalX, int globalZ)
    {
        Vector2i local = globalToLocal(globalX, globalZ);
        int x = local.x;
        int z = local.z;
        
        Chunk c = getChunk(globalX, globalZ);
        ArrayList<Block> b = new ArrayList<Block>();
        
        if(x != 0 && x != 15)
        {
            if(z != 0 && z != 15)
            {
                    for(int i = x-1; i <= x+1; i++)
                    {
                        for(int j = z-1; j <= z+1; j++)
                        { 
                                b.add(c.getMaxBlock(i, j));
                        }
                    }
            }
            else if(z == 15)
            {
                
                    for(int i = x-1; i <= x+1; i++)
                    {
                        for(int j = z-1; j <= z; j++)
                        {   
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                    c = getChunk(x, z+1);
                    for(int i = x-1; i <= x+1; i++)
                    {
                        for(int j = 0; j <= 0; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                
            }
            else if(z == 0)
            {
                
                    for(int i = x-1; i <= x+1; i++)
                    {
                        for(int j = z; j <= z+1; j++)
                        {   
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                    c = getChunk(x, z-1);
                    for(int i = x-1; i <= x+1; i++)
                    {
                        for(int j = 15; j <= 15; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                
            }
            
        }
        else if(x == 15)
        {
            if(z != 0 && z != 15)
            {
                    for(int i = x-1; i <= x; i++)
                    {
                        for(int j = z-1; j <= z+1; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                    c = getChunk(x+1, z);
                    for(int i = 0; i <= 0; i++)
                    {
                        for(int j = z-1; j <= z+1; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
            }
            else if(z == 15)
            {
                    for(int i = x-1; i <= x; i++)
                    {
                        for(int j = z-1; j <= z; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                    c = getChunk(x, z+1);
                    for(int i = x-1; i <= x; i++)
                    {
                        for(int j = 0; j <= 0; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                    c = getChunk(x+1, z);
                    for(int i = 0; i <= 0; i++)
                    {
                        for(int j = z-1; j <= z; j++)
                        {
                            b.add(c.getMaxBlock(i, j));
                        }
                    }
                    c = getChunk(x+1, z+1);
                    b.add(c.getMaxBlock(0, 0));
            }
        }
        return b;
    }
}
