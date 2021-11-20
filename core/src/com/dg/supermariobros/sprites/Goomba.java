package com.dg.supermariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;

    private int packAdjustmentX = 226;
    private int packAdjustmentY = 11;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("mario"), i * 16 + packAdjustmentX, packAdjustmentY, 17, 17));
        }
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / SuperMarioBros.PPM, 16 / SuperMarioBros.PPM);
    }

    public void update(float dt) {
        stateTime += dt;
        setPosition(b2dBody.getPosition().x - getWidth() / 2, b2dBody.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / SuperMarioBros.PPM, 32 / SuperMarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8 / SuperMarioBros.PPM);
        fixtureDef.filter.categoryBits = SuperMarioBros.ENEMY_BIT;
        fixtureDef.filter.maskBits =
                SuperMarioBros.GROUND_BIT
                        | SuperMarioBros.BRICK_BIT
                        | SuperMarioBros.COIN_BIT
                        | SuperMarioBros.ENEMY_BIT
                        | SuperMarioBros.OBJECT_BIT
                        | SuperMarioBros.MARIO_BIT;

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef);
    }
}
