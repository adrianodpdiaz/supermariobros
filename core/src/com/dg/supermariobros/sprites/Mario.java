package com.dg.supermariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.screens.PlayScreen;

public class Mario extends Sprite {
    public enum State {
        FALLING,
        JUMPING,
        STANDING,
        RUNNING
    };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2dBody;
    private TextureRegion marioStand;

    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;



    public Mario (World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("goku1"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 19, 0, 20, 20));
        }
        marioRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for(int i = 6; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(), i * 19, 0, 20, 20));
        }
        marioJump = new Animation<TextureRegion>(0.1f, frames);


        /*
        marioStand = new TextureRegion(getTexture(), 32, 269, 38, 54);
        setBounds(0, 0, 19 / SuperMarioBros.PPM, 27 / SuperMarioBros.PPM);
        setRegion(marioStand);
         */

        marioStand = new TextureRegion(getTexture(), 0, 0, 20, 20);
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
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
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
        if (b2dBody.getLinearVelocity().y > 0 || (b2dBody.getLinearVelocity().y < 0
                && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2dBody.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2dBody.getLinearVelocity().x != 0)
            return State.RUNNING;
        return State.STANDING;
    }

    public void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / SuperMarioBros.PPM, 32 / SuperMarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();


        shape.setRadius(8 / SuperMarioBros.PPM);

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef);
    }
}
