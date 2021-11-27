package com.dg.supermariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;
import com.dg.supermariobros.sprites.enemies.Enemy;
import com.dg.supermariobros.sprites.enemies.Turtle;

public class Goku extends Sprite {

    public enum State {
        FALLING,
        JUMPING,
        STANDING,
        RUNNING,
        GROWING,
        DEAD,
        WINNER
    }
    public State currentState;
    public State previousState;

    private float time;

    public World world;
    public PlayScreen screen;
    public Body b2dBody;
    private TextureRegion gokuStand;
    private TextureRegion gokuDead;

    private Animation<TextureRegion> gokuRun;
    private TextureRegion gokuJump;
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
    private boolean gokuIsDead;

    private boolean winner;
    private boolean runToCastle;
    private boolean destroyTexture;

    public Goku(PlayScreen screen) {

        this.world = screen.getWorld();
        this.screen = screen;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        winner = false;
        runToCastle = false;
        destroyTexture = false;

        // Run Animations
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goku1"), i * 19, 0, 19, 19));
        }
        gokuRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        bigGokuRun = new TextureRegion(screen.getAtlas().findRegion("big"), 383, 330, 33, 69);

        // Jump Animations
        gokuJump = new TextureRegion(screen.getAtlas().findRegion("goku1"), 19, 0, 19, 19);
        bigGokuJump = new TextureRegion(screen.getAtlas().findRegion("big"), 94, 77, 33, 58);

        // Dead Texture Region
        gokuDead = new TextureRegion(screen.getAtlas().findRegion("goku1"), 57, 23, 19, 15);

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

    public void defineGoku() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7.5f / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.GOKU_BIT;
        fixtureDef.filter.maskBits = collisionProfile();

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 7 / MainGame.PPM),
                new Vector2(2 / MainGame.PPM, 7 / MainGame.PPM));
        fixtureDef.filter.categoryBits = MainGame.GOKU_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2dBody.setGravityScale(1.5f);
        b2dBody.createFixture(fixtureDef).setUserData(this);
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
        fixtureDef.filter.maskBits = collisionProfile();

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

        b2dBody.setGravityScale(1.5f);
        b2dBody.createFixture(fixtureDef).setUserData(this);
        timeToDefineBigGoku = false;
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
        fixtureDef.filter.maskBits = collisionProfile();

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 7 / MainGame.PPM),
                new Vector2(2 / MainGame.PPM, 7 / MainGame.PPM));
        fixtureDef.filter.categoryBits = MainGame.GOKU_HEAD_BIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        b2dBody.setGravityScale(1.5f);
        b2dBody.createFixture(fixtureDef).setUserData(this);
        timeToRedefineGoku = false;
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
        if(runToCastle) {
            time += Gdx.graphics.getDeltaTime();
            b2dBody.setLinearVelocity(new Vector2(0.7f, b2dBody.getLinearVelocity().y));

            if(time > 2.3f) {
                b2dBody.setLinearVelocity(new Vector2(0,0));
            }
            if(time > 4f) {
                screen.setCongratulations(true);
                world.destroyBody(b2dBody);
                destroyTexture = true;
            }
        }
    }

    public boolean isBig() {
        return isBig;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = gokuDead;
                break;
            case GROWING:
                region = growGoku.getKeyFrame(stateTimer);
                if(growGoku.isAnimationFinished(stateTimer))
                    runGrowAnimation = false;
                break;
            case JUMPING:
                region = isBig ? bigGokuJump : gokuJump;
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
        if(gokuIsDead)
            return State.DEAD;
        else if(runGrowAnimation)
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

    public float getStateTimer() {
        return stateTimer;
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

    public void hit(Enemy enemy) {
        if(enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL) {
            ((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT : Turtle.KICK_LEFT);
        } else {
            if(isBig) {
                isBig = false;
                timeToRedefineGoku = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                new SoundManager().getAssetManager().get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                screen.getMusic().stop();
                new SoundManager().getAssetManager().get("audio/sounds/die.wav", Sound.class).play();

                gokuIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = MainGame.NOTHING_BIT;
                for(Fixture fixture : b2dBody.getFixtureList())
                    fixture.setFilterData(filter);
                b2dBody.applyLinearImpulse(new Vector2(0, 4f), b2dBody.getWorldCenter(), true);
            }
        }
    }

    public void win() {
        screen.getMusic().stop();
        new SoundManager().getAssetManager().get("audio/sounds/stage-clear.wav", Sound.class).play();

        b2dBody.setGravityScale(0.05f);
        b2dBody.setLinearVelocity(new Vector2(0, 0));
        winner = true;
        b2dBody.applyForce(new Vector2(0, -1f), b2dBody.getWorldCenter(), true);
    }

    public void goToTheCastle() {
        b2dBody.setGravityScale(1f);
        runToCastle = true;

        Filter filter = new Filter();
        filter.maskBits =
                MainGame.GROUND_BIT
                        | MainGame.OBJECT_BIT
                        | MainGame.INVISIBLE_BIT;
        for(Fixture fixture : b2dBody.getFixtureList())
            fixture.setFilterData(filter);
    }

    public boolean getWinner() {
        return winner;
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyTexture)
            super.draw(batch);
    }
    
    private short collisionProfile() {
        return MainGame.GROUND_BIT
                | MainGame.BRICK_BIT
                | MainGame.COIN_BIT
                | MainGame.ENEMY_BIT
                | MainGame.OBJECT_BIT
                | MainGame.ENEMY_HEAD_BIT
                | MainGame.ITEM_BIT
                | MainGame.HOLE_BIT
                | MainGame.FLAGPOLE_BIT
                | MainGame.WALL_BIT
                | MainGame.INVISIBLE_BIT;
    }
}
