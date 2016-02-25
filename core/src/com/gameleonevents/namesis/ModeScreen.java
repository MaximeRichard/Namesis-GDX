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
 * Created by Maxime on 25/02/2016.
 */
public class ModeScreen implements Screen {
    final NamesisGame game;
    OrthographicCamera camera;
    Stage stage;
    private Sprite background;
    public ModeScreen(NamesisGame namesisGame) {
        game = namesisGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        background = new Sprite(new Texture("data/mode-background.png"));
        initImageButton("fantome", Gdx.graphics.getWidth() * 0.10f, Gdx.graphics.getHeight() * 0.18f);
        initImageButton("predateur", Gdx.graphics.getWidth() * 0.35f, Gdx.graphics.getHeight() * 0.18f);
        initImageButton("defensif", Gdx.graphics.getWidth() * 0.60f, Gdx.graphics.getHeight() * 0.18f);
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

    public void initImageButton(String title, float positionX, float positionY){
        final String eventbutton = title;
        TextureAtlas applicationAtlas = new TextureAtlas("data/etat/packed/etat.pack");
        Skin applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);
        ImageButton.ImageButtonStyle mapButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable drawButton = applicationSkin.getDrawable(title);
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        mapButtonStyle.imageUp = drawButton;
        ImageButton mapButton = new ImageButton(mapButtonStyle);
        mapButton.setPosition(positionX, positionY);
        mapButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("My Tag", "Touche");
                ButtonEvent(eventbutton);
                dispose();
                return true;
            }
        });
        stage.addActor(mapButton);
    }

    public void ButtonEvent(String action){
        switch (action){
            case "fantome":
                game.setScreen(new ExplorationScreen(game, PlayerMode.fantome));
                break;
            case "predateur":
                game.setScreen(new ExplorationScreen(game, PlayerMode.predateur));
                break;
            default:
                game.setScreen(new ExplorationScreen(game, PlayerMode.defenseur));
                break;
        }
    }
}
