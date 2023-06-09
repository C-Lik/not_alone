package Items;

import java.awt.*;

import Graphics.Animation;
import Graphics.Assets;

/*!
    \brief Map transition element.

    It is a solid element of the map.
    When the player collides with it, a transition between maps occurs or the game enters the win state.
 */
public class Portal extends ItemBlock {

    private final Animation portalAnim;

    /*!
        \brief Parameterized constructor.

        Sets the positions of the element, its dimensions, and the animation.

        @param xPos - the position of the element on the X-axis;
        @param yPos - the position of the element on the Y-axis;
     */
    public Portal(int xPos, int yPos) {
        super(xPos, yPos - 200);
        hitBox = new Rectangle(x, y, 48, 200);
        portalAnim = new Animation(4, Assets.getInstance().portal);
    }

    //! \brief Updates the position of the element based on the camera and runs the animation.
    @Override
    public void update() {
        super.update();
        portalAnim.runAnimation();
    }

    //! \brief Draws the animation.
    @Override
    public void draw(Graphics g) {
        portalAnim.drawAnimation(g, hitBox.x - hitBox.width, hitBox.y, hitBox.width * 2, hitBox.height);
    }
}
