package Items;

import Graphics.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

/*!
    \brief Potion

    Collision with the player will provide a benefit.
 */
public class Potion extends ItemBlock {

    private final BufferedImage image;

    /*!
        \brief Parameterized constructor.

        Sets the positions of the element, its dimensions, and the image.

        @param xPos - the position of the element on the X-axis;
        @param yPos - the position of the element on the Y-axis;
        @param id - the type of potion.
     */
    public Potion(int xPos, int yPos, int id) {
        super(xPos + 16, yPos);
        hitBox = new Rectangle(x, y, 32, 40);
        benefit = 20;
        if(id == 1) {
            image = Assets.getInstance().potions[0];
        }
        else {
            image = Assets.getInstance().potions[1];
            benefit += 5;
        }
    }

    //! \brief Draws the corresponding image.
    @Override
    public void draw(Graphics g) {
        g.drawImage(image, hitBox.x, hitBox.y, hitBox.width, hitBox.height, null);
    }
}
