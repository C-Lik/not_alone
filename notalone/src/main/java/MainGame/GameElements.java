package MainGame;

import Audio.SoundLibrary;
import Control.Collision;
import Entity.*;
import Exceptions.CriticalExceptionHandler;
import GameState.*;
import Input.*;
import Items.*;
import Graphics.Map;

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

    public Player player;
    private final Map map;
    public static DBHandler level;
    public Portal portal;
    public CopyOnWriteArrayList<ItemBlock> items = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    public boolean hitPortal = false;
    private final Collision collCheck;

    /*!
        \brief Constructor.

        Set references to Singleton class instances:
        \li \ref Collision
        \li \ref Player
        \li \ref CurrentLevel

        Constructs a new map.
     */
    public GameElements() {
        collCheck = Collision.getInstance();
        player = Player.getInstance();
        level = DBHandler.getInstance();
        map = new Map();
    }

    /*!
        \brief  Update.

        Calls the following methods:
        \li \ref playSound();
        \li \ref updateElements();
        \li \ref updatePortal();
        \li \ref updateWinOrNextMap();
        \li \ref checkIfGameOver();
     */
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
        if (enemies.size() != 0) {
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
            level.readLevel();
            Game.currentMapNumber = Integer.parseInt(level.getMapName().substring(3));
            player.increaseHitPowerBy(-(Game.currentMapNumber - 1) * 5);
        } else {
            if (Game.currentMapNumber < Game.MAPS_NUMBER) {
                Game.currentMapNumber++;
            }
            level.updateLevel();
        }
        map.loadMap(level.getMapName());
        createItems();
        createEnemies();
        level.clearLists();

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
        if (enemies.size() == 0) {
            portal.draw(g);
        }
    }

    //! \brief Creates the list of elements based on the data read from the database.
    private void createItems() {
        Random rand = new Random();
        LinkedHashMap<String, Vector<Vector<Integer>>> list = level.getItemsList();
        for (String item : list.keySet()) {
            Vector<Vector<Integer>> indexes = list.get(item);
            for (Vector<Integer> index : indexes) {
                int x = index.get(0) * Game.tileSize;
                int y = index.get(1) * Game.tileSize;
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
        LinkedHashMap<String, Vector<Vector<Integer>>> list = level.getEnemiesList();

        for (String item : list.keySet()) {
            Vector<Vector<Integer>> indexes = list.get(item);

            for (Vector<Integer> index : indexes) {
                int x = index.get(0) * Game.tileSize;
                int y = index.get(1) * Game.tileSize;
                int left = index.get(2) * Game.tileSize;
                int right = index.get(3) * Game.tileSize;

                Enemy tempEnemy = null;

                if (item.equals("goblin")) {
                    tempEnemy = new Goblin(x, y);
                }
                if (item.equals("ghoul")) {
                    tempEnemy = new Ghoul(x, y);
                }
                if (item.equals("hellDog")) {
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
