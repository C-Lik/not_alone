package Control;

import Audio.SoundLibrary;
import Entity.Enemy;
import Entity.Player;
import Input.KeyHandler;
import Items.Block;
import Items.ItemBlock;
import Graphics.Map;

import java.util.concurrent.CopyOnWriteArrayList;

/*!
    \brief Collision detection

    Provides methods that determine the collisions between the player and enemies, elements and other blocks.

    \note Implements the Singleton design pattern.
 */
public class Collision {

    private static Collision instance = null;
    private long startTimeCollision, collisionDuration;
    private Enemy tempEnemy;

    //! \brief Private constructor
    private Collision() {}

    /*!
        \brief Checks if an entity collides with the map blocks.

        The player collision with the map blocks will stop him to pass through them.
     */
    public void checkCollisionTile(Player player) {

        if (KeyHandler.upPressed && !player.getAttacking()) {
            player.getHitBox().y++;
            for (Block block : Map.gameMap) {
                if (block.hitBox.intersects(player.getHitBox())) {
                    player.setVelocityY(-13);
                }
            }
            player.getHitBox().y--;
        }
        float GRAVITY = 0.5f;
        player.setVelocityY(player.getVelocityY() + GRAVITY);

        // horizontal collision
        player.getHitBox().x += (int) player.getVelocityX();
        for (Block block : Map.gameMap) {
            if (player.getHitBox().intersects(block.hitBox)) {
                player.getHitBox().x -= (int) player.getVelocityX();
                while (!block.hitBox.intersects(player.getHitBox())) {
                    player.getHitBox().x += (int) Math.signum(player.getVelocityX());
                }
                player.setVelocityX(0);
                player.getHitBox().x = (int) player.getX();
                player.setFalling(false);
            }
        }

        // vertical collision
        player.getHitBox().y += (int) player.getVelocityY();

        for (Block block : Map.gameMap) {
            if (player.getHitBox().intersects(block.hitBox)) {
                player.getHitBox().y -= (int) player.getVelocityY();
                while (!block.hitBox.intersects(player.getHitBox())) {
                    player.getHitBox().y += (int) Math.signum(player.getVelocityY());
                }
                player.getHitBox().y -= (int) Math.signum(player.getVelocityY());
                player.setVelocityY(0);
                player.setY(player.getHitBox().y);
                player.setFalling(false);
            }
        }
    }

    /*!
        \brief  Checks if the player collides with the map elements.

        When the player collides with an element, the collision determines:
        - Benefits:\n
            \li increase in the player's life level based on the collected element;\n
            \li increase in the available number of attacks.\n
        - Play the corresponding sound;\n
        - Remove the element after it is collected.
     */
    public void checkCollisionItems(CopyOnWriteArrayList<ItemBlock> items, Player player) {
        for (ItemBlock elem : items) {
            if (player.getHitBox().intersects(elem.hitBox)) {
                SoundLibrary.getItem.play();

                if (player.getLife() < 100) {
                    player.updateLife(elem.getBenefit());
                    if (player.getLife() >= 100)
                        player.updateLife(100 - player.getLife());
                }

                player.increaseNumberAttack();
                items.remove(elem);
            }
        }
    }

    /*!
        \brief Checks if the player collides with the enemies.

        The player collision with an enemy will decrease the life of the player with the enemy attack power.\n
        If the player attacks, then the enemy life will decrease with the player attack power.
     */
    public void checkCollisionEnemies(CopyOnWriteArrayList<Enemy> enemies, Player player) {
        for (Enemy elem : enemies) {
            if (player.getHitBox().intersects(elem.getHitBox()) && !player.wasHit()) {
                tempEnemy = elem;
                startTimeCollision = System.nanoTime();
                player.setWasHit(true);
            }
            if (collisionDuration == 0 && player.getAttacking())
                if (player.getAttackBox().intersects(elem.getHitBox()) && !player.wasHit()) {
                    startTimeCollision = System.nanoTime();
                    elem.setWasHit(true);
                    elem.setVelocityX(0);
                    tempEnemy = elem;
                }
        }

        if (System.nanoTime() - startTimeCollision >= 900_000_000) {
            if (player.wasHit()) {
                player.updateLife(tempEnemy.getHitPower());
                player.setWasHit(false);
            }
        }

        if (System.nanoTime() - startTimeCollision >= 10_000_000) {
            if (tempEnemy != null && tempEnemy.wasHit()) {
                tempEnemy.updateLife(player.getHitPower());
                tempEnemy.resetVelocityX(); // sets velocity x on initial value
                tempEnemy.setWasHit(false);
            }
            collisionDuration = 0;
        }
    }

    //! Gets the class instance
    public static Collision getInstance() {
        if (instance == null) {
            instance = new Collision();
        }
        return instance;
    }
}