package Entity;

import Audio.SoundLibrary;
import Control.Collision;
import Graphics.Animation;
import Graphics.Assets;
import Input.KeyHandler;
import MainGame.Game;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

/*!
    \brief Implements the Player entity.

    Inherits from the Entity class with specific methods and attributes.\n
     \note Implements the Singleton design pattern.
 */
public class Player extends Entity {

    private static Player instance = null;
    private final Collision collCheck;

    private final int[] initialPlayerPosition = {100, 200};

    private long startTimeAnimation;
    private int MAX_ATTACK_NUMBER, attackNumberCounter,
            playerPosition, lastSafePositionX, lastSafePositionY, lastFacing, seconds;
    private float velocityY = 0;
    private boolean falling, jumping;
    private final CopyOnWriteArrayList<Long> startTimeAttack = new CopyOnWriteArrayList<>();
    private final Rectangle attackBox;

    /*! \brief Constructor.

      - Sets:
          \li the initial position in the map;
          \li the dimensions of the entity and hitBox;;
          \li the attack power;
          \li the health;
          \li the number of attacks;
          \li the initial number of seconds after which the number of attacks is recharged;
      - Create the list of animations.
   */
    private Player() {

        super(100, 200);

        hitBox = new Rectangle((int) x, (int) y, 45, 100);
        lifeBox = new Rectangle((int) x, (int) y, 45, 5);

        collCheck = Collision.getInstance();

        attackBox = new Rectangle(0, 0, 180, 150);
        life = 100;
        hitPower = -20;
        MAX_ATTACK_NUMBER = 4;
        attackNumberCounter = MAX_ATTACK_NUMBER;
        seconds = 5;

        Assets tempA = Assets.getInstance();
        animation.put("idleR", new Animation(4, tempA.player_idleR));
        animation.put("idleL", new Animation(4, tempA.player_idleL));
        animation.put("runR", new Animation(3, tempA.player_runR));
        animation.put("runL", new Animation(3, tempA.player_runL));
        animation.put("attackR", new Animation(4, tempA.player_attackR));
        animation.put("attackL", new Animation(4, tempA.player_attackL));
        animation.put("jumpR", new Animation(2, tempA.player_jumpR));
        animation.put("jumpL", new Animation(2, tempA.player_jumpL));
        animation.put("fallR", new Animation(2, tempA.player_fallR));
        animation.put("fallL", new Animation(2, tempA.player_fallL));
        animation.put("hitR", new Animation(3, tempA.player_hitR));
        animation.put("hitL", new Animation(3, tempA.player_hitL));
        animation.put("deathR", new Animation(2, tempA.player_deathR));
        animation.put("deathL", new Animation(2, tempA.player_deathL));
    }

    //! \brief Gets the class instance.
    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    /*!
        \brief Update.

        Call the following methods:
        - \ref setFacingAndUpdateVelocityX();
        - \ref checkIfFallInVoid();
        - \ref collCheck.checkCollisionTile(this);
        - \ref camera.stopCamera(this);
        - \ref setJumpFallAndUpdateVelocityY();
        - \ref attack();
        - \ref updateStamina();
        - \ref setPosition();
        - \ref saveLastSafePosition();
        - \ref updateLifeBox();
        - \ref runAnimations();
     */
    @Override
    public void update() {
        setFacingAndUpdateVelocityX();
        checkIfFallInVoid();
        collCheck.checkCollisionTile(this);
        camera.stopCamera(this);
        setJumpFallAndUpdateVelocityY();
        attack();
        updateStamina();
        setPosition();
        saveLastSafePosition();
        updateLifeBox();
        runAnimations();
    }

