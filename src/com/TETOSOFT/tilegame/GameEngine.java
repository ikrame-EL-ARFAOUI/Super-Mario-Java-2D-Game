package com.TETOSOFT.tilegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;

import com.TETOSOFT.audio.Audio;
import com.TETOSOFT.graphics.*;
import com.TETOSOFT.input.*;
import com.TETOSOFT.test.GameCore;
import com.TETOSOFT.tilegame.sprites.*;

/**
 * GameManager manages all parts of the game.
 */
public class GameEngine extends GameCore 
{
	public static void main(String[]args) throws IOException {
		Accueil maFenetre = new Accueil();
		maFenetre.setVisible(true);
		while(maFenetre.isShowing()) {
			System.out.println("");
		}
		new GameEngine().run();
	}
    
    
    public static final float GRAVITY = 0.002f;
    
    private Point pointCache = new Point();
    private TileMap map;
    private MapLoader mapLoader;
    private InputManager inputManager;
    private TileMapDrawer drawer;
    private int mapNumber = 1;

    
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction jump;
    private GameAction exit;
    private GameAction mouseClicked;
    private GameAction switchBackgrounds;
    
    private GameAction exitE;
    private GameAction replay;
    
    private int collectedStars=0;
    private int numLives=6;
    
    
     public void init()
    {
        super.init();
        
        // set up input manager
        initInput();
        
        // start resource manager
        mapLoader = new MapLoader(screen.getFullScreenWindow().getGraphicsConfiguration());
        
        // load resources
        drawer = new TileMapDrawer();
        drawer.setBackground(mapLoader.loadImage("background1.jpg"));
        
        // load first map
        map = mapLoader.loadNextMap();
    }

    /**
     * Switches Background
     */
    public void switchBackground()
    {
        if (this.mapNumber == 1)
        {
            drawer.setBackground(mapLoader.loadImage("background2.png"));
        }
        else
        {
            drawer.setBackground(mapLoader.loadImage("background1.jpg"));
        }

        this.mapNumber = ( this.mapNumber + 1 ) % 2;
    }


    /**
     * Closes any resurces used by the GameManager.
     */
    public void stop() {
        super.stop();
        
    }
    
    
    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit",GameAction.DETECT_INITAL_PRESS_ONLY);
        mouseClicked = new GameAction("mouseClicked",GameAction.DETECT_INITAL_PRESS_ONLY);
        switchBackgrounds = new GameAction("switchBagrounds",GameAction.DETECT_INITAL_PRESS_ONLY);
        
        inputManager = new InputManager(screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);
        
        exitE = new GameAction("switchBagrounds",GameAction.DETECT_INITAL_PRESS_ONLY);
     	replay = new GameAction("switchBagrounds",GameAction.DETECT_INITAL_PRESS_ONLY);
        
        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(jump, KeyEvent.VK_UP);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(switchBackgrounds, KeyEvent.VK_X);
        inputManager.mapToMouse(mouseClicked, MouseEvent.BUTTON3);
        
