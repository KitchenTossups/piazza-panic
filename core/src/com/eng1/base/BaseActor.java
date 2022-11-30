package com.eng1.base;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Polygon;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;

import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Extends functionality of the LibGDX Actor class.
 * by adding support for textures/animation,
 * collision polygons, movement, world boundaries, and camera scrolling.
 * Most game objects should extend this class; lists of extensions can be retrieved by stage and class name.
 *
 * @author Lee Stemkoski
 * @see #Actor
 */
@SuppressWarnings("unused")
public class BaseActor extends Actor {
    private Animation<TextureRegion> animation;
    private boolean animationPaused;

    private final Vector2 velocityVec, accelerationVec;
    private float acceleration, maxSpeed, deceleration, elapsedTime, loci;

    private Rectangle boundaryRectangle;

    private Polygon boundaryPolygon;

    // stores size of game world for all actors
    private static Rectangle worldBounds;

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

        // initialize physics data
        this.velocityVec = new Vector2(0, 0);
        this.accelerationVec = new Vector2(0, 0);
        this.acceleration = 0;
        this.maxSpeed = 1000;
        this.deceleration = 0;

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
        this.animationPaused = false;

        // initialize physics data
        this.velocityVec = new Vector2(0, 0);
        this.accelerationVec = new Vector2(0, 0);
        this.acceleration = 0;
        this.maxSpeed = 1000;
        this.deceleration = 0;

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
     * Align center of actor at given position coordinates.
     *
     * @param x x-coordinate to center at
     * @param y y-coordinate to center at
     */
    public void centerAtPosition(float x, float y) {
        this.setPosition(x - this.getWidth() / 2, y - this.getHeight() / 2);
    }

    /**
     * Repositions this BaseActor so its center is aligned
     * with center of other BaseActor. Useful when one BaseActor spawns another.
     *
     * @param other BaseActor to align this BaseActor with
     */
    public void centerAtActor(BaseActor other) {
        this.centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }

    // ----------------------------------------------
    // Animation methods
    // ----------------------------------------------

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
        TextureRegion tr = this.animation.getKeyFrame(0);
        this.setSize(w, h);
        this.setOrigin(w / 2, h / 2);

        if (this.boundaryPolygon == null || this.boundaryRectangle == null)
            this.setBoundaryRectangle();
    }

//    public abstract Rectangle getBounds();

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
        this.animationPaused = pause;
    }

    /**
     * Checks if animation is complete: if play mode is normal (not looping)
     * and elapsed time is greater than time corresponding to last frame.
     *
     * @return boolean
     */
    public boolean isAnimationFinished() {
        return this.animation.isAnimationFinished(this.elapsedTime);
    }

    /**
     * Sets the opacity of this actor.
     *
     * @param opacity value from 0 (transparent) to 1 (opaque)
     */
    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }

