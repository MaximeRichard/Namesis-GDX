package com.gameleonevents.namesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Maxime on 24/02/2016.
 */
public class MainMenuScreen implements Screen {
    final NamesisGame game;

    private Sprite background;

    private TextureAtlas applicationAtlas;
    private Skin applicationSkin;
    private Stage stage;
    ImageButton startButton;

    OrthographicCamera camera;

    public MainMenuScreen(final NamesisGame namesisGame) {
        game = namesisGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        background = new Sprite(new Texture("data/main_menu.png"));
        initStartButton();
        //buttonCredits = new Sprite(new Texture("data/main_menu.png"));
        //buttonExit = new Sprite(new Texture("data/main_menu.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        game.batch.begin();
        stage.draw();
        game.batch.end();
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

    }

    public void initStartButton(){
        applicationAtlas = new TextureAtlas("data/menubuttons/menubutton.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);
        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable drawButton = applicationSkin.getDrawable("start");
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        startButtonStyle.imageUp = drawButton;
        startButton = new ImageButton(startButtonStyle);
        startButton.setOrigin(startButton.getWidth() / 2, startButton.getHeight() / 2);
        startButton.setPosition(Gdx.graphics.getWidth()*0.36f, Gdx.graphics.getHeight()/2);

        this.startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("My Tag", "Touche");
                game.setScreen(new ExplorationScreen(game));
                dispose();
                return true;
            }
        });

        stage.addActor(startButton);
    }
}
