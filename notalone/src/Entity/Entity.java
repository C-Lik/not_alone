package Entity;

import java.awt.*;
import java.util.LinkedHashMap;

import Control.Camera;
import Graphics.Animation;

/*!
    \brief Entity.

    Abstracts the characteristics of an entity with methods and properties specific to one.
 */
public abstract class Entity {

    protected float x, y, velocityX = 0;
    protected boolean attacking, gotHit = false;
    protected int facing = 1, life, lastLife, hitPower;
    protected Rectangle hitBox, lifeBox;
    protected final LinkedHashMap<String, Animation> animation;
    protected static final Camera camera = Camera.getInstance();

    //! \brief Constructor that sets the position and instantiates a list of animations.
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        animation = new LinkedHashMap<>();
    }

    public abstract void update();

    public abstract void draw(Graphics g);

    //! Updates the health level.
    public void updateLifeBox() {
        if (lastLife != life) {
            try {
                lifeBox.width = life * lifeBox.width / lastLife;
            } catch (Exception ignored) {}
            lastLife = life;
        }
    }

    //! Runs the entity's animations.
    public void runAnimations() {
        for (Animation anim : animation.values())
            anim.runAnimation();
    }

    //! \brief Returns the position on the X-axis.
    public float getX() {
        return x;
    }

    //! \brief Sets the position on the X-axis.
    public void setX(float x) {
        this.x = x;
    }

    //! \brief Returns the speed on the X-axis.
    public float getVelocityX() {
        return velocityX;
    }

    //! \brief Sets the speed on the X-axis.
    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    //! \brief Returns the \ref hitBox.
    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean getAttacking() {
        return attacking;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int f) {
        facing = f;
    }

    public int getLife() {
        return life;
    }

    public void updateLife(int value) {
        life += value;
    }

    public boolean wasHit() {
        return gotHit;
    }

    public void setWasHit(boolean value) {
        gotHit = value;
    }

    //! \brief Returns the attack power of the entity.
    public int getHitPower() {
        return hitPower;
    }

    //! \brief Increases the attack power.
    public void increaseHitPowerBy(int value) {
        hitPower += value;
    }

    //! \brief Draws the health level corresponding to the entity's health.
    public void drawLifeBox(Graphics g) {
        g.drawRect(lifeBox.x, lifeBox.y, hitBox.width, lifeBox.height);
        Color prev = g.getColor();
        g.setColor(Color.red);
        g.fillRect(lifeBox.x + 1, lifeBox.y + 1, lifeBox.width - 1, lifeBox.height - 1);
        g.setColor(prev);
    }
}
