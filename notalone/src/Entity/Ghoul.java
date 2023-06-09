package Entity;

import Control.EnemyBehavior;
import Graphics.Animation;
import Graphics.Assets;

import java.awt.*;

/*!
    \brief Implements the Ghoul enemy.

    Inherits from the \ref Enemy class with specific methods and attributes.
 */
public class Ghoul extends Enemy {

    /*! \brief Constructor.

        - Sets:
            \li the initial position in the map;
            \li the dimensions of the entity;
            \li the attack power;
            \li the health;
            \li the speed on the X-axis;
        - Creates the list of animations.
     */
    public Ghoul(int xPos, int yPos) {
        super(xPos, yPos - 100);
        hitBox = new Rectangle((int) x, (int) y, 40, 100);
        lifeBox = new Rectangle((int) x, (int) y - 10, 40, 5);
        life = 100;
        hitPower = -20;
        Assets tempA = Assets.getInstance();
        animation.put("walkR", new Animation(4, tempA.ghoul_walkR));
        animation.put("walkL", new Animation(4, tempA.ghoul_walkL));
        resetVelocityX();
    }

    //! \brief Resets the speed of the entity on the X-axis.
    @Override
    public void resetVelocityX() {
        velocityX = 2.5F;
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

        Depending on the direction of the entity, a specific animation and its health will be drawn.
     */
    @Override
    public void draw(Graphics g) {
        if (facing == 1)
            animation.get("walkR").drawAnimation(g, hitBox.x - 20, hitBox.y - 11, 96, 111);
        else
            animation.get("walkL").drawAnimation(g, hitBox.x - 30, hitBox.y - 11, 96, 111);
        drawLifeBox(g);
    }
}
