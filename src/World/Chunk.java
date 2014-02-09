
package World;

import Game.Game;
import Utils.Vector2i;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * A chunk is a node that makes up the world
 * @author Lucas's Computer
 */
public class Chunk extends Node
{
    /*
     * 3d array of blocks
     * Makes up 1 Chunk
     */
    private Geometry[][][] blockArray = new Geometry[16][256][16];
    //private List<Geometry> blocks = new ArrayList<Geometry>();
    
    private List<Spatial> currentGeoms = new ArrayList<Spatial>();
    private List<Spatial> lastGeoms = new ArrayList<Spatial>();
    
    private int xDisp;
    private int zDisp;
    
    
    
    /**
     * Creates a Chunk: 16 x 16 x 128 blocks
     * @param location
     */
    public Chunk(Vector2i location)
    {
            loadBlocks(location);
    }
    
    /**
     * Loads the blocks into the chunk
     * @todo Create algoritm for terrain creation
     * @param globalPos
     */
    private void loadBlocks(Vector2i globalPos)
    {
        xDisp =  (globalPos.x)*blockArray.length-3200;
        zDisp =  (globalPos.z)*blockArray[0][0].length-3200;
        for(int i = blockArray.length-1; i >= 0; i--)
        { 
            for(int k = blockArray[0][0].length - 1; k >= 0; k--)
            {
                boolean maxSet = false;
                for(int j = blockArray[0].length - 1; j >= 0; j--)
                {
                    if(World.perlinNoise[i+(globalPos.x)*blockArray.length-3200][k+(globalPos.z)*blockArray[0][0].length-3200]*205+50 > j && !maxSet) //World.perlinNoise[i+(globalPos.x)*blockArray.length-3200][k+(globalPos.z)*blockArray[0][0].length-3200]*205+50 > j && !maxSet
                    {
                        maxSet = true;
                        Block b = new Block((int)(Math.random()*4) +1);                   
                        b.setLocalTranslation(i, j, k);
                        blockArray[i][j][k] = b;  
                       
                    }
                    else if(maxSet && needsToBeDrawn(World.perlinNoise, i, k, j))
                    {
                            Block b = new Block((int)(Math.random()*4) +1);                   
                            b.setLocalTranslation(i, j, k);
                            blockArray[i][j][k] = b; 
                    }
                }
            }
        }
        //cullHiddenBlocks();
        combineMeshes();
    }
    
    /**
     * Combines meshes to create a chunk
     */
    public void combineMeshes()
    {
        List<Geometry> geoms = GeometryBatchFactory.makeBatches(getAllBlocks());
        for(Geometry g: geoms)
        {
            currentGeoms.add(g);
            attachChild(g);
        }
    }
    
    /**
     * Gets a block within the chunk
     * @param x
     * @param y
     * @param z
     * @return Block
     */
    public Block getBlock(int x, int y, int z)
    {
        return (Block)blockArray[x][y][z];
        
    }
    
    /**
     * Removes a block, then updates the mesh
     * @param b 
     */
    public void updateMeshRemove(Block b) // remove
    {
        Vector3f pos = b.getLocalTranslation();
        blockArray[(int)(pos.x+.5)][(int)(pos.y+.5)][(int)(pos.z+.5)] = null;
        
        lastGeoms.clear();
        lastGeoms.addAll(currentGeoms);
        currentGeoms.clear();
        List<Geometry> geoms = GeometryBatchFactory.makeBatches(getAllBlocks());
        
        for(Geometry g: geoms)
        {
            currentGeoms.add(g);
            attachChild(g);
        }
        for(Spatial s: lastGeoms)
        {
            this.detachChild(s);
        }
        System.out.println("optimized");
    }
    
    /**
     * Adds a block, then updates the mesh
     * @param b 
     */
    public void updateMeshAdd(int x, int y, int z) // add
    {
        Block b = new Block(2);
        b.setLocalTranslation(x, y, z);
        blockArray[x][y][z] = b;
        
        lastGeoms.clear();
        lastGeoms.addAll(currentGeoms);
        currentGeoms.clear();
        List<Geometry> geoms = GeometryBatchFactory.makeBatches(getAllBlocks());
        
        for(Geometry g: geoms)
        {
            currentGeoms.add(g);
            attachChild(g);
        }
        for(Spatial s: lastGeoms)
        {
            this.detachChild(s);
        }
        System.out.println("optimized");
    }
    
    /**
     * Overrides the toString method
     * @return String
     */
    public String toString()
    {
        return "Not Null";
    }
    
    /**
     * Adds blocks from the blockArray into an ArrayList of geometries
     * @return ArrayList of geometries
     */
    public ArrayList<Geometry> getAllBlocks()
    {
        ArrayList<Geometry> l = new ArrayList<Geometry>();
        for(int i = 0; i < blockArray.length; i++)
        {
            for(int j = 0; j < blockArray[0].length; j++)
            {
                for(int k = 0; k < blockArray[0][0].length; k++)
                {
                    if(blockArray[i][j][k] != null)
                        l.add(blockArray[i][j][k]);
                }
            }
        }
        return l;
    }
    
    /**
     * Gets height
     * @param x
     * @param z
     * @return int height
     */
    public int getHeight(int x, int z)
    {
        for(int i = blockArray[0].length-1; i >= 0; i--)
        {
            if(blockArray[x][i][z] != null)
                return i;
        }
        return -1;
    }
    
    /**
     * Gets the maximum block from blockArray
     * @param x
     * @param z
     * @return Block
     */
    public Block getMaxBlock(int x, int z)
    {
        for(int i = blockArray[0].length-1; i >= 0; i--)
        {
            if(blockArray[x][i][z] != null)
                return (Block)blockArray[x][i][z];
        }
        return null;
    }
    
    /**
     * Culls all blocks that are not visable to the user
     */
    public void cullHiddenBlocks()
    {
        for(int i = 1; i < blockArray.length-1; i++)
        {
            for(int j = 1; j < blockArray[0].length-1; j++)
            {
                for(int k = 1; k < blockArray[0][0].length-1; k++)
                {
                    if(hasAllNeighbors(i, j, k))
                        getBlock(i, j, k).setCullHint(CullHint.Always);
                }
            }
        }
    }
    
    /**
     * Returns whether the block is surrounded by blocks
     * @param x
     * @param y
     * @param z
     * @return true if the block is completly surrounded by blocks
     */
    public boolean hasAllNeighbors(int x, int y, int z)
    {
        try
        {
            return getBlock(x+1, y, z) != null && 
               getBlock(x-1, y, z) != null &&
               getBlock(x, y+1, z) != null &&
               getBlock(x, y, z+1) != null &&
               getBlock(x, y, z-1) != null;
        }
        catch(IndexOutOfBoundsException ed){}
        return false;
        
    }
    
    /**
     * Returns whether the block needs to be drawn or not
     * @param noise
     * @param x
     * @param z
     * @param y
     * @return true if block is visable to user and needs to be drawn
     */
    public boolean needsToBeDrawn(float[][] noise, int x, int z, int y)
    {
        try
        {
            return Math.floor(noise[x+xDisp+1][z+zDisp]*205+50) <  y ||
               Math.floor(noise[x+xDisp-1][z+zDisp]*205+50) <  y ||
               Math.floor(noise[x+xDisp][z+zDisp+1]*205+50) <  y ||
               Math.floor(noise[x+xDisp][z+zDisp-1]*205+50) <  y;
        }
        catch(IndexOutOfBoundsException ex){}
        return false;
    }
    
}
