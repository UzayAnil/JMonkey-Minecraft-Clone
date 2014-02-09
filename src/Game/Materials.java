
package Game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.math.ColorRGBA;

/**
 * Sets up the materials to be used in the game
 * @author Lucas's Computer
 */
public class Materials 
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

        
        MATERIALS[3] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture gras = assetManager.loadTexture("Materials/DirtWGrass.jpg");
        MATERIALS[3].setTexture("ColorMap", gras);
        
        MATERIALS[4] = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        Texture ston = assetManager.loadTexture("Materials/Stone.png");
        MATERIALS[4].setTexture("ColorMap", ston);
        
        
    }
}
