package NewGame;


import NewGame.Utils.PerlinNoise;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import jme3tools.optimize.GeometryBatchFactory;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lucas
 */
public class BlockBuildingTest extends SimpleApplication implements ActionListener
{
    public static Clump[][][] world = new Clump[10][10][10];
    
    public static void main(String[] moooo)
    {
        BlockBuildingTest test = new BlockBuildingTest();
        test.start();
    }

    @Override
    public void simpleInitApp() 
    {
        MaterialIndex.loadMaterials(assetManager);
        cam.setLocation(new Vector3f(1, 1, 1));
        
        
        //Clump baseClump = new Clump(true);
        
        WorldBlock block = new WorldBlock();
        
        for(int i = 0; i < 1; i++)
        {
            for(int j = 0; j < 1; j++)
            {
                for(int k = 0; k < 1; k++)
                {
                    addClump(i, j, k, block);
                }
            }
        }
        
        
         
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
        
        
        initKeys();
    }
    
    public void initKeys()
    {
        inputManager.addMapping("leftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "leftClick");
    }
    
    public void addClump(int x, int y, int z, WorldBlock block)
    {  
        Clump c = new Clump(block);
        world[x][y][z] = c;
        c.setLocalTranslation(x*10, y*10, z*10);
        rootNode.attachChild(world[x][y][z]);
    }
    
    /*
    public void checkFaces(int x, int y, int z, boolean cull)
    {
        if(x != 9)
        {
            if(world[x+1][y][z] != null)
            {
                world[x][y][z].cullFace(3, cull);
                world[x+1][y][z].cullFace(2, cull);
            }
        }
        if(x != 0)
        {
            if(world[x-1][y][z] != null)
            {
                world[x][y][z].cullFace(2, cull);
                world[x-1][y][z].cullFace(3, cull);
            }
        }
        if(y != 9)
        {
            if(world[x][y+1][z] != null)
            {
                world[x][y][z].cullFace(5, cull);
                world[x][y+1][z].cullFace(4, cull);
            }
        }
        if(y != 0)
        {
            if(world[x][y-1][z] != null)
            {
                world[x][y][z].cullFace(4, cull);
                world[x][y-1][z].cullFace(5, cull);
            }
        }
        if(z != 9)
        {
            if(world[x][y][z+1] != null)
            {
                world[x][y][z].cullFace(0, cull);
                world[x][y][z+1].cullFace(1, cull);
            }
        }
        if(z != 0)
        {
            if(world[x][y][z-1] != null)
            {
                world[x][y][z].cullFace(1, cull);
                world[x][y][z-1].cullFace(0, cull);
            }
        }
        
    }
     * 
     */

    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("leftClick") && isPressed)
        {
            System.out.println("click!");
            Vector3f origin    = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2, cam.getHeight()/2), 0.0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2, cam.getHeight()/2), 0.3f);
            dir.subtractLocal(origin).normalizeLocal();
            Ray ray = new Ray(origin, dir);
            CollisionResults results = new CollisionResults();
            rootNode.collideWith(ray, results);
            //OtherEntityHandler.others.collideWith
            Geometry collide = new Geometry();
            try
            {
                collide = results.getClosestCollision().getGeometry();
            }
            catch(NullPointerException ex){}
            
            if(collide.getMesh() instanceof Quad)
            {               
                Node n = collide.getParent();
                System.out.println(n);
                int x = (int)n.getLocalTranslation().x, y = (int)n.getLocalTranslation().y, z = (int)n.getLocalTranslation().z;
                Clump c = (Clump)n.getParent();
                System.out.println(c);
                n.removeFromParent();
                c.checkFaces(x, y, z, false);
               
            }
        }
    }
    
}
