package Items;

import Control.Camera;

import java.awt.*;

//! \brief Abstractions the concept of an item block.
public abstract class ItemBlock {
    protected int x, y, benefit;
    public Rectangle hitBox;
    private final static Camera camera = Camera.getInstance();

    /*!
        \brief Parameterized constructor.

         Sets the positions of the item block.

        @param xPos - the position of the element on the X-axis;
        @param yPos - the position of the element on the Y-axis;
     */
    public ItemBlock(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    //! \brief Updates the position of the item block in the game window based on the camera.
    public void update() {
        hitBox.x = camera.getCamera() + this.x;
    }

    //! Abstract method.
    public abstract void draw(Graphics g);

    //! Returns the benefit of the item block.
    public int getBenefit() {
        return benefit;
    }
}
