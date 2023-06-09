package Entity;

import Control.EnemyBehavior;
import Graphics.Assets;
import Graphics.Animation;

import java.awt.*;

/*!
    \brief Implements the HellDog enemy.

    Inherits from the \ref Enemy class with specific methods and attributes.
 */
public class HellDog extends Enemy {

    private final int scale = 2;

    /*! \brief Constructor.

        - Sets:
            \li the initial position in the map;
            \li the dimensions of the entity;
            \li the attack power;
            \li the health;
            \li the speed on the X-axis;
        - Creates the list of animations.
     */
    public HellDog(int xPos, int yPos) {
        super(xPos, yPos);
        y -= 20 * scale;
        hitBox = new Rectangle((int) x, (int) y, 35 * scale, 20 * scale);
        lifeBox = new Rectangle((int) x, (int) y - 20, 35 * scale, 5);
        life = 60;
        hitPower = -10;
        Assets tempA = Assets.getInstance();
        animation.put("runR", new Animation(2, tempA.dog_runR));
        animation.put("runL", new Animation(2, tempA.dog_runL));
        animation.put("idleR", new Animation(4, tempA.dog_idleR));
        animation.put("idleL", new Animation(4, tempA.dog_idleL));
        resetVelocityX();
    }

    //! \brief Resets the speed of the entity on the X-axis.
    @Override
    public void resetVelocityX() {
        velocityX = 5;
    }

    /*!
        \brief Update.

        Calls:
        - the enemy control function (\ref Control.EnemyControl);
        - the position update function \ref updatePosition();
        - the health level update function \ref updateLifeBox();
        - the animation running function for each animation of the entity.
     */
    @Override
    public void update() {
        EnemyBehavior.behave(this);
        updatePosition();
        updateLifeBox();
        runAnimations();
    }

    /*!
        \brief Draw the entity on the window.

        Depending on the direction of the entity, the health and one of the animations will be drawn, with the
        following priorities:
        - it has been attacked or is attacking;
        - it is running.
    */
    @Override
    public void draw(Graphics g) {

        int distance = hitBox.height - 32 * scale;

        if (gotHit || attacking) {
            if (facing == 1)
                animation.get("idleR").drawAnimation(g, hitBox.x - 16 * scale, hitBox.y + distance, 64 * scale, 32 * scale);
            else
                animation.get("idleL").drawAnimation(g, hitBox.x - 16 * scale, hitBox.y + distance, 64 * scale, 32 * scale);
        } else {
            if (facing == 1)
                animation.get("runR").drawAnimation(g, hitBox.x - 16 * scale, hitBox.y + distance, 67 * scale, 32 * scale);
            else
                animation.get("runL").drawAnimation(g, hitBox.x - 16 * scale, hitBox.y + distance, 67 * scale, 32 * scale);
        }
        drawLifeBox(g);
    }
}
