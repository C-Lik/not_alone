package Items;

import Graphics.Animation;
import Graphics.Assets;

import java.awt.*;

public class Portal extends ItemBlock {

    private final Animation portalAnim;

    public Portal(int xPos, int yPos) {
        super(xPos, yPos - 200);
        hitBox = new Rectangle(x, y, 48, 200);
        portalAnim = new Animation(4, Assets.getInstance().portal);
    }

    @Override
    public void update() {
        super.update();
        portalAnim.runAnimation();
    }

    @Override
    public void draw(Graphics g) {
        portalAnim.drawAnimation(g, hitBox.x - hitBox.width, hitBox.y, hitBox.width * 2, hitBox.height);
    }
}
