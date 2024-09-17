package Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Image {
    private final BufferedImage image;

    public Image(String path) {
        image = loadImage(path);
    }

    /*!
        \brief Loads image

        Takes the path to an image as a parameter and returns a reference to the loaded image or null.
     */
    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(Image.class.getResource(path)));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /*! \brief Cuts image

        Returns an image starting from the point (x, y), with the specified width and height.
     */
    public BufferedImage crop(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }
}
