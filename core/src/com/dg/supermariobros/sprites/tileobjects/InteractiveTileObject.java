package com.dg.supermariobros.sprites.tileobjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.screens.PlayScreen;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (bounds.getX() + bounds.getWidth() / 2) / SuperMarioBros.PPM,
                (bounds.getY() + bounds.getHeight() /2) / SuperMarioBros.PPM);
        body = world.createBody(bodyDef);

        shape.setAsBox(
                (bounds.getWidth() / 2)  / SuperMarioBros.PPM,
                (bounds.getHeight() / 2) / SuperMarioBros.PPM);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);
    }

    public abstract void onHeadHit();

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * SuperMarioBros.PPM / 16),
                (int) (body.getPosition().y * SuperMarioBros.PPM / 16));
    }
}
