
package World;

import Game.Materials;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Block is a geometry that makes up a chunk
 * @author Lucas's Computer
 */
public class Block extends Geometry
{
    /*
     * Block id Number
     */
    private int id;
    
    public boolean hasBounding;
    
    /**
     * Creates a Block with given id and position
     * @param id 
     * @param center 
     */
    public Block(int id)
    {
        this.id = id;
        setMesh(new Box(.5f, .5f, .5f));
        setMaterial(Materials.MATERIALS[id]);
    }
    
    /**
     * Returns Block's Id
     * @return Block id 
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * Overrides the equals method
     * @param other
     * @return true if blocks are equal
     */
    public boolean equals(Object other)
    {
        return getLocalTranslation().equals(((Block)other).getLocalTranslation());
    }
    
    /**
     * Overrides the toString method
     * @return String
     */
    public String toString()
    {
        return "Not Null(Block)";
    }
}
