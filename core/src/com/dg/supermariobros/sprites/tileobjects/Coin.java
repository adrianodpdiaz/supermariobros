package com.dg.supermariobros.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;
import com.dg.supermariobros.sprites.items.ItemDef;
import com.dg.supermariobros.sprites.items.Mushroom;
import com.dg.supermariobros.sprites.tileobjects.InteractiveTileObject;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 53;

    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_overworld");
        fixture.setUserData(this);
        setCategoryFilter(SuperMarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("coin", "collision");
        if(getCell().getTile().getId() != BLANK_COIN) {
            if(object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(
                                new Vector2(body.getPosition().x, body.getPosition().y + 16 / SuperMarioBros.PPM),
                                Mushroom.class));
                new SoundManager().getAssetManager().get("audio/sounds/vine.wav", Sound.class).play();
            } else {
                new SoundManager().getAssetManager().get("audio/sounds/coin.wav", Sound.class).play();
            }
        } else {
            new SoundManager().getAssetManager().get("audio/sounds/bump.wav", Sound.class).play();
        }

        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }
}
