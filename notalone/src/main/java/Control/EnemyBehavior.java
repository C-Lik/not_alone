package Control;

import Entity.Enemy;
import Entity.EntityOrientation;
import Entity.Player;

//! \brief Manages the enemies actions.
public class EnemyBehavior {

    private static final Player player = Player.getInstance();

    /*!
     \brief Controls the enemies behaviour based on the player position.

     It determines whether the player is in the enemy area and establish based on the distance between them the
     followings:
     - follow the player;
     - attack the player;
     - keep walking.
     */
    public static void behave(Enemy enemy) {

        int distanceX = enemy.getHitBox().x - player.getHitBox().x;
        int distanceY = Math.abs(player.getHitBox().y - enemy.getHitBox().y);
        int minDistanceY = Math.abs(player.getHitBox().height - enemy.getHitBox().height);
        if (!enemy.wasHit()) {

            // if player is within enemy's boundaries
            if (player.getPosition() + player.getHitBox().width >= enemy.getBoundLeft() &&
                    player.getPosition() <= enemy.getBoundRight() - player.getHitBox().width) {

                int minDistance = Math.min(player.getHitBox().width, enemy.getHitBox().width);
                if (Math.abs(distanceX) <= minDistance && distanceY <= minDistanceY) {
                    // if enemy is close enough to the player, then he can attack
                    enemy.setAttacking(true);
                    enemy.setVelocityX(0);
                } else {
                    enemy.resetVelocityX();// reset initial velocity
                    if (distanceY <= 100) {
                        float vel = enemy.getVelocityX();
                        if (vel <= 3)
                            vel += 1.5F;
                        enemy.setVelocityX(vel + 1.5F);
                        // if distance is negative, the player is at right of enemy -> facing = 1
                        if (distanceX > 0) {
                            enemy.setFacing(EntityOrientation.left);
                        } else {
                            enemy.setFacing(EntityOrientation.right);
                        }
                    }
                    enemy.setAttacking(false);
                }
            } else {
                enemy.resetVelocityX(); // reset initial velocity
                enemy.setAttacking(false);
            }
        }

        // enemy is walking
        if (enemy.isFacingRight()) {
            if ((int) (enemy.getX() + enemy.getVelocityX() + enemy.getHitBox().width) < enemy.getBoundRight()) {
                enemy.setX(enemy.getX() + enemy.getVelocityX());
            } else {
                enemy.setFacing(EntityOrientation.left);
            }
        } else {
            if ((int) (enemy.getX() - enemy.getVelocityX()) >= enemy.getBoundLeft()) {
                enemy.setX(enemy.getX() - enemy.getVelocityX());
            } else {
                enemy.setFacing(EntityOrientation.right);
            }
        }
    }
}
