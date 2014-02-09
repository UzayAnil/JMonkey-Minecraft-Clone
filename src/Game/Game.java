
package Game;

import World.Block;
import World.Chunk;
import World.World;
import World.WorldUpdateQueue;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import java.util.List;

/**
 * Game is a SimpleApplication that implements the ActionListener interface
 * Game represents a randomly generated block world where the user can add or 
 * remove blocks
 * @author Lucas's Computer
 */
public class Game extends SimpleApplication implements ActionListener
{
    public static Application app;
    public static World world;
    private static BulletAppState bulletAppState;
    private static RigidBodyControl collisionTemplate;
    
    private Node activeCollisions;

    
    /**
     * Called at the Start of Application
     * loads Everything
     */
    public void simpleInitApp() 
    {
        app = this;
        loadAssets();
        cam.setLocation(new Vector3f(16*200 + 8, 256, 16*200 + 8));
        
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        flyCam.setMoveSpeed(20);
        
        collisionTemplate = new RigidBodyControl(new MeshCollisionShape(new Box(.5f, .5f, .5f)), 0);
        activeCollisions = new Node();
        
        world = new World(World.Size.LARGE);
        //world.setCullHint(CullHint.Dynamic);
        rootNode.attachChild(world);
        
        for(int i = 200; i < 211; i++)
        {
                for(int k = 200; k < 211; k++)
                {
                    world.addChunk(i, k);
                }
        }
        
        //CollisionShape worldCollision = CollisionShapeFactory.createMeshShape(world);
        //RigidBodyControl landscape = new RigidBodyControl(worldCollision, 0);
        //world.addControl(landscape);
        //
        //.addControl(new RigidBodyControl(new MeshCollisionShape(box), 0));

        //world.addChunk(200, 200);
        //Chunk c = world.world[200][200];
        //addPhysicsBlock(c.getMaxBlock(8, 8));
         
        
        
        
        
        //cam.setLocation(new Vector3f(8, 24, 8));
        //cam.lookAtDirection(new Vector3f(0, -1, 0).normalizeLocal(), Vector3f.UNIT_Y);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
        
        
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Textures/crosshair.png", true);
        pic.setWidth(settings.getWidth()/60);
        pic.setHeight(settings.getHeight()/60);
        pic.setPosition(settings.getWidth()/2, settings.getHeight()/2);
        guiNode.attachChild(pic);


        this.viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        
        if (usePhysics) {
            CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(0.25f, 1.8f, 1);
            player3 = new CharacterControl(capsuleShape, 0.5f);
            player3.setJumpSpeed(10);
            player3.setFallSpeed(10);
            player3.setGravity(10);

            player3.setPhysicsLocation(new Vector3f(16*200 +8, 256, 16*200+8));

            bulletAppState.getPhysicsSpace().add(player3);
        }
        
        this.initKeys();
        
        
    }
    
    
    /**
     * Loads the materials to be used
     */
    public void loadAssets()
    {
        Materials.loadMaterials(assetManager);
    }
    
    private boolean usePhysics = false;
    private boolean physicsAdded = false;

    private CharacterControl player3;


    private void initKeys() {
        // You can map one or several inputs to one named action
        this.inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        this.inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        this.inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        this.inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        this.inputManager.addMapping("Jumps", new KeyTrigger(KeyInput.KEY_SPACE));
        this.inputManager.addListener(this.actionListener, "Lefts");
        this.inputManager.addListener(this.actionListener, "Rights");
        this.inputManager.addListener(this.actionListener, "Ups");
        this.inputManager.addListener(this.actionListener, "Downs");
        this.inputManager.addListener(this.actionListener, "Jumps");
        
        inputManager.addMapping("leftClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("rightClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(this, "leftClick", "rightClick");
    }
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    
    /**
     * Adds physics to a block
     * @param b 
     */
    public static void addPhysicsBlock(Block b)
    {
        b.addControl(new RigidBodyControl(new MeshCollisionShape(b.getMesh()), 0));
        bulletAppState.getPhysicsSpace().add(b);
    }
    
    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(final String name, final boolean keyPressed, final float tpf) {
            if (name.equals("Lefts")) {
                if (keyPressed) {
                    Game.this.left = true;
                } else {
                    Game.this.left = false;
                }
            } else if (name.equals("Rights")) {
                if (keyPressed) {
                    Game.this.right = true;
                } else {
                    Game.this.right = false;
                }
            } else if (name.equals("Ups")) {
                if (keyPressed) {
                    Game.this.up = true;
                } else {
                    Game.this.up = false;
                }
            } else if (name.equals("Downs")) {
                if (keyPressed) {
                    Game.this.down = true;
                } else {
                    Game.this.down = false;
                }
            } else if (name.equals("Jumps")) {
                Game.this.player3.jump();
            }
        }
    };
    private final Vector3f walkDirection = new Vector3f();

