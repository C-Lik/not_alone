package Entity;

/*!
    \brief Enemy.

     Abstracts the characteristics of an enemy. Extends the \ref Entity class.
 */
public abstract class Enemy extends Entity {

    private int[] bounds;

    //! \brief Constructor that sets the position of the enemy.
    public Enemy(float x, float y) {
        super(x, y);
    }

    public abstract void resetVelocityX();

    //! \brief Constructor that sets the position of the enemy.
    public void updatePosition() {
        hitBox.x = (int) (camera.getCameraXAxis() + this.x);
        lifeBox.x = hitBox.x;
    }

    //! \brief Sets the boundaries of the entity.
    public void setBounds(int value1, int value2) {
        bounds = new int[]{value1, value2};
    }

    //! \brief Returns the lower boundary of the entity.
    public int getBoundLeft() {
        return bounds[0];
    }

    //! \brief Returns the upper boundary of the entity.
    public int getBoundRight() {
        return bounds[1];
    }

}
