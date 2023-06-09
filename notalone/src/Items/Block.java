package Items;

import MainGame.Game;

import java.awt.*;

//! \brief Block element.
public class Block extends ItemBlock {

    /*!
        \brief Parameterized constructor.

        Sets the positions and dimensions of the block.

        @param xPos - the position of the element on the X-axis;
        @param yPos - the position of the element on the Y-axis;
     */
    public Block(int xPos, int yPos) {
        super(xPos, yPos);
        hitBox = new Rectangle(x, y, Game.tileSize, Game.tileSize);
    }

    @Override
    public void draw(Graphics g) {}
}
