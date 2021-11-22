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
import com.badlogic.gdx.utils.Array;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sprites.enemies.Enemy;
import com.dg.supermariobros.sprites.enemies.Turtle;
import com.dg.supermariobros.sprites.tileobjects.Brick;
import com.dg.supermariobros.sprites.tileobjects.Coin;
import com.dg.supermariobros.sprites.enemies.Goomba;

public class B2WorldCreator {

    private Array<Goomba> goombas;
    private Array<Turtle> turtles;

    public B2WorldCreator (PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // creates ground bodies/fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MainGame.PPM,
                    (rect.getY() + rect.getHeight() /2) / MainGame.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(
                    (rect.getWidth() / 2)  / MainGame.PPM,
                    (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MainGame.GROUND_BIT;
            body.createFixture(fixtureDef);
        }

        // creates pipe bodies/fixtures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MainGame.PPM,
                    (rect.getY() + rect.getHeight() /2) / MainGame.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(
                    (rect.getWidth() / 2)  / MainGame.PPM,
                    (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MainGame.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }

        // creates brick bodies/fixtures
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            new Brick(screen, object);
        }

        // creates coin bodies/fixtures
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            new Coin(screen, object);
        }

        // creates all the goombas
        goombas = new Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            //goombas.add(new Goomba(screen, rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM));
            goombas.add(new Goomba(screen, object));
        }

        // creates the turtles
        turtles = new Array<Turtle>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            turtles.add(new Turtle(screen, object));
        }

        // creates holes bodies/fixtures
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MainGame.PPM,
                    (rect.getY() + rect.getHeight() /2) / MainGame.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(
                    (rect.getWidth() / 2)  / MainGame.PPM,
                    (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MainGame.HOLE_BIT;
            body.createFixture(fixtureDef);
        }

        // creates the flag pole
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MainGame.PPM,
                    (rect.getY() + rect.getHeight() /2) / MainGame.PPM);
            body = world.createBody(bodyDef);

            shape.setAsBox(
                    (rect.getWidth() / 2)  / MainGame.PPM,
                    (rect.getHeight() / 2) / MainGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = MainGame.FLAGPOLE_BIT;
            body.createFixture(fixtureDef);
        }
    }

    public Array<Enemy> getEnemies() {
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turtles);
        return enemies;
    }
}
