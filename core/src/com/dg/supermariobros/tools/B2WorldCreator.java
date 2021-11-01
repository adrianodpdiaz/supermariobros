package com.dg.supermariobros.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.sprites.Brick;
import com.dg.supermariobros.sprites.Coin;

public class B2WorldCreator {

    private BodyDef bodyDef;
    private PolygonShape shape = new PolygonShape();
    private FixtureDef fixtureDef = new FixtureDef();
    private Body body;

    public B2WorldCreator (World world, TiledMap map) {
        BodyDef bodyDef = new BodyDef();
        shape = new PolygonShape();
        fixtureDef = new FixtureDef();

        // creates ground bodies/fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / SuperMarioBros.PPM,
                    (rect.getY() + rect.getHeight() /2) / SuperMarioBros.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(
                    (rect.getWidth() / 2)  / SuperMarioBros.PPM,
                    (rect.getHeight() / 2) / SuperMarioBros.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        // creates pipe bodies/fixtures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / SuperMarioBros.PPM,
                    (rect.getY() + rect.getHeight() /2) / SuperMarioBros.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(
                    (rect.getWidth() / 2)  / SuperMarioBros.PPM,
                    (rect.getHeight() / 2) / SuperMarioBros.PPM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }

        // creates brick bodies/fixtures
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Brick(world, map, rect);
        }

        // creates coin bodies/fixtures
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Coin(world, map, rect);
        }
    }
}
