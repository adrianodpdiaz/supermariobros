package com.dg.supermariobros.sprites.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;
import com.dg.supermariobros.sprites.tileobjects.InteractiveTileObject;

public class Brick extends InteractiveTileObject {

    public Brick (PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
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
