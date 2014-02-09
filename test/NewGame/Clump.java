package NewGame;


import NewGame.Utils.PerlinNoise;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
public class Clump extends Node
{
    public WorldBlock[][][] clump = new WorldBlock[CLUMP_DIMENSION_X][CLUMP_DIMENSION_Y][CLUMP_DIMENSION_Z];
    public static final int CLUMP_DIMENSION_X = 10;
    public static final int CLUMP_DIMENSION_Y = 10;
    public static final int CLUMP_DIMENSION_Z = 10;
    
    private boolean loaded = false;
    
    public Clump(boolean loadClump)
    {
        if(loadClump)
        {
            loadClump();
        }
        
    }
    
    public Clump(WorldBlock block)
    {
        loadClump(block);
    }
    
    public final void loadClump(WorldBlock block)
    {
        loaded = true;
        for(int i = Clump.CLUMP_DIMENSION_X-1; i >= 0; i--)
        {
            for(int j = Clump.CLUMP_DIMENSION_Y-1; j >= 0; j--)
            {
                for(int k = Clump.CLUMP_DIMENSION_Z-1; k >= 0; k--)
                {
                        clump[i][j][k] = (WorldBlock)block.clone();
                        clump[i][j][k].setLocalTranslation(i, j, k);
                        this.attachChild(clump[i][j][k]); 
                        checkFaces(i, j, k, true);
                }
            }
        }
    }
    
    public final void loadClump()
    {
        loaded = true;
        for(int i = CLUMP_DIMENSION_X-1; i >= 0; i--)
        {
            for(int j = CLUMP_DIMENSION_Y-1; j >= 0; j--)
            {
                for(int k = CLUMP_DIMENSION_Z-1; k >= 0; k--)
                {
                    int blockType = 1;
                    
                    clump[i][j][k] = new WorldBlock((byte)i, (byte)j, (byte)k, blockType);
                    this.attachChild(clump[i][j][k]); 
                    checkFaces(i, j, k, true);  
                }
            }
        }
    }
    
    
    public void checkFaces(int x, int y, int z, boolean cull)
    {
        Vector3f pos = getWorldTranslation();
        if(x != CLUMP_DIMENSION_X-1)
        {
            if(clump[x+1][y][z] != null)
            {
                clump[x][y][z].cullFace(3, cull);
                
                clump[x+1][y][z].cullFace(2, cull);
            }
        }
        
        /*
        else
        {
            if(BlockBuildingTest.world[(int)pos.x+1][(int)pos.y][(int)pos.z] != null)
            {
                if(BlockBuildingTest.world[(int)pos.x+1][(int)pos.y][(int)pos.z].clump[0][y][z] != null)
                {
                    BlockBuildingTest.world[(int)pos.x+1][(int)pos.y][(int)pos.z].clump[0][y][z].cullFace(2, cull);
                    clump[x][y][z].cullFace(3, cull);
                }
            }
        }
         * 
         */
        
        if(x != 0)
        {
            if(clump[x-1][y][z] != null)
            {
                clump[x][y][z].cullFace(2, cull);
                clump[x-1][y][z].cullFace(3, cull);
            }
        }
        /*
        else
        {
            if(pos.x > 0)
            {
                if(BlockBuildingTest.world[(int)pos.x-1][(int)pos.y][(int)pos.z] != null)
                {
                    if(BlockBuildingTest.world[(int)pos.x-1][(int)pos.y][(int)pos.z].clump[9][y][z] != null)
                    {
                        BlockBuildingTest.world[(int)pos.x-1][(int)pos.y][(int)pos.z].clump[9][y][z].cullFace(3, cull);
                        clump[x][y][z].cullFace(2, cull);
                    }
                }
            }
            
        }
         * 
         */
        if(y != CLUMP_DIMENSION_Y-1)
        {
            if(clump[x][y+1][z] != null)
            {
                clump[x][y][z].cullFace(5, cull);
                /*
                if(clump[x][y][z].blockType == 1)
                {
                    clump[x][y][z].setFaceMat(2);
                    clump[x][y][z].blockType = 2;
                }
                 * 
                 */
                clump[x][y+1][z].cullFace(4, cull);
            }
        }
        /*
        else
        {
            if(BlockBuildingTest.world[(int)pos.x][(int)pos.y+1][(int)pos.z] != null)
            {
                if(BlockBuildingTest.world[(int)pos.x][(int)pos.y+1][(int)pos.z].clump[x][0][z] != null)
                {
                    BlockBuildingTest.world[(int)pos.x][(int)pos.y+1][(int)pos.z].clump[x][0][z].cullFace(4, cull);
                    clump[x][y][z].cullFace(5, cull);
                }
            }
        }
         * 
         */
        if(y != 0)
        {
            if(clump[x][y-1][z] != null)
            {
                clump[x][y][z].cullFace(4, cull);
                clump[x][y-1][z].cullFace(5, cull);
            }
        }
        /*
        else
        {
            if(pos.y > 0)
            {
                if(BlockBuildingTest.world[(int)pos.x][(int)pos.y-1][(int)pos.z] != null)
                {
                    if(BlockBuildingTest.world[(int)pos.x][(int)pos.y-1][(int)pos.z].clump[x][9][z] != null)
                    {
                        BlockBuildingTest.world[(int)pos.x][(int)pos.y-1][(int)pos.z].clump[x][9][z].cullFace(5, cull);
                        clump[x][y][z].cullFace(4, cull);
                    }
                }
            }
            
        }
         * 
         */
        if(z != CLUMP_DIMENSION_Z-1)
        {
            if(clump[x][y][z+1] != null)
            {
                clump[x][y][z].cullFace(0, cull);
                clump[x][y][z+1].cullFace(1, cull);
            }
        
            /*
        else
        {
            if(BlockBuildingTest.world[(int)pos.x][(int)pos.y][(int)pos.z+1] != null)
            {
                if(BlockBuildingTest.world[(int)pos.x][(int)pos.y][(int)pos.z+1].clump[x][y][0] != null)
                {
                    BlockBuildingTest.world[(int)pos.x][(int)pos.y][(int)pos.z+1].clump[x][y][0].cullFace(1, cull);
                    clump[x][y][z].cullFace(0, cull);
                }
            }
        }
             * 
             */
        if(z != 0)
        {
            if(clump[x][y][z-1] != null)
            {
                clump[x][y][z].cullFace(1, cull);
                clump[x][y][z-1].cullFace(0, cull);
            }
        }
        /*
        else
        {
            if(pos.z > 0)
            {
                if(BlockBuildingTest.world[(int)pos.x][(int)pos.y][(int)pos.z-1] != null)
                {
                    if(BlockBuildingTest.world[(int)pos.x][(int)pos.y][(int)pos.z-1].clump[x][y][9] != null)
                    {
                        BlockBuildingTest.world[(int)pos.x][(int)pos.y][(int)pos.z-1].clump[x][y][9].cullFace(0, cull);
                        clump[x][y][z].cullFace(1, cull);
                    }
                }
          }
         * 
         */
            
        }
    }
    
    @Override
    public String toString()
    {
        return "X: " + (int)getWorldTranslation().x + " Y: " + (int)getWorldTranslation().y + " Z: " + (int)getWorldTranslation().z; 
    }
    
    //public Clump copy()
    {
        
    }
}
