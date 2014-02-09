package NewGame;


import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
public class WorldBlock extends Node
{
    byte x;
    byte y;
    byte z;
    int blockType;
    
    public WorldBlock(byte x, byte y, byte z, int blockType)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        setLocalTranslation(x, y, z);
        addFaces(blockType);
        this.blockType = blockType;
    }
    
    public WorldBlock()
    {
        x = 0;
        y = 0;
        z = 0;
        addFaces(1);
        this.blockType = 1;
    }
    
    public final void addFaces(int blockType)
    {
        for(int i = 0; i < 6; i++)
        {
            Quad q = new Quad(1f, 1f);
            Geometry g = new Geometry("quad", q);
            g.setMaterial(MaterialIndex.getMaterial(blockType, i));
            attachChild(g);
            setFace(i, g);
            
        }
    }
    
    public void setFaceMat(int blockType)
    {
        int i = 0;
        for(Spatial g: this.getChildren())
        {
            g.setMaterial(MaterialIndex.getMaterial(blockType, i));
            i++;
        }
    }
    
    public void cullFace(int i, boolean cull)
    {
        if(cull)
            getChild(i).setCullHint(CullHint.Always);
        else
        {
            getChild(i).setCullHint(CullHint.Dynamic);
            //System.out.println("Culled");
        }
    }
    
    public static void setFace(int faceNum, Geometry g)
    {
        switch(faceNum)
        {
            case 0: //front
                g.setLocalTranslation(0, 0, .5f);
                break;
                
            case 1: //back
                g.rotate(0, (float)Math.PI, 0);
                g.setLocalTranslation(1f, 0, -.5f);
                break;
                
             case 2: //left
                g.rotate(0, -(float)Math.PI/2, 0);
                g.setLocalTranslation(0, 0, -.5f);
                break;
             
             case 3: //right
                g.rotate(0, (float)Math.PI/2, 0);
                g.setLocalTranslation(1f, 0, .5f);
                break;
                
             case 4: //bottom
                g.setLocalTranslation(0, 0, -.5f);
                g.rotate((float)Math.PI/2, 0, 0);
                break;
                 
              case 5: //top
                g.rotate(-(float)Math.PI/2, 0, 0);
                g.setLocalTranslation(0, 1f, .5f);
                
                break; 
                
                
        }
    }
    
    @Override
    public String toString()
    {
        return "X: " + x + " Y: " + y + " Z: " + z; 
    }
}
