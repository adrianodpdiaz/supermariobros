package com.dg.supermariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    private int packAdjustmentX = 226;
    private int packAdjustmentY = 11;

    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mario"), i * 16 + packAdjustmentX, packAdjustmentY, 17, 17));
        }
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MainGame.PPM, 16 / MainGame.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2dBody);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("mario"), 33 + packAdjustmentX, packAdjustmentY, 16, 16));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2dBody.setLinearVelocity(velocity);
            setPosition(b2dBody.getPosition().x - getWidth() / 2, b2dBody.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / MainGame.PPM);
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
        vertex[2] = new Vector2(-3, 3).scl(1 / MainGame.PPM);
        vertex[3] = new Vector2(3, 3).scl(1 / MainGame.PPM);
        head.set(vertex);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = MainGame.ENEMY_HEAD_BIT;
        b2dBody.createFixture(fixtureDef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
    }
}
