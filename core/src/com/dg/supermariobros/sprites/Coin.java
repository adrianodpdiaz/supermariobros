package com.dg.supermariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 53;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_overworld");
        fixture.setUserData(this);
        setCategoryFilter(SuperMarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("coin", "collision");
        if(getCell().getTile().getId() != BLANK_COIN) {
            new SoundManager().getAssetManager().get("audio/sounds/coin.wav", Sound.class).play();
        } else {
            new SoundManager().getAssetManager().get("audio/sounds/bump.wav", Sound.class).play();
        }

        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
