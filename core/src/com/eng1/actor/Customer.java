package com.eng1.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.eng1.non_actor.Recipe;
import com.eng1.base.BaseActor;

import java.util.Date;
import java.util.Random;

public class Customer extends BaseActor {

    private final Recipe order;
    private final long orderPlaced, customerNumber;
    private boolean moving;
    private Direction direction;

    final Animation<TextureRegion> north, south, east, west;

    public Customer(float x, float y, Stage s, Recipe order, long customerNumber, long delay) {
        super(x, y, s);
        this.order = order;

        this.orderPlaced = new Date().getTime() + 11000 + delay;

        this.setTouchable(Touchable.enabled);

        this.customerNumber = customerNumber;

        Texture texture = new Texture(Gdx.files.internal("spritemap/Customer" + new Random().nextInt(5) + ".png"), true);

        int rows = 4;
        int cols = 4;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();

        float frameDuration = 0.2f;

        for (int c = 0; c < cols; c++)
            textureArray.add(temp[0][c]);
        south = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[2][c]);
        west = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[3][c]);
        east = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[1][c]);
        north = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        setAnimation(east);
    }

    public Recipe getOrder() {
        return this.order;
    }

    public long getOrderPlaced() {
        return this.orderPlaced;
    }

    public long getCustomerNumber() {
        return customerNumber;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public enum Direction {
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        // pause animation when character not moving
        if (!moving) {
            setAnimationPaused(true);
        } else {
            setAnimationPaused(false);

            switch (this.direction) {
                case NORTH:
                    setAnimation(north);
                    break;
                case WEST:
                    setAnimation(west);
                    break;
                case EAST:
                    setAnimation(east);
                    break;
                case SOUTH:
                    setAnimation(south);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "order=" + order.toString() +
                ", orderPlaced=" + orderPlaced +
                ", customerNumber=" + customerNumber +
                '}';
    }
}
