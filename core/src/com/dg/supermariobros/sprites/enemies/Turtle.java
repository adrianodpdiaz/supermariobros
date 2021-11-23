package com.dg.supermariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sprites.Goku;

public class Turtle extends Enemy {
    public static final int KICK_LEFT = -2;
    public static final int KICK_RIGHT = 2;
    public enum State {WALKING, MOVING_SHELL, STANDING_SHELL, DEAD}
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;
    private float deadRotationDegrees;
    private boolean destroyed;

    public Turtle(PlayScreen screen, MapObject object) {
        super(screen, object);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("mario"), 339, 38, 16, 24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("mario"), 355, 38, 16, 24));
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;

        shell = new TextureRegion(screen.getAtlas().findRegion("mario"), 403, 38, 16, 24);

        deadRotationDegrees = 0;

        setBounds(getX(), getY(), 15 / MainGame.PPM, 22 / MainGame.PPM);
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
        vertex[0] = new Vector2(-5, 8f).scl(1 / MainGame.PPM);
        vertex[1] = new Vector2(5, 8f).scl(1 / MainGame.PPM);
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

        if(currentState == State.DEAD) {
            deadRotationDegrees += 3;
            rotate(deadRotationDegrees);
            if(stateTime > 5 && !destroyed) {
                world.destroyBody(b2dBody);
                destroyed = true;
            }
        } else {
            b2dBody.setLinearVelocity(velocity);
        }
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

    @Override
    public void onEnemyHit(Enemy enemy) {
        if(enemy instanceof Turtle) {
            if(((Turtle) enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
                killed();
            } else if(currentState == State.MOVING_SHELL && ((Turtle) enemy).currentState == State.WALKING) {
                return;
            } else {
                reverseVelocity(true, false);
            }
        } else if(currentState != State.MOVING_SHELL) {
            reverseVelocity(true, false);
        }
    }

    public void killed() {
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MainGame.NOTHING_BIT;

        for(Fixture fixture : b2dBody.getFixtureList())
            fixture.setFilterData(filter);
        b2dBody.applyLinearImpulse(new Vector2(0, 5f), b2dBody.getWorldCenter(), false);
    }

    @Override
    public void draw(Batch batch) {
        if(!destroyed)
            super.draw(batch);
    }
}
