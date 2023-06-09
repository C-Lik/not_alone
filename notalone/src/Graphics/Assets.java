package Graphics;

import java.awt.image.BufferedImage;

/*!
    \brief Manages images.

    Contains references to all images specific to each game element.
    \note Implements the Singleton design pattern.
 */
public class Assets {

    private static Assets instance = null;

    Image player;
    Image goblin;
    Image ghoul;
    Image hellDog;
    Image portalSprite;

    public final BufferedImage[] player_idleR = new BufferedImage[8];
    public final BufferedImage[] player_idleL = new BufferedImage[8];
    public final BufferedImage[] player_runR = new BufferedImage[8];
    public final BufferedImage[] player_runL = new BufferedImage[8];
    public final BufferedImage[] player_attackR = new BufferedImage[8];
    public final BufferedImage[] player_attackL = new BufferedImage[8];
    public final BufferedImage[] player_jumpR = new BufferedImage[2];
    public final BufferedImage[] player_jumpL = new BufferedImage[2];
    public final BufferedImage[] player_fallR = new BufferedImage[2];
    public final BufferedImage[] player_fallL = new BufferedImage[2];
    public final BufferedImage[] player_hitL = new BufferedImage[3];
    public final BufferedImage[] player_hitR = new BufferedImage[3];
    public final BufferedImage[] player_deathR = new BufferedImage[7];
    public final BufferedImage[] player_deathL = new BufferedImage[7];

    public final BufferedImage[] goblin_attackR = new BufferedImage[8];
    public final BufferedImage[] goblin_attackL = new BufferedImage[8];
    public final BufferedImage[] goblin_runR = new BufferedImage[8];
    public final BufferedImage[] goblin_runL = new BufferedImage[8];
    public final BufferedImage[] goblin_hitR = new BufferedImage[4];
    public final BufferedImage[] goblin_hitL = new BufferedImage[4];

    public final BufferedImage[] ghoul_walkR = new BufferedImage[8];
    public final BufferedImage[] ghoul_walkL = new BufferedImage[8];

    public final BufferedImage[] dog_runR = new BufferedImage[5];
    public final BufferedImage[] dog_runL = new BufferedImage[5];
    public final BufferedImage[] dog_idleR = new BufferedImage[6];
    public final BufferedImage[] dog_idleL = new BufferedImage[6];

    public final BufferedImage[] potions = new BufferedImage[2];
    public final BufferedImage[] portal = new BufferedImage[8];

    public final BufferedImage menu;
    public final BufferedImage help;
    public final BufferedImage win;
    public final BufferedImage lost;

    /*!
        \brief Private constructor

        Sets the sprites and images, loading images and cutting them using the \ref getAssets() function
     */
    private Assets() {
        player = new Image("/items/player.png");
        goblin = new Image("/items/goblin.png");
        ghoul = new Image("/items/ghoul.png");
        hellDog = new Image("/items/hellDog.png");
        portalSprite = new Image("/items/portal.png");

        potions[0] = Image.LoadImage("/items/potions/potion1.png");
        potions[1] = Image.LoadImage("/items/potions/potion2.png");

        menu = Image.LoadImage("/textures/states/menu.png");
        help = Image.LoadImage("/textures/states/help.png");
        win = Image.LoadImage("/textures/states/win.png");
        lost = Image.LoadImage("/textures/states/lost.png");
        getAssets();
    }

    //! \brief Sets the images arrays
    private void getAssets() {

        int length = player_idleR.length;
        for (int i = 0; i < length; ++i) {
            player_attackR[i] = player.crop(i * 320, 0, 320, 250);
            player_attackL[length - i - 1] = player.crop(i * 320, 250, 320, 250);

            player_runR[i] = player.crop(i * 128, 500, 128, 128);
            player_runL[i] = player.crop((i + length) * 128, 500, 128, 128);
            player_idleR[i] = player.crop(i * 112, 628, 112, 200);
            player_idleL[i] = player.crop((i + length) * 112, 628, 112, 200);

            goblin_attackL[length - i - 1] = goblin.crop(i * 208, 0, 208, 103);
            goblin_attackR[i] = goblin.crop(i * 208, 103, 208, 103);
            goblin_runL[length - i - 1] = goblin.crop(i * 96, 206, 96, 95);
            goblin_runR[i] = goblin.crop((i + length) * 96, 206, 96, 95);

            ghoul_walkL[i] = ghoul.crop(i * 96, 0, 96, 111);
            ghoul_walkR[length - i - 1] = ghoul.crop(i * 96, 111, 96, 111);
            portal[i] = portalSprite.crop(i * 48, 0, 48, 52);
        }

        length = player_fallR.length;
        for (int i = 0; i < length; ++i) {
            player_fallR[i] = player.crop(i * 128, 828, 128, 184);
            player_fallL[i] = player.crop(i * 128 + 256, 828, 128, 184);
            player_jumpL[i] = player.crop(i * 144 + 512, 828, 144, 161);
            player_jumpR[i] = player.crop(i * 144 + 800, 828, 144, 161);
        }

        length = player_hitR.length;
        for (int i = 0; i < length; ++i) {
            player_hitR[i] = player.crop(i * 96 + 1792, 628, 96, 177);
            player_hitL[i] = player.crop(i * 96 + 2080, 628, 96, 177);
        }

        length = player_deathR.length;
        for (int i = 0; i < length; ++i) {
            player_deathR[i] = player.crop(i * 176, 989, 176, 186);
            player_deathL[i] = player.crop((i + length) * 176, 989, 176, 186);
        }

        length = goblin_hitR.length;
        for (int i = 0; i < length; ++i) {
            goblin_hitL[length - i - 1] = goblin.crop(i * 112, 301, 112, 95);
            goblin_hitR[i] = goblin.crop((i + length) * 112, 301, 112, 95);
        }


        length = dog_idleR.length;
        for (int i = 0; i < length; ++i) {
            dog_idleR[i] = hellDog.crop(i * 64, 0, 64, 32);
            dog_idleL[length - i - 1] = hellDog.crop(i * 64, 32, 64, 32);
        }

        length = dog_runR.length;
        for (int i = 0; i < length; ++i) {
            dog_runR[i] = hellDog.crop(i * 67, 64, 67, 32);
            dog_runL[length - i - 1] = hellDog.crop(i * 67, 96, 67, 32);
        }

    }

    //! \brief Gets the class instance
    public static Assets getInstance() {
        if (instance == null)
            instance = new Assets();
        return instance;
    }
}
