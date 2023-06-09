package Graphics;

import Control.Camera;
import Items.Block;
import MainGame.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/*!
    \brief The current level map.

    The class contains methods that handle:\n
    - reading files and constructing a map;
    - updating and drawing the map.
 */

public class Map {

    public final static ArrayList<Block> gameMap = new ArrayList<>();
    private final static ArrayList<BufferedImage> mapPlans = new ArrayList<>();

    public static final int rows = 18;
    public static final int cols = 125;

    public static final int worldWidth = cols * Game.tileSize;
    private final Camera camera = Camera.getInstance();

    //! \brief No-Args constructor.
    public Map() {}

    /*!
     \brief Member function that loads the map.

     Opens the directory with the map name and reads:
     \li the matrix from which new blocks will be constructed and added to a list;
     \li the images that will be added to a list.

     \attention Throws an IO error that needs to be handled at a higher level.

     @param mapName - a string representing the name of the map to be loaded.
     */
    public void loadMap(String mapName) throws IOException {
        mapPlans.clear();
        gameMap.clear();
        String location = "./res/textures/";
        File file = new File(location + mapName);
        String[] fileList = file.list();
        String pathToMap = "";

        assert fileList != null;
        for (String str : fileList) {
            if (str.contains(".txt")) pathToMap = "/textures/" + mapName + "/" + str;
            else if (str.contains(mapName)) {
                mapPlans.add(Image.LoadImage("/textures/" + mapName + "/" + str));
            }
        }

        InputStream mapFile = getClass().getResourceAsStream(pathToMap);
        assert mapFile != null;
        BufferedReader bf = new BufferedReader(new InputStreamReader(mapFile));
        for (int i = 0; i < rows; i++) {
            String[] elements = bf.readLine().split(" ");
            for (int j = 0; j < cols; j++) {
                if (Integer.parseInt(elements[j]) != 0) {
                    gameMap.add(new Block(j * Game.tileSize, i * Game.tileSize));
                    if (j == 0 || j == cols - 1) {
                        gameMap.get(gameMap.size() - 1).hitBox.width = 1;
                    }
                }
            }
        }
    }

    //! \brief Function that updates the position of each block constructed in the \ref loadMap(String) function.
    public void update() {
        for (Block block : gameMap)
            block.update();
    }

    /*!
     \brief Function that draws the map images.

     This function retrieves the images one by one from a list and cuts them based on the window size and the
     position of the \ref Control.Camera, then displays them in the window.

     @param g - the graphics object used to draw the images.
     */
    public void draw(Graphics g) {

        // mapPlans.get(0) represents the background image
        g.drawImage(mapPlans.get(0), 0, 0, Game.WIDTH, mapPlans.get(0).getHeight() * 3, null);
        double parallaxSpeed = 0;
        for (int i = 1; i < mapPlans.size(); ++i) {
            if (i <= mapPlans.size() - 2) parallaxSpeed += 0.2;
            else parallaxSpeed = 1;

            int height = mapPlans.get(i).getHeight() * 2;
            int yPos = Game.HEIGHT - height;

            int xPos = (int) Math.abs(camera.getCamera() * parallaxSpeed / 2);
            BufferedImage img = mapPlans.get(i).getSubimage(xPos, 0, Game.WIDTH / 2, height / 2);
            g.drawImage(img, 0, yPos, Game.WIDTH, height, null);
        }
    }
}