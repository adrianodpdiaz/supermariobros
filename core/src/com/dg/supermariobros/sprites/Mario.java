package com.dg.supermariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.screens.PlayScreen;

public class Mario extends Sprite {
    public World world;
    public Body b2dBody;
    private TextureRegion marioStand;

    public Mario (World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("goku1"));
        this.world = world;
        defineMario();

        /*
        marioStand = new TextureRegion(getTexture(), 32, 269, 38, 54);
        setBounds(0, 0, 19 / SuperMarioBros.PPM, 27 / SuperMarioBros.PPM);
        setRegion(marioStand);
         */

        marioStand = new TextureRegion(getTexture(), 0, 0, 20, 20);
        setBounds(0, 0, 17/ SuperMarioBros.PPM, 17/ SuperMarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt) {
        setPosition(b2dBody.getPosition().x - getWidth() /2, b2dBody.getPosition().y - getHeight() /2);
    }

    public void defineMario() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / SuperMarioBros.PPM, 32 / SuperMarioBros.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2dBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        CircleShape shape = new CircleShape();


        shape.setRadius(7 / SuperMarioBros.PPM);

        fixtureDef.shape = shape;
        b2dBody.createFixture(fixtureDef);
    }
}
