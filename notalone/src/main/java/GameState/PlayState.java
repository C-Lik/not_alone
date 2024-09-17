package GameState;

import Audio.SoundLibrary;
import Control.Camera;
import Input.KeyHandler;
import MainGame.Game;
import MainGame.GameElements;

import java.awt.*;

/*!
    \brief Play State.

    This class contains a member variable that represents all the elements specific to a game map, and it calls
    the update and draw methods for those elements.

    \note This class extends \ref GameState and represents a component of the State design pattern implementation.
    \note Implements the Singleton design pattern.
 */
public class PlayState extends GameState {

    private static PlayState instance = null;
    public final GameElements elements;

    /*!
        \brief Private constructor.

        Instantiates the camera and constructs a new member containing the main elements.
     */
    private PlayState() {
        Camera.getInstance().setStartCamPos();
        elements = new GameElements();
    }

    /*!
       \brief Update.

       It calls the update function of the game elements and checks if the ESC key has been pressed.
       If the ESC key is pressed, it transitions to the MenuState and stops the current map's sound.
    */
    @Override
    public void update() {
        elements.update();
        if (KeyHandler.exitPressed) {
            SoundLibrary.gameSound.stop();
            Game.getInstance().state = MenuState.getInstance();
        }
    }

    //! \brief Calls the function that draws the elements.
    @Override
    public void draw(Graphics g) {
        elements.draw(g);
    }

    //! \brief Gets the class instance.
    public static PlayState getInstance() {
        if (instance == null) {
            instance = new PlayState();
        }
        return instance;
    }
}
