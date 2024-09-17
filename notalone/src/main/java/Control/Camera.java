package Control;

import Entity.Player;
import MainGame.Game;
import Graphics.Map;

/*!
    \brief The relative positions of elements

    Provides methods for: \n
    \li setting and getting the X-axis value position;
    \li stopping the camera movement at the map endings.

    \note Implements the Singleton design pattern.
 */
public class Camera {

    private static Camera instance = null;

    private int cameraX;
    private int startCamPos;

    /*!
     \brief Private constructor

     Sets the X-axis position to 0;
     */
    private Camera() {
        cameraX = 0;
    }

    //! \brief Sets the start camera position on the X-axis.
    public void setStartCamPos() {
        startCamPos = Game.WIDTH / 2 - Player.getInstance().getHitBox().width / 2;
    }

    //! \brief Gets the class instance.
    public static Camera getInstance() {
        if (instance == null)
            instance = new Camera();
        return instance;
    }

    /*!
     \brief Stops the camera movement.

     Stops the camera movement at the beginning and the end of the map.

     \param player - The Player object.
     */

    public void stopCamera(Player player) {
        if ((int) player.getX() >= startCamPos && cameraX <= 0) {
            if (cameraX >= -(Map.worldWidth - Game.WIDTH - player.getHitBox().width / 2))
                cameraX -= player.getVelocityX();
            else {
                // if we reached the end of the map
                player.setX(player.getX() + player.getVelocityX());
                if (player.getX() < startCamPos) {
                    player.setX(startCamPos);
                    cameraX -= player.getVelocityX();
                }
            }
        } else {
            cameraX = 0;
            player.setX(player.getX() + player.getVelocityX());
        }
    }

    //! \brief Retrieves the camera position on the X-axis.
    public int getCameraXAxis() {
        return cameraX;
    }

    //! \brief Sets the camera position.
    public void setCamera(int value) {
        cameraX = value;
    }
}