    /**
     * Overrides the simpleUpdate method
     * Updates the word
     * @param tpf 
     */
    public void simpleUpdate(final float tpf) 
    {
        WorldUpdateQueue.readyToAdd = true;
        //addChunk();
        //updateRegions();
        //updateCollision();
        Vector3f camDir = this.cam.getDirection().clone().multLocal(0.2f);
        Vector3f camLeft = this.cam.getLeft().clone().multLocal(0.4f);
        this.walkDirection.set(0, 0, 0);
        if (this.left) {
            this.walkDirection.addLocal(camLeft);
        }
        if (this.right) {
            this.walkDirection.addLocal(camLeft.negate());
        }
        if (this.up) {
            this.walkDirection.addLocal(camDir);
        }
        if (this.down) {
            this.walkDirection.addLocal(camDir.negate());
        }

        if (usePhysics) {
            this.player3.setWalkDirection(this.walkDirection);
            this.cam.setLocation(this.player3.getPhysicsLocation());
        }
    }
    
    /**
     * Adds chunks to the world using WorldUpdateQueue
     */
    public void addChunk()
    {
        if(!WorldUpdateQueue.updateQueue.isEmpty() && WorldUpdateQueue.readyToAdd)
        {
            WorldUpdateQueue.readyToAdd = false;
            Vector3f r = WorldUpdateQueue.updateQueue.remove(0);
            Game.world.addChunk((int)r.x, (int)r.z);
        }
        
    }
    
    /**
     * Updates the state of the regiouns the user is located in
     */
    public void updateRegions()
    {
        Vector3f loc = cam.getLocation();
        
        Vector3f local = new Vector3f(loc.x%16, loc.y%16, loc.z%16);
        Vector3f cLoc = new Vector3f(loc.x/16, 0, loc.z/16);
        System.out.println(local);
        if(local.x > 8 && world.world[(int)cLoc.x+1][(int)cLoc.z] == null)
        {
            WorldUpdateQueue.addUpdateRequest(new Vector3f((int)cLoc.x+1, 5, (int)cLoc.z));
            System.out.println("added");
        }
        if(local.y > 8 && world.world[(int)cLoc.x][(int)cLoc.z+1] == null)
        {
            WorldUpdateQueue.addUpdateRequest(new Vector3f((int)cLoc.x, 5, (int)cLoc.z+1));
            System.out.println("added");
        }
        
            
    }
    
    int colUpdateCounter = 0;
    
    /**
     * Updates collision in the world
     */
    public void updateCollision()
    {
        
        Vector3f loc = cam.getLocation();
        ArrayList<Block> inRange  = world.getInRangeBlocks((int)loc.x, (int)loc.z);
        
        
        for(Block b: inRange)
        {
            if(!b.hasBounding)
            {
                b.hasBounding = true;
                b.addControl(collisionTemplate.cloneForSpatial(b));
                bulletAppState.getPhysicsSpace().add(b); 
                activeCollisions.attachChild(b);
            }
        }
        if(colUpdateCounter == 1000)
        {
            //removeOldCollisions(inRange);
            colUpdateCounter = 0;
        }
        else
        {
            colUpdateCounter++;
        }
    }
    
    /**
     * Removes old collision
     * I.E gets rid of when collision when a block is removed
     * @param collisions 
     */
    public void removeOldCollisions(ArrayList<Block> collisions)
    {
        List<Spatial> allCol = activeCollisions.getChildren();
        for(Spatial b: allCol)
        {
            if(!collisions.contains((Block)b))
            {
                activeCollisions.detachChild(b);
                bulletAppState.getPhysicsSpace().remove(b);
            }    
        }
    }
    
