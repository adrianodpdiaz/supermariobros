package com.dg.supermariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sprites.Goku;

public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;
    public enum State {WALKING, MOVING_SHELL, STANDING_SHELL}
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;

    public Turtle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("mario"), 339, 38, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("mario"), 355, 38, 16, 24));
        shell = new TextureRegion(screen.getAtlas().findRegion("mario"), 403, 38, 16, 24);
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;

        setBounds(getX(), getY(), 16 / MainGame.PPM, 24 / MainGame.PPM);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.2f / MainGame.PPM);
        fixtureDef.filter.categoryBits = MainGame.ENEMY_BIT;
        fixtureDef.filter.maskBits =
                MainGame.GROUND_BIT
                        | MainGame.BRICK_BIT
                        | MainGame.COIN_BIT
                        | MainGame.ENEMY_BIT
                        | MainGame.OBJECT_BIT
                        | MainGame.GOKU_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef).setUserData(this);

        // Creating the head
        PolygonShape head = new PolygonShape();
        Vector2[] vertex = new Vector2[4];
        vertex[0] = new Vector2(-5, 8).scl(1 / MainGame.PPM);
        vertex[1] = new Vector2(5, 8).scl(1 / MainGame.PPM);
        vertex[2] = new Vector2(-3, 5).scl(1 / MainGame.PPM);
        vertex[3] = new Vector2(3, 5).scl(1 / MainGame.PPM);
        head.set(vertex);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MainGame.ENEMY_HEAD_BIT;
        b2dBody.createFixture(fixtureDef).setUserData(this);
    }

    public TextureRegion getFrame(float dt){

        TextureRegion region;

        switch (currentState){
            case MOVING_SHELL:
            case STANDING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if(velocity.x > 0 && region.isFlipX() == false){
            region.flip(true, false);
        }
        if(velocity.x < 0 && region.isFlipX() == true){
            region.flip(true, false);
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if(currentState == State.STANDING_SHELL && stateTime > 10){
            currentState = State.WALKING;
            velocity.x = 1;
            System.out.println("WAKE UP SHELL");
        }

        setPosition(b2dBody.getPosition().x - getWidth() / 2, b2dBody.getPosition().y - 8 /MainGame.PPM);
        b2dBody.setLinearVelocity(velocity);
    }

    @Override
    public void hitOnHead(Goku goku) {
        if(currentState == State.STANDING_SHELL) {
            kick(goku.getX() <= this.getX() ? KICK_RIGHT : KICK_LEFT);
        }
        else {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
    }

    public void kick(int direction){
        velocity.x = direction;
        currentState = State.MOVING_SHELL;
    }

    public State getCurrentState() {
        return currentState;
    }
}