    @Override
    public void draw(Graphics g) {

        int yPos;
        long duration = System.nanoTime() - startTimeAnimation;
        if (duration >= 900_000_000) {
            attacking = false;
        } else {
            yPos = hitBox.y - 100; // - 100 = hitBox(100) - 200
            if (facing == 1)
                animation.get("attackR").drawAnimation(g, hitBox.x - 64, yPos, 320, 200);
            else
                animation.get("attackL").drawAnimation(g, hitBox.x - 208, yPos, 320, 200);
        }

        if (!attacking) {
            if (jumping) {
                yPos = hitBox.y - 61; // - 61 = hitBox(100) - 161
                if (facing == 1)
                    animation.get("jumpR").drawAnimation(g, hitBox.x - 55, yPos, 144, 161);
                else
                    animation.get("jumpL").drawAnimation(g, hitBox.x - 38, yPos, 144, 161);
            } else if (isFalling()) {
                yPos = hitBox.y - 84; // - 84 = hitBox(100) - 184
                if (facing == 1)
                    animation.get("fallR").drawAnimation(g, hitBox.x - 15, yPos, 128, 184);
                else
                    animation.get("fallL").drawAnimation(g, hitBox.x - 65, yPos, 128, 184);
            } else if (velocityX != 0) {
                yPos = hitBox.y - 28; // - 28 = hitBox(100) - 128
                if (facing == 1)
                    animation.get("runR").drawAnimation(g, hitBox.x - 42, yPos, 128, 128);
                else
                    animation.get("runL").drawAnimation(g, hitBox.x - 42, yPos, 128, 128);
            } else if (gotHit) {
                yPos = hitBox.y - 77;
                if (facing == 1)
                    animation.get("hitR").drawAnimation(g, hitBox.x, yPos, 96, 177);
                else
                    animation.get("hitL").drawAnimation(g, hitBox.x - 35, yPos, 96, 177);
            } else {
                yPos = hitBox.y - 100;
                if (facing == 1)
                    animation.get("idleR").drawAnimation(g, hitBox.x, yPos, 112, 200);
                else
                    animation.get("idleL").drawAnimation(g, hitBox.x - 65, yPos, 112, 200);
            }
        }
        drawLifeBox(g);
        drawAvailableAttacks(g);
    }

    //! \brief Draw the number of attacks and the player's health.
    private void drawAvailableAttacks(Graphics g) {
        // draw available attacks
        Color prev = g.getColor();
        g.setColor(Color.white);
        Font font = new Font("Arial", Font.PLAIN, 24);
        g.setFont(font);
        String s = "Available stamina: " + attackNumberCounter;
        String sLife = "Life: " + life;
        g.drawString(s, 10, 30);
        g.drawString(sLife, Game.WIDTH - 150, 30);
        g.setColor(prev);
    }

    //! \brief Set the absolute position of the player relative to the game window.
    private void setPosition() {
        hitBox.x = (int) x;
        hitBox.y = (int) y;
        lifeBox.x = hitBox.x + facing * 5;
        lifeBox.y = hitBox.y - 20;
        playerPosition = Math.abs(camera.getCameraXAxis()) + hitBox.x;
    }

    //! \brief Increase the position on the Y-axis, set \ref jumping and \ref falling.
    private void setJumpFallAndUpdateVelocityY() {
        y += velocityY;
        if (velocityY < 0) {
            jumping = true;
            SoundLibrary.playerJump.loopPlaying();
        } else if (velocityY > 1) {
            falling = true;
            jumping = false;
            SoundLibrary.playerJump.stop();
        }
    }

    //! \brief Check if the player has fallen into the void (reduce health and reset the player to the last safe position).
    private void checkIfFallInVoid() {
        if (hitBox.y >= 700) {
            life -= 20;
            resetPosition();
        }
    }

    //! \brief Set \ref facing and update the velocity on the X-axis.
    private void setFacingAndUpdateVelocityX() {
        // if we press no button
        if (KeyHandler.leftPressed && KeyHandler.rightPressed ||
                !KeyHandler.leftPressed && !KeyHandler.rightPressed)
            velocityX *= 0.8F;
        else if (!attacking) {
            if (KeyHandler.leftPressed) {
                velocityX--;
                facing = -1;
            } else {
                velocityX++;
                facing = 1;
            }
        }

        if (velocityX > 0 && velocityX < 0.75) velocityX = 0;
        if (velocityX > -0.75 && velocityX < 0) velocityX = 0;

        if (velocityX > 5) velocityX = 5;
        if (velocityX < -5) velocityX = -5;
    }