    /**
     * This class allows for the addition or subtraction of blocks
     * If left clicked it will remove a block.
     * If right clicked it will add a block.
     * @param name
     * @param isPressed
     * @param tpf 
     */
    public void onAction(String name, boolean isPressed, float tpf) 
    {
        if(name.equals("leftClick") && isPressed)
        {
            System.out.println("Left!");
            Vector3f origin    = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2, cam.getHeight()/2), 0.0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2, cam.getHeight()/2), 0.3f);
            dir.subtractLocal(origin).normalizeLocal();
            Ray ray = new Ray(origin, dir);
            CollisionResults results = new CollisionResults();
            rootNode.collideWith(ray, results);
            
            Vector3f hit = new Vector3f();
            Vector3f normal = new Vector3f();
            try
            {
                hit = results.getClosestCollision().getContactPoint();
                normal = results.getClosestCollision().getContactNormal();
                
            }
            catch(NullPointerException ex)
            {
                
            }
            
            System.out.println(hit);
            System.out.println(normal);
            
            int x = (int)Math.floor(hit.x+.5);
            int y = (int)Math.floor(hit.y+.5);
            int z = (int)Math.floor(hit.z+.5);
            
            
            Block block = world.chooseClickedBlock(x, y, z, normal);
            
            try
            {
                System.out.println(hit);
                world.getChunk((int)hit.x, (int)hit.z).updateMeshRemove(block);
                bulletAppState.getPhysicsSpace().remove(block); 
                
                
            }
            catch(NullPointerException ex)
            {
                
            }
             
        }
        
        if(name.equals("rightClick") && isPressed)
        {
            System.out.println("Right!");
            
            Vector3f origin    = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2, cam.getHeight()/2), 0.0f);
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(cam.getWidth()/2, cam.getHeight()/2), 0.3f);
            dir.subtractLocal(origin).normalizeLocal();
            Ray ray = new Ray(origin, dir);
            CollisionResults results = new CollisionResults();
            rootNode.collideWith(ray, results);
            
            Vector3f hit = new Vector3f();
            Vector3f normal = new Vector3f();
            try
            {
                hit = results.getClosestCollision().getContactPoint();
                normal = results.getClosestCollision().getContactNormal();
                
            }
            catch(NullPointerException ex)
            {
                
            }
            System.out.println(hit);
            System.out.println(normal);
            
            int x = (int)Math.floor(hit.x+.5);
            int y = (int)Math.floor(hit.y+.5);
            int z = (int)Math.floor(hit.z+.5);
            
            
            Block block = world.chooseClickedBlock(x, y, z, normal);
            try{
            if(getAddBlockVector(block.getLocalTranslation(), normal) == null)
            {
                System.out.println("This is null");
            }
                Vector3f newBlock = getAddBlockVector(block.getLocalTranslation(), normal);
                world.getChunk((int)hit.x, (int)hit.z).updateMeshAdd((int)newBlock.x%16, (int)newBlock.y, (int)newBlock.z%16);
            }
            catch(NullPointerException ex){}
        }
        
        
    };
    
    /**
     * Gets the vector where the block should be added(based upon click)
     * @param block
     * @param normal
     * @return Vector3f
     */
    public Vector3f getAddBlockVector(Vector3f block, Vector3f normal)
    {
        if(normal.x == 1)
        {
            return new Vector3f(block.x+1, block.y, block.z);
        }
        if(normal.x == -1)
        {
            return new Vector3f(block.x-1, block.y, block.z);
        }
        if(normal.y == 1)
        {
            return new Vector3f(block.x, block.y+1, block.z);
        }
        if(normal.y == -1)
        {
            return new Vector3f(block.x, block.y-1, block.z);
        }
        if(normal.z == 1)
        {
            return new Vector3f(block.x, block.y, block.z+1);
        }
        if(normal.z == -1)
        {
            return new Vector3f(block.x, block.y, block.z-1);
        }
        return null;
    }
    
    private int num = 202;
}
