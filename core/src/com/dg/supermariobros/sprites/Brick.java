package com.dg.supermariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.sounds.SoundManager;

public class Brick extends InteractiveTileObject {

    public Brick (World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);
        fixture.setUserData(this);
        setCategoryFilter(SuperMarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("brick", "collision");
        new SoundManager().getAssetManager().get("audio/sounds/breakblock.wav", Sound.class).play();

        setCategoryFilter(SuperMarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
    }
}
