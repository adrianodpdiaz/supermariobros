package com.dg.supermariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sprites.Goku;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    protected MapObject object;
    public Body b2dBody;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, MapObject object) {
        this.object = object;
        this.world = screen.getWorld();
        this.screen = screen;

        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        setPosition(rect.getX() / MainGame.PPM, rect.getY() / MainGame.PPM);

        defineEnemy();
        velocity = new Vector2(-0.8f, 0);

        b2dBody.setActive(false);
        b2dBody.setGravityScale(15);
    }

    protected abstract void defineEnemy();
    public abstract void update(float dt);
    public abstract void hitOnHead(Goku goku);
    public abstract void onEnemyHit(Enemy enemy);

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

    public MapObject getObject() {
        return object;
    }
}