//     ----------------------------------------------
//     physics/motion methods
//     ----------------------------------------------

    /**
     * Set acceleration of this object.
     *
     * @param acc Acceleration in (pixels/second) per second.
     */
    public void setAcceleration(float acc) {
        this.acceleration = acc;
    }

    /**
     * Set deceleration of this object.
     * Deceleration is only applied when object is not accelerating.
     *
     * @param dec Deceleration in (pixels/second) per second.
     */
    public void setDeceleration(float dec) {
        this.deceleration = dec;
    }

    /**
     * Set maximum speed of this object.
     *
     * @param ms Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeed(float ms) {
        this.maxSpeed = ms;
    }

    /**
     * Set the speed of movement (in pixels/second) in current direction.
     * If current speed is zero (direction is undefined), direction will be set to 0 degrees.
     *
     * @param speed of movement (pixels/second)
     */
    public void setSpeed(float speed) {
        // if length is zero, then assume motion angle is zero degrees
        if (this.velocityVec.len() == 0)
            this.velocityVec.set(speed, 0);
        else
            this.velocityVec.setLength(speed);
    }

    /**
     * Calculates the speed of movement (in pixels/second).
     *
     * @return speed of movement (pixels/second)
     */
    public float getSpeed() {
        return this.velocityVec.len();
    }

    /**
     * Determines if this object is moving (if speed is greater than zero).
     *
     * @return false when speed is zero, true otherwise
     */
    public boolean isMoving() {
        return (this.getSpeed() > 0);
    }

    /**
     * Sets the angle of motion (in degrees).
     * If current speed is zero, this will have no effect.
     *
     * @param angle of motion (degrees)
     */
    public void setMotionAngle(float angle) {
        this.velocityVec.setAngleDeg(angle);
    }

    /**
     * Get the angle of motion (in degrees), calculated from the velocity vector.
     * <br>
     * To align actor image angle with motion angle, use <code>setRotation( getMotionAngle() )</code>.
     *
     * @return angle of motion (degrees)
     */
    public float getMotionAngle() {
        return this.velocityVec.angleDeg();
    }

    /**
     * Update accelerate vector by angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @param angle Angle (degrees) in which to accelerate.
     * @see #acceleration
     * @see #applyPhysics
     */
    public void accelerateAtAngle(float angle) {
        this.accelerationVec.add(new Vector2(this.acceleration, 0).setAngleDeg(angle));
    }

    /**
     * Update accelerate vector by current rotation angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @see #acceleration
     * @see #applyPhysics
     */
    public void accelerateForward() {
        this.accelerateAtAngle(this.getRotation());
    }

    /**
     * Adjust velocity vector based on acceleration vector,
     * then adjust position based on velocity vector. <br>
     * If not accelerating, deceleration value is applied. <br>
     * Speed is limited by maxSpeed value. <br>
     * Acceleration vector reset to (0,0) at end of method. <br>
     *
     * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
     * @see #acceleration
     * @see #deceleration
     * @see #maxSpeed
     */
    public void applyPhysics(float dt) {
        // apply acceleration
        this.velocityVec.add(this.accelerationVec.x * dt, this.accelerationVec.y * dt);

        float speed = this.getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (this.accelerationVec.len() == 0)
            speed -= this.deceleration * dt;

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, this.maxSpeed);

        // update velocity
        this.setSpeed(speed);

        // apply velocity
        this.moveBy(this.velocityVec.x * dt, this.velocityVec.y * dt);

        // reset acceleration
        this.accelerationVec.set(0, 0);
    }

    // ----------------------------------------------
    // Collision polygon methods
    // ----------------------------------------------

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

    /**
     * Replace default (rectangle) collision polygon with an n-sided polygon. <br>
     * Vertices of polygon lie on the ellipse contained within bounding rectangle.
     * Note: one vertex will be located at point (0,width);
     * a 4-sided polygon will appear in the orientation of a diamond.
     *
     * @param numSides number of sides of the collision polygon
     */
    public void setBoundaryPolygon(int numSides) {
        float w = this.getWidth();
        float h = this.getHeight();

        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        this.boundaryPolygon = new Polygon(vertices);

    }

    /**
     * Returns bounding polygon for this BaseActor, adjusted by Actor's current position and rotation.
     *
     * @return bounding polygon for this BaseActor
     */
    public Polygon getBoundaryPolygon() {
        this.boundaryPolygon.setPosition(this.getX(), this.getY());
        this.boundaryPolygon.setOrigin(this.getOriginX(), this.getOriginY());
        this.boundaryPolygon.setRotation(this.getRotation());
        this.boundaryPolygon.setScale(this.getScaleX(), this.getScaleY());
        return this.boundaryPolygon;
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
     * Determine if this BaseActor overlaps other BaseActor (according to collision polygons).
     *
     * @param other BaseActor to check for overlap
     * @return true if collision polygons of this and other BaseActor overlap
     * //     * @see #setCollisionRectangle
     * //     * @see #setCollisionPolygon
     */
    public boolean overlaps(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;

        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    /**
     * Implement a "solid"-like behavior:
     * when there is overlap, move this BaseActor away from other BaseActor
     * along minimum translation vector until there is no overlap.
     *
     * @param other BaseActor to check for overlap
     */
    public void preventOverlap(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();

        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return;

        MinimumTranslationVector mtv = new MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

        if (!polygonOverlap)
            return;

        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
    }

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param width  width of world
     * @param height height of world
     */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param baseActor whose size determines the world bounds (typically a background image)
     */
    public static void setWorldBounds(BaseActor baseActor) {
        setWorldBounds(baseActor.getWidth(), baseActor.getHeight());
    }

    /**
     * If an edge of an object moves past the world bounds,
     * adjust its position to keep it completely on screen.
     */
    public void boundToWorld() {
        if (this.getX() < 0)
            this.setX(0);
        if (this.getX() + this.getWidth() > worldBounds.width)
            this.setX(worldBounds.width - this.getWidth());
        if (this.getY() < 0)
            this.setY(0);
        if (this.getY() + this.getHeight() > worldBounds.height)
            this.setY(worldBounds.height - this.getHeight());
    }

    /**
     * Center camera on this object, while keeping camera's range of view
     * (determined by screen size) completely within world bounds.
     */
    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();

        // center camera on actor
        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2);
        cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2);
        cam.update();
    }

    // ----------------------------------------------
    // Instance list methods
    // ----------------------------------------------

    /**
     * Retrieves a list of all instances of the object from the given stage with the given class name
     * or whose class extends the class with the given name.
     * If no instances exist, returns an empty list.
     * Useful when coding interactions between different types of game objects in update method.
     *
     * @param stage     Stage containing BaseActor instances
     * @param className name of a class that extends the BaseActor class
     * @return list of instances of the object in stage which extend with the given class name
     */
    public static ArrayList<BaseActor> getList(Stage stage, String className) {
        ArrayList<BaseActor> list = new ArrayList<>();

        Class<?> theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (Exception error) {
            error.printStackTrace();
        }

        for (Actor a : stage.getActors()) {
            assert theClass != null;
            if (theClass.isInstance(a))
                list.add((BaseActor) a);
        }

        return list;
    }

    /**
     * Returns number of instances of a given class (that extends BaseActor).
     *
     * @param className name of a class that extends the BaseActor class
     * @return number of instances of the class
     */
    public static int count(Stage stage, String className) {
        return getList(stage, className).size();
    }

    // ----------------------------------------------
    // Actor methods: act and draw
    // ----------------------------------------------

    /**
     * Processes all Actions and related code for this object;
     * automatically called by act method in Stage class.
     *
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    public void act(float dt) {
        super.act(dt);

        if (!this.animationPaused)
            this.elapsedTime += dt;
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