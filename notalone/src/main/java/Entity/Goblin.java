package Entity;

import Control.EnemyBehavior;
import Graphics.Animation;
import Graphics.Assets;

import java.awt.*;

/*!
    \brief Implements the Goblin enemy.

    Inherits from the \ref Enemy class with specific methods and attributes.
 */
public class Goblin extends Enemy {

    /*! \brief Constructor.

        - Sets:
            \li the initial position in the map;
            \li the dimensions of the entity;
            \li the attack power;
            \li the health;
            \li the speed on the X-axis;
        - Creates the list of animations.
     */
    public Goblin(int xPos, int yPos) {
        super(xPos, yPos - 100);
        hitBox = new Rectangle((int) x, (int) y, 50, 100);
        lifeBox = new Rectangle((int) x, (int) y - 10, 50, 5);
        life = 110;
        hitPower = -25;
        Assets tempA = Assets.getInstance();
        animation.put("runR", new Animation(3, tempA.goblin_runR));
        animation.put("runL", new Animation(3, tempA.goblin_runL));
        animation.put("hitR", new Animation(4, tempA.goblin_hitR));
        animation.put("hitL", new Animation(4, tempA.goblin_hitL));
        animation.put("attackR", new Animation(4, tempA.goblin_attackR));
        animation.put("attackL", new Animation(4, tempA.goblin_attackL));
        resetVelocityX();
    }

    //! \brief Resets the speed of the entity on the X-axis.
    @Override
    public void resetVelocityX() {
        velocityX = 3;
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
        - it has been attacked;
        - it is attacking;
        - it is running.
     */
    @Override
    public void draw(Graphics g) {

        if (gotHit) {
            if (facing == 1)
                animation.get("hitR").drawAnimation(g, hitBox.x - 20, hitBox.y + 5, 112, 95);
            else
                animation.get("hitL").drawAnimation(g, hitBox.x - 30, hitBox.y + 5, 112, 95);
        } else if (attacking) {
            if (facing == 1)
                animation.get("attackR").drawAnimation(g, hitBox.x - 96, hitBox.y - 3, 208, 103);
            else
                animation.get("attackL").drawAnimation(g, hitBox.x - 64, hitBox.y - 3, 208, 103);
        } else {
            if (facing == 1)
                animation.get("runR").drawAnimation(g, hitBox.x - 10, hitBox.y + 5, 96, 95);
            else
                animation.get("runL").drawAnimation(g, hitBox.x - 30, hitBox.y + 5, 96, 95);
        }
        drawLifeBox(g);
    }

}
