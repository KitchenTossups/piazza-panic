package com.eng1.base;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * Extends functionality of the LibGDX Actor class.
 * by adding support for textures/animation,
 * collision polygons, movement, world boundaries, and camera scrolling.
 * Most game objects should extend this class; lists of extensions can be retrieved by stage and class name.
 *
 * @author Lee Stemkoski
 * @author Liam Burnand (modified code)
 * @see #Actor
 */
@SuppressWarnings("unused")
public class BaseActor extends Actor {
    private Animation<TextureRegion> animation;
    private float elapsedTime, loci;
    private boolean animationPaused;

    private Rectangle boundaryRectangle;

    private Polygon boundaryPolygon;

    public BaseActor(float x, float y, Stage s) {
        // call constructor from Actor class
        super();

        // perform additional initialization tasks
        this.setPosition(x, y);
        s.addActor(this);

        // initialize animation data
        this.animation = null;
        this.elapsedTime = 0;
        this.animationPaused = false;

        this.boundaryPolygon = null;
        this.boundaryRectangle = null;
        this.loci = -1;
    }

    public BaseActor(float x, float y, Stage s, float loci) {
        // call constructor from Actor class
        super();

        // initialize animation data
        this.animation = null;
        this.elapsedTime = 0;

        if (loci < 0)
            return;

        // perform additional initialization tasks
        this.setPosition(x, y);
        s.addActor(this);

        this.boundaryPolygon = null;
        this.boundaryRectangle = null;

        this.loci = loci;
    }

    /**
     * Sets the animation used when rendering this actor; also sets actor size.
     *
     * @param anim animation that will be drawn when actor is rendered
     */
    public void setAnimation(Animation<TextureRegion> anim) {
        this.animation = anim;
        TextureRegion tr = this.animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        this.setSize(w, h);
        this.setOrigin(w / 2, h / 2);

        if (this.boundaryPolygon == null || this.boundaryRectangle == null)
            this.setBoundaryRectangle();
    }

    public void setAnimation(Animation<TextureRegion> anim, float w, float h) {
        this.animation = anim;
        this.setSize(w, h);
        this.setOrigin(w / 2, h / 2);

        if (this.boundaryPolygon == null || this.boundaryRectangle == null)
            this.setBoundaryRectangle();
    }

    /**
     * Creates an animation from images stored in separate files.
     *
     * @param fileNames     array of names of files containing animation images
     * @param frameDuration how long each frame should be displayed
     * @param loop          should the animation loop
     */
    public void loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) {
        Array<TextureRegion> textureArray = new Array<>();

        for (String fileName : fileNames) {
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            this.setAnimation(anim);

    }

    public void loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop, float w, float h) {
        Array<TextureRegion> textureArray = new Array<>();

        for (String fileName : fileNames) {
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            this.setAnimation(anim, w, h);
    }

    public void loadAnimationFromTextureRegion(Array<TextureRegion> array, float frameDuration, float w, float h) {
        Animation<TextureRegion> anim = new Animation<>(frameDuration, array);

        anim.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            this.setAnimation(anim, w, h);
    }

    /**
     * Creates an animation from a spritesheet: a rectangular grid of images stored in a single file.
     *
     * @param fileName      name of file containing spritesheet
     * @param rows          number of rows of images in spritesheet
     * @param cols          number of columns of images in spritesheet
     * @param frameDuration how long each frame should be displayed
     * @param loop          should the animation loop
     */
    public void loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);

        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            this.setAnimation(anim);

    }

    /**
     * Convenience method for creating a 1-frame animation from a single texture.
     * <p>
     * Original credit - <a href="https://github.com/mariorez/libgdx-maze-runman">mariorez</a>
     *
     * @param fileName names of image file
     */
    public void loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        this.loadAnimationFromFiles(fileNames, 1, true);
    }

    public void loadTexture(String fileName, float w, float h) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        this.loadAnimationFromFiles(fileNames, 1, true, w, h);
    }

    public void loadTexture(String[] fileNames, float w, float h) {
        this.loadAnimationFromFiles(fileNames, 1, false, w, h);
    }

    public void setTexture(Texture texture) {
        Array<TextureRegion> textureArray = new Array<>();
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        textureArray.add(new TextureRegion(texture));

        Animation<TextureRegion> anim = new Animation<>(1, textureArray);

        anim.setPlayMode(Animation.PlayMode.LOOP);

        if (this.animation == null)
            this.setAnimation(anim);
    }

    public void setTexture(TextureRegion textureRegion) {
        Array<TextureRegion> textureArray = new Array<>();
        textureArray.add(textureRegion);

        Animation<TextureRegion> anim = new Animation<>(1, textureArray);

        anim.setPlayMode(Animation.PlayMode.LOOP);

        if (this.animation == null)
            this.setAnimation(anim);
    }

    /**
     * Set the pause state of the animation.
     *
     * @param pause true to pause animation, false to resume animation
     */
    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }

    /**
     * Set rectangular-shaped collision polygon.
     * This method is automatically called when animation is set,
     * provided that the current boundary polygon is null.
     *
     * @see #setAnimation
     */
    public void setBoundaryRectangle() {
        float w = this.getWidth();
        float h = this.getHeight();

        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        this.boundaryPolygon = new Polygon(vertices);

        this.boundaryRectangle = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public Rectangle getBoundaryRectangle() {
        this.boundaryRectangle.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        return this.boundaryRectangle;
    }

    public Rectangle getLociRectangle() {
        if (this.loci == -1)
            return null;
        return this.boundaryRectangle.set(this.getX() - this.loci, this.getY() - this.loci, this.getWidth() + (2 * this.loci), this.getHeight() + (2 * this.loci));
    }

    /**
     * Processes all Actions and related code for this object;
     * automatically called by act method in Stage class.
     *
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float dt) {
        super.act(dt);

        if (!animationPaused)
            elapsedTime += dt;
    }

    /**
     * Draws current frame of animation; automatically called by draw method in Stage class. <br>
     * If color has been set, image will be tinted by that color. <br>
     * If no animation has been set or object is invisible, nothing will be drawn.
     *
     * @param batch       (supplied by Stage draw method)
     * @param parentAlpha (supplied by Stage draw method)
     * @see #setColor
     * @see #setVisible
     */
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // apply color tint effect
        Color c = this.getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        if (this.animation != null && this.isVisible())
            batch.draw(this.animation.getKeyFrame(this.elapsedTime),
                    this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
                    this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
    }
}