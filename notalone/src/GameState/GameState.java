package GameState;

import Audio.SoundLibrary;
import Input.MouseHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

/*!
    \brief Game State.

    \note Implements the State design pattern.
 */
public abstract class GameState {

    protected BufferedImage image;

    //! Abstract method.
    public abstract void update();

    //! Draw the image of the game state.
    public void draw(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    public static boolean wasWin = false;

    /*!
        \brief Check if the button has been pressed.

        The function:
        - plays the button press sound (different sound for game already won when the load button is pressed);
        - invalidates the saved mouse coordinates.

        @return true/false depending on whether the button has been pressed.
     */
    public boolean buttonWasClicked(Rectangle button) {
        if (MouseHandler.mouseX >= button.x && MouseHandler.mouseX <= button.x + button.width) {
            if (MouseHandler.mouseY >= button.y && MouseHandler.mouseY <= button.y + button.height) {
                // if we won, and we press Load button, then play another sound
                if (wasWin && button.y == 319)
                    SoundLibrary.deactivatedClickSound.play();
                else
                    SoundLibrary.clickSound.play();
                MouseHandler.resetMouse();
                return true;
            }
        }
        return false;
    }
}
