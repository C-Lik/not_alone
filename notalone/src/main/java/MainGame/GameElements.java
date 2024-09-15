package MainGame;

import Audio.SoundLibrary;
import Control.Collision;
import Database.DBHandler;
import Database.EnemyEntity;
import Database.ItemEntity;
import Entity.*;
import Exceptions.CriticalExceptionHandler;
import GameState.GameOverState;
import GameState.MenuOption;
import GameState.PlayState;
import GameState.WinState;
import Graphics.Map;
import Input.KeyHandler;
import Items.ItemBlock;
import Items.Portal;
import Items.Potion;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/*!
    \brief Implements the game elements.
 */
public class GameElements {

    public static DBHandler level;
    private final Map map;
    private final Collision collCheck;
    public Player player;
    public Portal portal;
    public CopyOnWriteArrayList<ItemBlock> items = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    public boolean hitPortal = false;

   public GameElements() {
        collCheck = Collision.getInstance();
        player = Player.getInstance();
        level = DBHandler.getInstance();
        map = new Map();
    }

    public void update() {
        playSound();
        updateElements();
        collCheck.checkCollisionItems(items, player);
        collCheck.checkCollisionEnemies(enemies, player);
        updatePortal();
        updateWinOrNextMap();
        checkIfGameOver();
    }

    /*!
        \brief Decide and sets two possible states.

        If the player intersects with the portal, then:\n
            - if the current map number is the last one, transition to the game won state;
            - otherwise, attempt to load the next map;
     */
    private void updateWinOrNextMap() {
        // if player gets to the portal, then load the next map or get to the win state
        if (!hitPortal && player.getHitBox().intersects(portal.hitBox)) {
            if (Game.currentMapNumber == Game.MAPS_NUMBER) {
                SoundLibrary.gameSound.stop();
                level.updateWin();
                PlayState.wasWin = true;
                Game.getInstance().state = WinState.getInstance();
            } else {
                hitPortal = true;
                try {
                    reset();
                } catch (Exception e) {
                    CriticalExceptionHandler.handle(e);
                }
            }
        }
    }

    /*!
        \brief Calls the update function for the portal if the enemy list is empty.
     */
    private void updatePortal() {
        if (!enemies.isEmpty()) {
            if (KeyHandler.killEnemiesPressed)
                enemies.clear();
        } else {
            portal.update();
        }
    }

    //! \brief Enters the game over state if the player dies.
    private void checkIfGameOver() {
        if (player.getLife() <= 0) {
            SoundLibrary.gameSound.stop();
            Game.getInstance().state = GameOverState.getInstance();
        }
    }

    //! \brief Calls the update methods for all the elements held by the class.
    private void updateElements() {
        map.update();
        player.update();
        for (ItemBlock item : items)
            item.update();
        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.getLife() <= 0)
                enemies.remove(enemy);
        }
    }

    //! \brief Plays the sound if it is not disabled.
    private void playSound() {
        if (Game.SoundOn)
            SoundLibrary.gameSound.loopPlaying();
        else
            SoundLibrary.gameSound.stop();
    }

    /*!
        \brief Resets the elements.

        Clears the references to enemies and items.\n
        Calls the reset function for the player.\n
        Stops the sound of the previous map and sets the current one.\n
        Calls the functions for updating the level/reading the level.\n
        Loads a new map corresponding to the current level.\n
        Creates the enemies and beneficial items.
     */
    public void reset() throws IOException {
        items.clear();
        enemies.clear();
        player.reset();
        hitPortal = false;

        if (Game.option == MenuOption.newGame) {
            level.updateLevel();
        } else if (Game.option == MenuOption.loadGame) {
            level.loadLevel();
            Game.currentMapNumber = level.getMapId();
            player.increaseHitPowerBy(-(Game.currentMapNumber - 1) * 5);
        } else {
            if (Game.currentMapNumber < Game.MAPS_NUMBER) {
                Game.currentMapNumber++;
            }
            level.updateLevel();
        }
        map.loadMap("map" + level.getMapId());
        createItems();
        createEnemies();
        level.clearOldData();

        SoundLibrary.gameSound.stop();
        if (Game.currentMapNumber != 0)
            SoundLibrary.gameSound.setClip("map" + Game.currentMapNumber + "Sound.wav");
    }

    //! \brief Calls the drawing functions for each element.
    public void draw(Graphics g) {
        map.draw(g);
        player.draw(g);
        for (ItemBlock item : items)
            item.draw(g);
        for (Entity entity : enemies)
            entity.draw(g);
        if (enemies.isEmpty()) {
            portal.draw(g);
        }
    }

    //! \brief Creates the list of elements based on the data read from the database.
    private void createItems() {
        Random rand = new Random();
        LinkedHashMap<String, Vector<ItemEntity>> list = level.getItemsList();
        for (String item : list.keySet()) {
            Vector<ItemEntity> entities = list.get(item);
            for (ItemEntity entity : entities) {
                int x = entity.indexX() * Game.tileSize;
                int y = entity.indexY() * Game.tileSize;
                if (item.equals("potion")) {
                    items.add(new Potion(x, y, rand.nextInt(2) + 1));
                }
                if (item.equals("portal")) {
                    portal = new Portal(x, y);
                }
            }
        }
    }

    //! \brief Creates the list of enemies based on the data read from the database.
    private void createEnemies() {
        LinkedHashMap<String, Vector<EnemyEntity>> list = level.getEnemiesList();

        for (String item : list.keySet()) {
            Vector<EnemyEntity> entities = list.get(item);

            for (EnemyEntity entity : entities) {
                int x = entity.indexX() * Game.tileSize;
                int y = entity.indexY() * Game.tileSize;
                int left = entity.leftLimit() * Game.tileSize;
                int right = entity.rightLimit() * Game.tileSize;

                Enemy tempEnemy = null;

                if (item.equals("goblin")) {
                    tempEnemy = new Goblin(x, y);
                }
                if (item.equals("ghoul")) {
                    tempEnemy = new Ghoul(x, y);
                }
                if (item.equals("hell_dog")) {
                    tempEnemy = new HellDog(x, y);
                }

                if (tempEnemy != null) {
                    tempEnemy.setBounds(left, right);
                    tempEnemy.increaseHitPowerBy(-(Game.currentMapNumber - 1) * 5);
                    enemies.add(tempEnemy);
                }
            }
        }
    }
}
