package com.gameleonevents.namesis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NamesisGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public ActionResolver act;

    public NamesisGame(ActionResolver actionResolver) {
        this.act = actionResolver;
    }

    public void create() {
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); //important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
