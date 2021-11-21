package com.dg.supermariobros.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;

public class Goku extends Sprite {
    public enum State {
        FALLING,
        JUMPING,
        STANDING,
        RUNNING,
        GROWING
    }
    public State currentState;
    public State previousState;

    public World world;
    public Body b2dBody;
    private TextureRegion gokuStand;

    private Animation<TextureRegion> gokuRun;
    private Animation<TextureRegion> gokuJump;
    private float stateTimer;
    private boolean runningRight;

    private TextureRegion bigGokuStand;
    private TextureRegion bigGokuJump;
    private TextureRegion bigGokuRun;
    private Animation<TextureRegion> growGoku;
    private boolean isBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigGoku;
    private boolean timeToRedefineGoku;

    public Goku(PlayScreen screen) {

        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        // Run Animations
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goku1"), i * 19, 0, 19, 19));
        }
        gokuRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        bigGokuRun = new TextureRegion(screen.getAtlas().findRegion("big"), 383, 330, 33, 69);

        // Jump Animations
        for(int i = 6; i < 7; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goku1"), i * 19, 0, 19, 19));
        }
        gokuJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        bigGokuJump = new TextureRegion(screen.getAtlas().findRegion("big"), 94, 77, 33, 58);

        // Growing Animation
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big"), 9, 257, 30, 60));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big"), 7, 12, 33, 60));
        growGoku = new Animation<TextureRegion>(0.45f, frames);

        gokuStand = new TextureRegion(screen.getAtlas().findRegion("goku1"), 0, 0, 19, 19);
        bigGokuStand = new TextureRegion(screen.getAtlas().findRegion("big"), 7, 12, 33, 60);

        defineGoku();
        setBounds(0, 0, 17/ MainGame.PPM, 17/ MainGame.PPM);
        setRegion(gokuStand);
    }

    private void defineBigGoku() {
        Vector2 currentPosition = b2dBody.getPosition();
        world.destroyBody(b2dBody);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition.add(0, 6.5f / MainGame.PPM));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.GOKU_BIT;
        fixtureDef.filter.maskBits =
                MainGame.GROUND_BIT
                        | MainGame.BRICK_BIT
                        | MainGame.COIN_BIT
                        | MainGame.ENEMY_BIT
                        | MainGame.OBJECT_BIT
                        | MainGame.ENEMY_HEAD_BIT
                        | MainGame.ITEM_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / MainGame.PPM));
        b2dBody.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 7 / MainGame.PPM),
                new Vector2(2 / MainGame.PPM, 7 / MainGame.PPM));
        fixtureDef.filter.categoryBits = MainGame.GOKU_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2dBody.createFixture(fixtureDef).setUserData(this);
        timeToDefineBigGoku = false;
    }

    public void update(float dt) {
        if(isBig)
            setPosition(b2dBody.getPosition().x - getWidth() /2,
                    b2dBody.getPosition().y - getHeight() / 2 - 9 / MainGame.PPM);
        else
            setPosition(b2dBody.getPosition().x - getWidth() / 2, b2dBody.getPosition().y - getHeight() /2);

        setRegion(getFrame(dt));

        if(timeToDefineBigGoku) {
            defineBigGoku();
        }
        if(timeToRedefineGoku) {
            redefineGoku();
        }
    }

    private void redefineGoku() {
        Vector2 currentPosition = b2dBody.getPosition();
        world.destroyBody(b2dBody);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currentPosition);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7.5f / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.GOKU_BIT;
        fixtureDef.filter.maskBits =
                MainGame.GROUND_BIT
                        | MainGame.BRICK_BIT
                        | MainGame.COIN_BIT
                        | MainGame.ENEMY_BIT
                        | MainGame.OBJECT_BIT
                        | MainGame.ENEMY_HEAD_BIT
                        | MainGame.ITEM_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 7 / MainGame.PPM),
                new Vector2(2 / MainGame.PPM, 7 / MainGame.PPM));
        fixtureDef.filter.categoryBits = MainGame.GOKU_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2dBody.createFixture(fixtureDef).setUserData(this);
        timeToRedefineGoku = false;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case GROWING:
                region = growGoku.getKeyFrame(stateTimer);
                if(growGoku.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = isBig ? bigGokuJump : gokuJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = isBig ? bigGokuRun : gokuRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = isBig ? bigGokuStand : gokuStand;
                break;
        }

        if((b2dBody.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if((b2dBody.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(runGrowAnimation)
            return State.GROWING;
        else if (b2dBody.getLinearVelocity().y > 0 || (b2dBody.getLinearVelocity().y < 0
                && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2dBody.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2dBody.getLinearVelocity().x != 0)
            return State.RUNNING;
        return State.STANDING;
    }

    public void grow() {
        if(!isBig) {
            runGrowAnimation = true;
            isBig = true;
            timeToDefineBigGoku = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
            new SoundManager().getAssetManager().get("audio/sounds/powerup.wav", Sound.class).play();
        } else {
            Hud.addScore(100);
        }
    }

    public void defineGoku() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7.5f / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.GOKU_BIT;
        fixtureDef.filter.maskBits =
                MainGame.GROUND_BIT
                        | MainGame.BRICK_BIT
                        | MainGame.COIN_BIT
                        | MainGame.ENEMY_BIT
                        | MainGame.OBJECT_BIT
                        | MainGame.ENEMY_HEAD_BIT
                        | MainGame.ITEM_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 7 / MainGame.PPM),
                new Vector2(2 / MainGame.PPM, 7 / MainGame.PPM));
        fixtureDef.filter.categoryBits = MainGame.GOKU_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2dBody.createFixture(fixtureDef).setUserData(this);
    }

    public boolean isBig() {
        return isBig;
    }

    public void hit() {
        if(isBig) {
            isBig = false;
            timeToRedefineGoku = true;
            setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            new SoundManager().getAssetManager().get("audio/sounds/powerdown.wav", Sound.class).play();
        } else {
            new SoundManager().getAssetManager().get("audio/sounds/die.wav", Sound.class).play();
        }
    }
}
