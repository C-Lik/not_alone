package GameState;

import java.awt.*;

import Audio.Sound;
import Graphics.Assets;
import Input.KeyHandler;
import MainGame.Game;

/*!
    \brief Win State.

    \note This class extends \ref GameState and represents a component of the State design pattern implementation.
    \note Implements the Singleton design pattern.
 */

public class WinState extends GameState {

    private static WinState instance = null;
    private static final Sound winGame = new Sound("winGame.wav");
    private final Rectangle back;

    /*!
      \brief Private constructor.

      Sets the dimensions, position, and specific image of the button in the win state.
   */
    private WinState() {
        back = new Rectangle(20, 20, 57, 57);
        image = Assets.getInstance().win;
    }

    /*!
        \brief Update.

        This method checks if the "Back" button or the ESC key has been pressed, and if so, it
        transitions to the Menu state (\ref MenuState).
     */
    @Override
    public void update() {
        if (buttonWasClicked(back) || KeyHandler.exitPressed) {
            Game.getInstance().state = MenuState.getInstance();
        }
    }

    //! \brief Get the class instance and play the corresponding sound.
    public static WinState getInstance() {
        if (instance == null) {
            instance = new WinState();
        }
        winGame.play();
        return instance;
    }
}
