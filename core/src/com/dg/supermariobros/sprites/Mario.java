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
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;

public class Mario extends Sprite {
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
    private TextureRegion marioStand;

    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;

    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion bigMarioRun;
    private Animation<TextureRegion> growMario;
    private boolean isBig;
    private boolean runGrowAnimation;

    public Mario (PlayScreen screen) {

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
        marioRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        bigMarioRun = new TextureRegion(screen.getAtlas().findRegion("big"), 383, 330, 33, 69);

        // Jump Animations
        for(int i = 6; i < 7; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goku1"), i * 19, 0, 19, 19));
        }
        marioJump = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big"), 94, 77, 33, 58);

        // Growing Animation
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big"), 9, 257, 30, 60));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big"), 7, 12, 33, 60));
        growMario = new Animation<TextureRegion>(0.45f, frames);

        marioStand = new TextureRegion(screen.getAtlas().findRegion("goku1"), 0, 0, 19, 19);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big"), 7, 12, 33, 60);

        defineMario();
        setBounds(0, 0, 17/ SuperMarioBros.PPM, 17/ SuperMarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt) {
        setPosition(b2dBody.getPosition().x - getWidth() /2, b2dBody.getPosition().y - getHeight() /2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case GROWING:
                region = growMario.getKeyFrame(stateTimer);
                if(growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = isBig ? bigMarioJump : marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = isBig ? bigMarioRun : marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = isBig ? bigMarioStand : marioStand;
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
        runGrowAnimation = true;
        isBig = true;
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        new SoundManager().getAssetManager().get("audio/sounds/powerup.wav", Sound.class).play();
    }

    public void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / SuperMarioBros.PPM, 32 / SuperMarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / SuperMarioBros.PPM);
        fixtureDef.filter.categoryBits = SuperMarioBros.MARIO_BIT;
        fixtureDef.filter.maskBits =
                SuperMarioBros.GROUND_BIT
                        | SuperMarioBros.BRICK_BIT
                        | SuperMarioBros.COIN_BIT
                        | SuperMarioBros.ENEMY_BIT
                        | SuperMarioBros.OBJECT_BIT
                        | SuperMarioBros.ENEMY_HEAD_BIT
                        | SuperMarioBros.ITEM_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / SuperMarioBros.PPM, 7 / SuperMarioBros.PPM),
                new Vector2(2 / SuperMarioBros.PPM, 7 / SuperMarioBros.PPM));
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;
        b2dBody.createFixture(fixtureDef).setUserData("head");
    }
}
