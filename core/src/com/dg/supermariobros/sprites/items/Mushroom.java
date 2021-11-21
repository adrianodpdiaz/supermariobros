package com.dg.supermariobros.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sprites.Goku;

public class Mushroom extends Item {

    private int packAdjustmentX = 436;
    private int packAdjustmentY = 45;

    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mario"), packAdjustmentX, packAdjustmentY, 19, 19);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);

        fixtureDef.filter.categoryBits = MainGame.ITEM_BIT;
        fixtureDef.filter.maskBits = MainGame.GOKU_BIT |
                MainGame.OBJECT_BIT |
                MainGame.GROUND_BIT |
                MainGame.COIN_BIT |
                MainGame.BRICK_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void use(Goku mario) {
        destroy();
        mario.grow();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}