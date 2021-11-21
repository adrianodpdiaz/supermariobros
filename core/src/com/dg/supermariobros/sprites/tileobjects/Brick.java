package com.dg.supermariobros.sprites.tileobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.dg.supermariobros.MainGame;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.screens.PlayScreen;
import com.dg.supermariobros.sounds.SoundManager;
import com.dg.supermariobros.sprites.Goku;

public class Brick extends InteractiveTileObject {

    public Brick (PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(MainGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Goku goku) {
        if(goku.isBig()) {
            new SoundManager().getAssetManager().get("audio/sounds/breakblock.wav", Sound.class).play();

            setCategoryFilter(MainGame.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
        } else {
            new SoundManager().getAssetManager().get("audio/sounds/bump.wav", Sound.class).play();
        }
    }
}
