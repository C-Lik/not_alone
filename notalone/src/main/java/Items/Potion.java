package Items;

import Graphics.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Potion extends ItemBlock {

    private final BufferedImage image;

    public Potion(int xPos, int yPos, int id) {
        super(xPos + 16, yPos);
        hitBox = new Rectangle(x, y, 32, 40);
        benefit = 20;
        if (id == 1) {
            image = Assets.getInstance().potions[0];
        } else {
            image = Assets.getInstance().potions[1];
            benefit += 5;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, hitBox.x, hitBox.y, hitBox.width, hitBox.height, null);
    }
}
