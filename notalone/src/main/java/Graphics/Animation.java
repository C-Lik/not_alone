package Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/*!
    \brief Animation.

    Provides methods for running and drawing an animation.
 */
public class Animation {

    private final int speed;
    private final int framesNumber;
    private int speedCounter = 0;
    private int currentFrameNumber = 0;

    private final BufferedImage[] images;   //contains all images of an animation
    private BufferedImage currentImg;       //current image

    public Animation(int speed, BufferedImage[] args) {
        this.speed = speed;
        framesNumber = args.length;
        images = new BufferedImage[framesNumber];
        System.arraycopy(args, 0, images, 0, framesNumber);
    }

    /*!
        \brief Runs the animation.

        The function increments the speed counter which determines to set the current image of the animation after
        \ref speed iterations.
     */
    public void runAnimation() {
        speedCounter++;
        if (speedCounter > speed) {
            speedCounter = 0;

            // next frame of the animation
            currentImg = images[currentFrameNumber];
            currentFrameNumber++;

            if (currentFrameNumber >= framesNumber)
                currentFrameNumber = 0;
        }
    }

    //! \brief Draws the current image of the animation.
    public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
        g.drawImage(currentImg, x, y, scaleX, scaleY, null);
    }
}