    /*! \brief Attack.

        Check if the attack button has been pressed.\n
        If the player is not moving, falling, jumping, or attacking, and the available number of attacks is not 0, then:
        - set the \ref attacking flag to true;
        - play the specific attack sound;
        - set the \ref attackBox;
        - decrease the available number of attacks;
        - add the attack time to the list of attacks.
     */
    private void attack() {
        // if we don't move, and we don't jump and didn't press attack already, then we can attack
        if (KeyHandler.attackPressed) {
            if (velocityX == 0 && !jumping && !falling && !attacking && attackNumberCounter != 0) {
                setAttacking(true);
                SoundLibrary.playerAttack.play();
                if (facing == 1) {
                    attackBox.x = hitBox.x + hitBox.width;
                } else {
                    attackBox.x = hitBox.x - attackBox.width;
                }
                attackBox.y = hitBox.y - (attackBox.height - hitBox.height);
                startTimeAnimation = System.nanoTime();
                startTimeAttack.add(startTimeAnimation);
                attackNumberCounter--;
            }
        }
    }

    //! \brief Update the number of attacks, proportional to the time specified with \ref seconds.
    private void updateStamina() {
        if (attackNumberCounter < MAX_ATTACK_NUMBER) {
            long currentTime = System.nanoTime();
            for (Long attackTime : startTimeAttack) {
                if ((currentTime - attackTime) / 1_000_000_000 >= seconds) {
                    attackNumberCounter++;
                    seconds += 5;
                    startTimeAttack.remove(attackTime);
                }
            }
        } else {
            seconds = 5;
        }
    }

    //! \brief Save the player's last safe position (save point).
    private void saveLastSafePosition() {
        // determining the last safe position
        if (!falling && !jumping) {
            if (camera.getCameraXAxis() == 0)
                lastSafePositionX = playerPosition;
            else {
                lastSafePositionX = camera.getCameraXAxis();
                lastFacing = facing;
            }
            lastSafePositionY = hitBox.y;
        }
    }

    //! \brief Reset the player's position to the last safe position.
    public void resetPosition() {
        if (camera.getCameraXAxis() == 0)
            x = initialPlayerPosition[0];
        else
            camera.setCamera(lastSafePositionX + lastFacing * hitBox.width / 2);

        y = lastSafePositionY;
        playerPosition = initialPlayerPosition[0];
        velocityX = 0;
        velocityY = 0;
        facing = 1;
    }

    //! \brief Reset the player to the main characteristics.
    public void reset() {
        lifeBox.width = hitBox.width;
        startTimeAttack.clear();
        MAX_ATTACK_NUMBER = 4;
        attackNumberCounter = MAX_ATTACK_NUMBER;
        life = 100;
        lastLife = life;
        hitPower = -20;
        x = initialPlayerPosition[0];
        y = initialPlayerPosition[1];
        camera.setCamera(0);
        playerPosition = initialPlayerPosition[0];
        velocityX = 0;
        velocityY = 0;
        facing = 1;
    }

    //! \brief Return the position on the Y-axis.
    public float getY() {
        return y;
    }

    //! \brief Set the position on the Y-axis.
    public void setY(float y) {
        this.y = y;
    }

    //! \brief Return the velocity on the Y-axis.
    public float getVelocityY() {
        return velocityY;
    }

    //! \brief Set the velocity on the Y-axis.
    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    //! \brief Return the \ref attackBox.
    public Rectangle getAttackBox() {
        return attackBox;
    }

    //! \brief Set the \ref falling flag.
    public void setFalling(boolean b) {
        falling = b;
    }

    //! \brief Return the \ref falling flag.
    public boolean isFalling() {
        return falling;
    }

    //! \brief Return the player's relative position.
    public int getPosition() {
        return playerPosition;
    }

    //! \brief Increase the available number of attacks.
    public void increaseNumberAttack() {
        MAX_ATTACK_NUMBER++;
        attackNumberCounter++;
    }
}
