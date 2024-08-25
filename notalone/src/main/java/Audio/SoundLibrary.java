package Audio;


/*!
    \brief Sounds library.

    Contains the game sounds and handles their closure upon game shutdown.
 */
public class SoundLibrary {

    public final static Sound clickSound = new Sound("clickOnButton.wav");
    public final static Sound deactivatedClickSound = new Sound("deactivatedButton.wav");

    public final static Sound menu = new Sound("menuSound.wav");
    public final static Sound gameSound = new Sound();

    public final static Sound getItem = new Sound("getItem.wav");
    public final static Sound playerAttack = new Sound("playerAttack.wav");
    public final static Sound playerJump = new Sound("jump.wav");

    //! \brief Closes the sounds.
    public static void closeSounds() {
        clickSound.close();
        deactivatedClickSound.close();
        menu.close();
        gameSound.close();
        getItem.close();
        playerJump.close();
        playerAttack.close();
    }
}
