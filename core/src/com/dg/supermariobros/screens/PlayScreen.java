package com.dg.supermariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dg.supermariobros.SuperMarioBros;
import com.dg.supermariobros.scenes.Hud;
import com.dg.supermariobros.sounds.SoundManager;
import com.dg.supermariobros.sprites.Goomba;
import com.dg.supermariobros.sprites.Mario;
import com.dg.supermariobros.tools.B2WorldCreator;
import com.dg.supermariobros.tools.WorldContactListener;

public class PlayScreen implements Screen {

    private SuperMarioBros game;
    private TextureAtlas atlas;

    private OrthographicCamera gameCam;
    private Viewport gameViewport;
    private Hud hud;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // Sprites
    private Mario player;
    private Goomba goomba;

    private Music music;

    public PlayScreen(SuperMarioBros game) {
        atlas = new TextureAtlas("goku.pack");
        this.game = game;

        //create the cam used to follow mario through cam world
        gameCam = new OrthographicCamera();

        // create a viewport that maintains the virtual aspect ratio
        gameViewport = new FitViewport(
                SuperMarioBros.V_WIDTH / SuperMarioBros.PPM,
                SuperMarioBros.V_HEIGHT / SuperMarioBros.PPM,
                gameCam);

        // create the game HUD (score/time/level info)
        hud = new Hud(game.batch);

        // load the map and setup the map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map_tiled/map1-1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 /  SuperMarioBros.PPM);

        // initially sets the camera to be centered correctly at the start of the map
        gameCam.position.set(gameViewport.getWorldWidth() / 2, gameViewport.getWorldHeight() / 2, 0);

        // creates the Box2DWorld, setting the gravity
        world = new World(new Vector2(0, -10), true);

        // Debug Lines
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(this);

        // creates Mario in the game world
        player = new Mario(this);

        music = new SoundManager().getAssetManager().get("audio/music/main_music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        goomba = new Goomba(this, 5.64f, .16f);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        // if the user is holding down mouse move the camera through the game world
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2dBody.applyLinearImpulse(
                    new Vector2(0, 4f), player.b2dBody.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2dBody.getLinearVelocity().x <= 2)
            player.b2dBody.applyLinearImpulse(new Vector2(0.1f, 0), player.b2dBody.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2dBody.getLinearVelocity().x >= -2)
            player.b2dBody.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2dBody.getWorldCenter(), true);

    }

    public void update(float dt) {
        // handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);
        goomba.update(dt);
        hud.update(dt);

        gameCam.position.x = player.b2dBody.getPosition().x;

        gameCam.update();
        // tells the renderer to draw only what the camera can see
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // clear the game screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the game map
        renderer.render();

        // render Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);
        game.batch.end();

        // set the batch to now draw what the HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // When the size of the screen changes, the viewport gets adjusted to know what the actual screen size is
        gameViewport.update(width, height);

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
