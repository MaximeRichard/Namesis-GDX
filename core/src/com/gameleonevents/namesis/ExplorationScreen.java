package com.gameleonevents.namesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sensoro.beacon.kit.Beacon;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maxime on 24/02/2016.
 */

enum PlayerMode{
    predateur,
    defenseur,
    fantome
}

enum BeaconMode{
    active,
    gray,
    cooldown
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

    ImageButton.ImageButtonStyle ims1,ims2,ims3;
    Drawable draw1,draw2,draw3;
    ImageButton butt1,butt2,butt3;

    TextureAtlas applicationAtlas;
    Skin applicationSkin;
    boolean hopital = false, armory = false, boost = false;

    public ExplorationScreen(NamesisGame namesisGame, PlayerMode state) {
        applicationAtlas = new TextureAtlas("data/explo/packed/exploration.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);

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
        initBeaconButtons();
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

        /*game.batch.begin();
        if (game.act.SendBeaconId() != null){
            if(game.act.SendBeaconId().containsKey("0117C55B5938")) {
                hopital = true;
            }
            else
                hopital = false;
            if(game.act.SendBeaconId().containsKey("0117C55CDCC2"))
                boost = true;
            else
                boost = false;
            if(game.act.SendBeaconId().containsKey("0117C552789F"))
                boost = true;
            else
                boost = false;
            if(game.act.SendBeaconId().containsKey("0117C55E2F15"))
                armory = true;
            else
                armory = false;
        }
        game.batch.end();*/

        for(Map.Entry<String, String> entry : this.game.act.SendBeaconId().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(key.equals("0117C55B5938") && value.equals("PROXIMITY_IMMEDIATE")) {
                if (Math.random() >= 0.5) {
                    game.setScreen(new Attack(game));
                } else {
                    game.setScreen(new Defense(game));
                }
            }
            if(key.equals("0117C55CDCC2") && value.equals("PROXIMITY_IMMEDIATE")) {
                if(Math.random()>=0.5) {
                    game.setScreen(new CounterAttack(game));
                }
                else{
                    game.setScreen(new TrapDefender(game));
                }
            }
            if(key.equals("0117C552789F") && value.equals("PROXIMITY_IMMEDIATE")) {
                if(Math.random()>=0.5) {
                    game.setScreen(new CounterAttack(game));
                }
                else{
                    game.setScreen(new TrapDefender(game));
                }
            }
            if(key.equals("0117C55E2F15") && value.equals("PROXIMITY_IMMEDIATE")) {
                if (Math.random() >= 0.5) {
                    game.setScreen(new Attack(game));
                } else {
                    game.setScreen(new Defense(game));
                }
            }

            // do what you have to do here
            // In your case, an other loop.
        }
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
        mapButton.setName(title);
        stage.addActor(mapButton);
    }

    public void initBeaconButtons(){
        ims1 = new ImageButton.ImageButtonStyle();
        draw1 = applicationSkin.getDrawable("hopital-gray");
        draw1.setMinHeight((draw1.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        draw1.setMinWidth((draw1.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        ims1.imageUp = draw1;
        butt1 = new ImageButton(ims1);
        butt1.setPosition(Gdx.graphics.getWidth() * 0.33f, Gdx.graphics.getHeight() * 0.85f);
        butt1.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("My Tag", "Touche");
                //ButtonEvent(eventbutton);
                dispose();
                return true;
            }
        });
        stage.addActor(butt1);

        ims2 = new ImageButton.ImageButtonStyle();
        draw2 = applicationSkin.getDrawable("armory-gray");
        draw2.setMinHeight((draw2.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        draw2.setMinWidth((draw2.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        ims2.imageUp = draw2;
        butt2 = new ImageButton(ims2);
        butt2.setPosition(Gdx.graphics.getWidth() * 0.465f, Gdx.graphics.getHeight() * 0.85f);
        butt2.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("My Tag", "Touche");
                //ButtonEvent(eventbutton);
                dispose();
                return true;
            }
        });
        stage.addActor(butt2);

        ims3 = new ImageButton.ImageButtonStyle();
        draw3 = applicationSkin.getDrawable("boost-gray");
        draw3.setMinHeight((draw3.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        draw3.setMinWidth((draw3.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        ims3.imageUp = draw3;
        butt3 = new ImageButton(ims3);
        butt3.setPosition(Gdx.graphics.getWidth() * 0.60f, Gdx.graphics.getHeight() * 0.85f);
        butt3.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("My Tag", "Touche");
                //ButtonEvent(eventbutton);
                dispose();
                return true;
            }
        });
        stage.addActor(butt3);
    }

    public void initTextButton(String titleButton, float positionX, float positionY, String text){
        final String eventbutton = titleButton;
        TextButton.TextButtonStyle mapButtonStyle = new TextButton.TextButtonStyle();
        Drawable drawButton = applicationSkin.getDrawable(titleButton);
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        mapButtonStyle.up = drawButton;
        mapButtonStyle.font = fontGenerator.generateFont(fontParameter);
        TextButton mapButton = new TextButton(text, mapButtonStyle);
        mapButton.setPosition(positionX, positionY);
        mapButton.getLabel().setWrap(true);
        mapButton.getLabel().setWidth(mapButton.getWidth() * 0.1f);
        mapButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ButtonEvent(eventbutton);
                dispose();
                return true;
            }
        });
        mapButton.setName(titleButton);
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
            case "predateur":
                    if(Math.random()>=0.5) {
                        game.setScreen(new Attack(game));
                    }
                    else{
                        game.setScreen(new Defense(game));
                    }
                break;
            case "defenseur":
                    if(Math.random()>=0.5) {
                        game.setScreen(new CounterAttack(game));
                    }
                    else{
                        game.setScreen(new TrapDefender(game));
                    }
                break;
                /*Skin uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
                Label label = new Label("Voulez-vous vraiment attaquer ce joueur?", uiSkin);
                label.setWrap(true);
                label.setFontScale(1f);
                label.setAlignment(Align.center);

                final Dialog dialog =
                        new Dialog("", uiSkin, "dialog") {
                            protected void result (Object object) {
                                System.out.println("Chosen: " + object);
                            }
                        };

                dialog.padTop(50).padBottom(50);
                dialog.getContentTable().add(label).width(850).row();
                dialog.getButtonTable().padTop(50);

                TextButton dbutton = new TextButton("Yes", uiSkin, "dialog");
                dbutton.getLabelCell().setActorWidth(dbutton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f);
                dbutton.getLabelCell().setActorHeight((dbutton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
                dbutton.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.log("My Tag", "Touche");
                        ButtonEvent("attackPredator");
                        dialog.hide();
                        dispose();
                        return true;
                    }
                });
                dialog.button(dbutton, true);

                dbutton = new TextButton("No", uiSkin, "dialog");
                dbutton.addListener(new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.log("My Tag", "Touche");
                        ButtonEvent("attackDefender");
                        dialog.hide();
                        dispose();
                        return true;
                    }
                });
                dialog.button(dbutton, false);
                dialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
                dialog.invalidateHierarchy();
                dialog.invalidate();
                dialog.layout();
                dialog.show(stage);
                break;*/
            default:
                break;
        }
    }
}