        inputManager.mapToKey(exitE, KeyEvent.VK_E);
     	inputManager.mapToKey(replay, KeyEvent.VK_R);
    }
    
    
    private void checkInput(long elapsedTime) 
    {
        
        if (exit.isPressed()) {
            stop();
        }
        
        if (exitE.isPressed()) {
            stop();  
        }
   	    if (replay.isPressed()) {
   		 new GameEngine().run(); 
        }
        
        if(mouseClicked.isPressed()) {
        	menuAction();
        }

        if (switchBackgrounds.isPressed())
        {
            switchBackground();
        }
        
        Player player = (Player)map.getPlayer();
        if (player.isAlive()) 
        {
            float velocityX = 0;
            if (moveLeft.isPressed()) 
            {
                velocityX-=player.getMaxSpeed();
            }
            if (moveRight.isPressed()) {
                velocityX+=player.getMaxSpeed();
            }
            if (jump.isPressed()) {
                player.jump(false);
            }
            player.setVelocityX(velocityX);
        }
        
        
    }
    
    
    
    public void menuAction() {
		// TODO Auto-generated method stub
    	int mx = inputManager.getMouseX();
    	int my = inputManager.getMouseY();
    	int screenWidth = screen.getWidth();
    	int screenHeight = screen.getHeight();
    	if( mx >= screenWidth / 2 - 90 && mx <= screenWidth / 2 + 110) {
    		if(mx >= 200 && my <= 250) {
    			stop();
    		}
    		if(my >= 300 && my <= 350) {
    			new GameEngine().run();
    		}
    	}
		
	}

	public void draw(Graphics2D g) {
    	
      	switch (getScene()) {
    	case 1:
    		   g.setColor(Color.black);
    		   g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
    		   new GameOver().draw(g,screen.getWidth(),screen.getHeight());
    		   break;
    		   
    	default:
        drawer.draw(g, map, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Press ESC for EXIT.",10.0f,20.0f);
        g.drawString("Press X to Change Background from Night to Day",10.0f,40.0f);
        g.setColor(Color.GREEN);
        g.drawString("Coins: "+collectedStars,300.0f,20.0f);
        g.setColor(Color.YELLOW);
        g.drawString("Lives: "+(numLives),500.0f,20.0f );
        g.setColor(Color.WHITE);
        g.drawString("Home: "+mapLoader.currentMap,700.0f,20.0f);
      	}
        
        
    }
    
    
   

	/**
     * Gets the current map.
     */
    public TileMap getMap() {
        return map;
    }
    
    
    /**
     * Gets the tile that a Sprites collides with. Only the
     * Sprite's X or Y should be changed, not both. Returns null
     * if no collision is detected.
     */
    public Point getTileCollision(Sprite sprite, float newX, float newY) 
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);
        
        // get the tile locations
        int fromTileX = TileMapDrawer.pixelsToTiles(fromX);
        int fromTileY = TileMapDrawer.pixelsToTiles(fromY);
        int toTileX = TileMapDrawer.pixelsToTiles(
                toX + sprite.getWidth() - 1);
        int toTileY = TileMapDrawer.pixelsToTiles(
                toY + sprite.getHeight() - 1);
        
        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                        map.getTile(x, y) != null) {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Checks if two Sprites collide with one another. Returns
     * false if the two Sprites are the same. Returns false if
     * one of the Sprites is a Creature that is not alive.
     */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }
        
        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }
        
        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());
        
        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
                s2x < s1x + s1.getWidth() &&
                s1y < s2y + s2.getHeight() &&
                s2y < s1y + s1.getHeight());
    }
    
    
    /**
     * Gets the Sprite that collides with the specified Sprite,
     * or null if no Sprite collides with the specified Sprite.
     */
    public Sprite getSpriteCollision(Sprite sprite) {
        
        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }
        
        // no collision found
        return null;
    }
    
    
    /**
     * Updates Animation, position, and velocity of all Sprites
     * in the current map.
     */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();
        
        
        // player is dead! start map over
        if (player.getState() == Creature.STATE_DEAD) {
            map = mapLoader.reloadMap();
            return;
        }
        
        // get keyboard/mouse input
        checkInput(elapsedTime);
        
        // update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);
        
        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                } else {
                    updateCreature(creature, elapsedTime);
                }
            }
            // normal update
            sprite.update(elapsedTime);
        }
    }
    
    
    /**
     * Updates the creature, applying gravity for creatures that
     * aren't flying, and checks collisions.
     */
    private void updateCreature(Creature creature,
            long elapsedTime) {
        
        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                    GRAVITY * elapsedTime);
        }
        
        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
                getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x) -
                        creature.getWidth());
            } else if (dx < 0) {
                creature.setX(
                        TileMapDrawer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }
        
        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y) -
                        creature.getHeight());
            } else if (dy < 0) {
                creature.setY(
                        TileMapDrawer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }
        
    }
    
    
    /**
     * Checks for Player collision with other Sprites. If
     * canKill is true, collisions with Creatures will kill
     * them.
     */
    public void checkPlayerCollision(Player player,
            boolean canKill) {
        if (!player.isAlive()) {
            return;
        }
        
        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        } else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            Audio.playSound("/audio/ecrasePersonnage.wav");
            if (canKill) {
                // kill the badguy and make player bounce
                badguy.setState(Creature.STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
            } else {
                // player dies!
                player.setState(Creature.STATE_DYING);
                numLives--;
                Audio.playSound("/audio/partiePerdue.wav");
                if(numLives==0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Audio.playSound("/audio/game-over.wav");
                    //stop();
                    setSceen(1);
                }
            }
        }
    }
    
    
    /**
     * Gives the player the speicifed power up and removes it
     * from the map.
     */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);

        
        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
        	collectedStars++;
            Audio.playSound("/audio/piece.wav");
            if(collectedStars==100) 
            {
            	 if (numLives<6) numLives++;
                collectedStars=0;
           
            }
            
          
            
        } 
       
        else if (powerUp instanceof PowerUp.Music) {
            // change the music
        	Audio.playSound("/audio/power-up.wav");
            
        } else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map      
        	
      
            map = mapLoader.loadNextMap();
            Audio.playSound("/audio/new-map.wav");
            
        }
        else if (powerUp instanceof PowerUp.Lives) {
            
            if (numLives<6) numLives++;
         
        }
        
        
        
    }
    
    
      
}
