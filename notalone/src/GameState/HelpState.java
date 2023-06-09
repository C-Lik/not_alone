package GameState;

import java.awt.*;

import Graphics.Assets;
import Input.KeyHandler;
import MainGame.Game;

/*!
    \brief Help State.

    \note \note This class extends \ref GameState and represents a component of the State design pattern implementation.
    \note Implements the Singleton design pattern.
 */
public class HelpState extends GameState {

    private static HelpState instance = null;
    private final Rectangle back;

    /*!
        \brief Private constructor.

        Sets the dimensions, button position, and specific image of the game state.
     */
    private HelpState() {
        back = new Rectangle(20, 20, 57, 57);
        image = Assets.getInstance().help;
    }

    /*!
        \brief Update.

        Checks if the "Back" button or the ESC key has been pressed, and if so, transitions to the Menu state (\ref MenuState).
     */
    @Override
    public void update() {
        if (buttonWasClicked(back) || KeyHandler.exitPressed) {
            Game.getInstance().state = MenuState.getInstance();
        }
    }

    //! \brief Gets the class instance.
    public static HelpState getInstance() {
        if (instance == null) {
            instance = new HelpState();
        }
        return instance;
    }
}
