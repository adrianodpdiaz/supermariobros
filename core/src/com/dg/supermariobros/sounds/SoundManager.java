package com.dg.supermariobros.sounds;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager implements Disposable {
    private AssetManager assetManager;

    public SoundManager() {
        assetManager = new AssetManager();

        assetManager.load("audio/music/main_music.mp3", Music.class);

        assetManager.load("audio/sounds/coin.wav", Sound.class);
        assetManager.load("audio/sounds/bump.wav", Sound.class);
        assetManager.load("audio/sounds/breakblock.wav", Sound.class);

        assetManager.finishLoading();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
