package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/*!
    \brief
 */
public class Image {
    private final BufferedImage image;

    /*!
        \brief Constructor.

        \param path - The path to the image to be loaded.
     */
    public Image(String path) {
        image = LoadImage(path);
    }

    /*! \brief Cuts image

        Returns an image starting from the point with x and y as coordinates and with the specified width and height.
     */
    public BufferedImage crop(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    /*!
        \brief Loads image

        Takes the path to an image as a parameter and returns a reference to the loaded image or null.
     */
    public static BufferedImage LoadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(Image.class.getResource(path)));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
