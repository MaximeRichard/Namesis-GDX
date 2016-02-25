package com.gameleonevents.namesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by Maxime on 24/02/2016.
 */

enum PlayerMode{
    predateur,
    defenseur,
    fantome
}

public class ExplorationScreen implements Screen {
    final NamesisGame game;
    OrthographicCamera camera;
    Stage stage;
    PlayerMode state;
    String stringState;
    FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/HAMLETORNOT.TTF"));
    FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private Sprite background;

    public ExplorationScreen(NamesisGame namesisGame, PlayerMode state) {
        game = namesisGame;
        this.state = state;
        stringState = "mode-"+ state.toString();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        fontParameter.size = 30 * (Gdx.graphics.getWidth() / 800);
        fontParameter.color = Color.WHITE;
        background = new Sprite(new Texture("data/interface-explo.png"));
        initImageButton("carte", 0, 0);
        initImageButton("perso-psycho", 0, Gdx.graphics.getHeight() * 0.78f);
        initTextButton(stringState, Gdx.graphics.getWidth() * 0.80f, 0, "Choix\ndu\nmode");
        initImageButton("hopital", Gdx.graphics.getWidth() * 0.33f, Gdx.graphics.getHeight() * 0.85f);
        initImageButton("armory", Gdx.graphics.getWidth() * 0.465f, Gdx.graphics.getHeight() * 0.85f);
        initImageButton("boost", Gdx.graphics.getWidth() * 0.60f, Gdx.graphics.getHeight() * 0.85f);
        initImageButton("predateur",   (Gdx.graphics.getWidth() * 0.33f) + (float) (Math.random() *  ((Gdx.graphics.getWidth() * 0.60f)-(Gdx.graphics.getWidth() * 0.33f))), Gdx.graphics.getHeight() * 0.50f);
        initImageButton("defenseur",  (Gdx.graphics.getWidth() * 0.33f) + (float) (Math.random() *  ((Gdx.graphics.getWidth() * 0.60f)-(Gdx.graphics.getWidth() * 0.33f))), Gdx.graphics.getHeight() * 0.28f);
        initImageButton("predateur",   (Gdx.graphics.getWidth() * 0.33f) + (float) (Math.random() *  ((Gdx.graphics.getWidth() * 0.60f)-(Gdx.graphics.getWidth() * 0.33f))), Gdx.graphics.getHeight() * 0.07f);
        initImageButton("scoreboard", Gdx.graphics.getWidth() * 0.85f, Gdx.graphics.getHeight() * 0.85f);
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
        TextureAtlas applicationAtlas = new TextureAtlas("data/explo/packed/exploration.pack");
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

    public void initTextButton(String titleButton, float positionX, float positionY, String text){
        final String eventbutton = titleButton;
        TextureAtlas applicationAtlas = new TextureAtlas("data/explo/packed/exploration.pack");
        Skin applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);
        TextButton.TextButtonStyle mapButtonStyle = new TextButton.TextButtonStyle();
        Drawable drawButton = applicationSkin.getDrawable(titleButton);
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        mapButtonStyle.up = drawButton;
        mapButtonStyle.font = fontGenerator.generateFont(fontParameter);
        TextButton mapButton = new TextButton(text, mapButtonStyle);
        mapButton.setPosition(positionX, positionY);
        mapButton.getLabel().setWrap(true);
        mapButton.getLabel().setWidth(mapButton.getWidth()*0.1f);
        mapButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ButtonEvent(eventbutton);
                dispose();
                return true;
            }
        });
        stage.addActor(mapButton);
    }

    public void ButtonEvent(String action){
        switch (action){
            case "mode-predateur":
                game.setScreen(new ModeScreen(game));
                break;
            case "mode-defenseur":
                game.setScreen(new ModeScreen(game));
                break;
            case "mode-fantome":
                game.setScreen(new ModeScreen(game));
                break;
            default:
                break;
        }
    }
}
