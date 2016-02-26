package com.gameleonevents.namesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Pierre on 26/02/2016.
 */
public class EcranIntermediaire implements Screen {

    private final int delay = 3;

    private Sprite background;
    private NamesisGame context;
    private SpriteBatch batch;
    private PlayerMode playerMode;

    @Override
    public void show() {

    }

    public EcranIntermediaire(NamesisGame game, PlayerMode mode, String imagePath){
        batch = new SpriteBatch();
        background = new Sprite(new Texture(imagePath));
        context = game;
        playerMode = mode;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                context.setScreen(new ExplorationScreen(context, playerMode));
            }
        }, delay);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        batch.dispose();
    }
}
