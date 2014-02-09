/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NewGame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Lucas
 */
public class MaterialIndex 
{
    /*
     * Array of block materials
     */
    public static Material[] MATERIALS = new Material[10];
    
    /**
     * Loads block materials
     * @param assetManager 
     */
    public static void loadMaterials(AssetManager assetManager)
    {
        MATERIALS[1] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        MATERIALS[1].setColor("Color", ColorRGBA.Blue);
        
        MATERIALS[2] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        MATERIALS[2].setColor("Color", ColorRGBA.Red);

        //Dirt
        MATERIALS[3] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Materials/Dirt1.png");
        MATERIALS[3].setTexture("ColorMap", tex);
        
        MATERIALS[4] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex1 = assetManager.loadTexture("Materials/DirtWGrass.jpg");
        MATERIALS[4].setTexture("ColorMap", tex1);
        
        MATERIALS[5] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex2 = assetManager.loadTexture("Materials/Dirt1.png");
        MATERIALS[5].setTexture("ColorMap", tex2);
        
        MATERIALS[6] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex3 = assetManager.loadTexture("Materials/HDgrass.jpg");
        MATERIALS[6].setTexture("ColorMap", tex3);
    }
    
    public static Material getMaterial(int blockType, int face)
    {
        switch(blockType)
        {
            case 1:
                switch(face)
                {
                    case 0:
                        return MATERIALS[4];

                    case 1:
                        return MATERIALS[4];
                        
                    case 2:
                        return MATERIALS[4];
                        
                    case 3:
                        return MATERIALS[4];
                        
                    case 4:
                        return MATERIALS[5];
                        
                    case 5:
                        return MATERIALS[6];
                }
            case 2:
                return MATERIALS[3];
                
            default:
                
                return MATERIALS[3];
        }
    }
}

