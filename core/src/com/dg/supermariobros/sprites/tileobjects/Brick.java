package com.dg.supermariobros.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;

public class Brick extends InteractiveTileObject {

    public Brick (PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MainGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("brick", "collision");
        new SoundManager().getAssetManager().get("audio/sounds/breakblock.wav", Sound.class).play();

        setCategoryFilter(MainGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
    }
}
