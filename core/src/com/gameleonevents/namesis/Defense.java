package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Pierre on 17/02/2016.
 */

enum GemColor{
    PURPLE,
    GREEN,
    YELLOW,
    BLUE,
    NONE
}

public class Defense implements Screen {

    private final float limitTimer = 0.75f;
    private String screen= "";

    private SpriteBatch batch;

    private TextureAtlas applicationAtlas;
    private Skin applicationSkin;
    private Stage stage;

    private BitmapFont score;
    private BitmapFont gameFont;

    private Sprite background;
    private Sprite shieldSprite;
    private Sprite fillBarSprite;
    private Sprite failSprite;

    private Sound hitSuccess;
    private Sound hitFailed;

    private GameState gameState;
    private OrthographicCamera camera;
    private ImageButton blueButton;
    private ImageButton yellowButton;
    private ImageButton greenButton;
    private ImageButton purpleButton;
    public ActionResolver act;

    private ColoredSword swordSelected;
    private SwordColor shieldColor;

    private float countdown;
    private int swordIndex;
    private float timer;
    private int defenceValidated;
    private float fillAmount;
    private boolean hasFailed;

    private String countdownText;
    private String gameText;
    private NamesisGame game;
    private LinkedList<String> imagesPath;
    private LinkedList<ColoredSword> swords;
    private LinkedHashMap<SwordColor, String> shieldPath;

    public Defense(NamesisGame game) {
        this.game = game;
        //Creating text using free type font generator. This allows to create
        //bitmap font without any quality loss.
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/HAMLETORNOT.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 30 * (Gdx.graphics.getWidth() / 800);
        fontParameter.color = Color.WHITE;
        score = fontGenerator.generateFont(fontParameter);
        gameFont = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        //Initializing sounds
        hitSuccess = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/gem_hit.mp3"));
        hitFailed = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hit_shield.mp3"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        applicationAtlas = new TextureAtlas("data/buttons.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);
        background = new Sprite(new Texture("data/defense_background.png"));
        timer = 0;
        swordIndex = 0;
        defenceValidated = 0;
        fillAmount = 1;
        gameText = "";
        countdownText = "";
        countdown = 5;
        hasFailed = false;

        fillBarSprite = new Sprite(new Texture("data/fill_bar.png"));
        failSprite = new Sprite(new Texture("data/defence_fail.png"));

        shieldPath = new LinkedHashMap<SwordColor, String>();
        shieldPath.put(SwordColor.NONE, "data/shield.png");
        shieldPath.put(SwordColor.BLUE, "data/blue_shield.png");
        shieldPath.put(SwordColor.YELLOW, "data/yellow_shield.png");
        shieldPath.put(SwordColor.GREEN, "data/green_shield.png");
        shieldPath.put(SwordColor.PURPLE, "data/purple_shield.png");

        imagesPath = new LinkedList<String>();
        imagesPath.add("data/blue_sword.png");
        imagesPath.add("data/yellow_sword.png");
        imagesPath.add("data/purple_sword.png");
        imagesPath.add("data/green_sword.png");

        InitializeButtons();

        swords = new LinkedList<ColoredSword>();
        shieldColor = SwordColor.NONE;
        shieldSprite = new Sprite(new Texture(shieldPath.get(shieldColor)));

        int iPreviousSwordIndex = 4;

        while(swords.size() < 12){
            int imageIndex = 0 + (int) (Math.random() * imagesPath.size());
            if(imageIndex != iPreviousSwordIndex){
                swords.add(new ColoredSword(imagesPath.get(imageIndex)));
                iPreviousSwordIndex = imageIndex;
            }
        }

        gameState = GameState.COUNTDOWN;
    }



    private void NextGem(){
            timer = 0;
            swordIndex ++;
            shieldColor = SwordColor.NONE;
            hasFailed = false;
    }

    public void InitializeButtons()
    {
        //Initialize blue button
        ImageButton.ImageButtonStyle blueButtonStyle = new ImageButton.ImageButtonStyle();
        Drawable drawButton = applicationSkin.getDrawable("blue_orb");
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        blueButtonStyle.imageUp = drawButton;
        blueButton = new ImageButton(blueButtonStyle);
        blueButton.setPosition(Gdx.graphics.getWidth()*0.83f, Gdx.graphics.getHeight()*0.15f);

        blueButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(gameState == GameState.INGAME){
                    if(swordSelected.getSwordColor() == SwordColor.BLUE){
                        shieldColor = SwordColor.BLUE;
                        defenceValidated++;
                        hitSuccess.play(0.5f);
                    }
                    else{
                        hasFailed = true;
                        hitFailed.play(0.5f);
                    }
                }
                return true;
            }
        });

        stage.addActor(blueButton);

        //Initialize yellow button
        ImageButton.ImageButtonStyle yellowButtonStyle = new ImageButton.ImageButtonStyle();
        drawButton = applicationSkin.getDrawable("yellow_orb");
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight()*0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        yellowButtonStyle.imageUp = drawButton;

        yellowButton = new ImageButton(yellowButtonStyle);
        yellowButton.setPosition(Gdx.graphics.getWidth()*0.01f, Gdx.graphics.getHeight()*0.15f);

        yellowButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(gameState == GameState.INGAME){
                    if(swordSelected.getSwordColor() == SwordColor.YELLOW){
                        shieldColor = SwordColor.YELLOW;
                        defenceValidated++;
                        hitSuccess.play(0.5f);
                    }
                    else{
                        hasFailed = true;
                        hitFailed.play(0.5f);
                    }
                }
                return true;
            }
        });

        stage.addActor(yellowButton);

        //Initialize green button
        ImageButton.ImageButtonStyle greenButtonStyle = new ImageButton.ImageButtonStyle();
        drawButton = applicationSkin.getDrawable("green_orb");
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight()*0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        greenButtonStyle.imageUp = drawButton;

        greenButton = new ImageButton(greenButtonStyle);
        greenButton.setPosition(Gdx.graphics.getWidth()*0.15f, Gdx.graphics.getHeight()*0.01f);

        greenButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.INGAME){
                    if(swordSelected.getSwordColor() == SwordColor.GREEN){
                        shieldColor = SwordColor.GREEN;
                        defenceValidated++;
                        hitSuccess.play(0.5f);
                    }
                    else{
                        hasFailed = true;
                        hitFailed.play(0.5f);
                    }
                }
                return true;
            }
        });

        stage.addActor(greenButton);

        //Initialize green button
        ImageButton.ImageButtonStyle purpleButtonStyle = new ImageButton.ImageButtonStyle();
        drawButton = applicationSkin.getDrawable("purple_orb");
        drawButton.setMinHeight((drawButton.getMinHeight() * Gdx.graphics.getHeight() * 0.0016f));
        drawButton.setMinWidth((drawButton.getMinWidth() * Gdx.graphics.getWidth() * 0.001f));
        purpleButtonStyle.imageUp = drawButton;

        purpleButton = new ImageButton(purpleButtonStyle);
        purpleButton.setPosition(Gdx.graphics.getWidth()*0.70f, Gdx.graphics.getHeight()*0.01f);

        purpleButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(gameState == GameState.INGAME){
                    if(swordSelected.getSwordColor() == SwordColor.PURPLE){
                        shieldColor = SwordColor.PURPLE;
                        defenceValidated++;
                        hitSuccess.play(0.5f);
                    }
                    else{
                        hasFailed = true;
                        hitFailed.play(0.5f);
                    }
                }
                return true;
            }
        });

        stage.addActor(purpleButton);
    }

    public void NotifyEnd()
    {
        gameState = GameState.STOPPED;
        shieldColor = SwordColor.NONE;
        gameText = "Score final : " + defenceValidated + " points";
        float delay = 1; // seconds

        int enemyScore = IA.SimulerAttaque();
        if(enemyScore > defenceValidated)
            screen = "data/screens/defense-echoue.png";
        else
            screen = "data/screens/defense-reussi.png";

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new EcranIntermediaire(game, PlayerMode.predateur, screen));
            }
        }, delay);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(gameState == GameState.COUNTDOWN)
        {
            countdown -= Gdx.app.getGraphics().getDeltaTime();

            if(countdown > 1){
                countdownText = new Integer(new Double(Math.floor(countdown)).intValue()).toString();
            }
            else if(countdown <= 1 && countdown > 0){
                countdownText = "";
                gameText = "Pare les attaques !";
            }
            else{
                gameText = "";
                gameState = GameState.INGAME;
            }
        }

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shieldSprite = new Sprite(new Texture(shieldPath.get(shieldColor)));

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(shieldSprite, new Double(Gdx.graphics.getWidth() * 0.5).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.4).intValue(),
                new Double(Gdx.graphics.getWidth() * 0.16).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.4).intValue());
        score.draw(batch, new Integer(defenceValidated).toString(), new Double(Gdx.graphics.getWidth() * 0.92).intValue(), new Double(Gdx.graphics.getHeight() * 0.965).intValue());

        batch.draw(fillBarSprite, new Double(Gdx.graphics.getWidth() * 0.31).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.925).intValue(),
                new Double((Gdx.graphics.getWidth() * 0.4) * fillAmount).intValue(),
                new Double(Gdx.graphics.getWidth() * 0.015).intValue());

        gameFont.draw(batch, gameText, new Double(Gdx.graphics.getWidth() * 0.37).intValue(), new Double(Gdx.graphics.getHeight() * 0.3).intValue());
        gameFont.draw(batch, countdownText, new Double(Gdx.graphics.getWidth() * 0.5).intValue(), new Double(Gdx.graphics.getHeight() * 0.3).intValue());

        batch.end();
        batch.begin();
        stage.draw();
        batch.end();

        if(gameState == GameState.INGAME)
        {
            if(swordIndex >= 12){
                NotifyEnd();
            }
            else
            {
                swordSelected = swords.get(swordIndex);
                batch.begin();
                batch.draw(swordSelected, new Double(Gdx.graphics.getWidth() * 0.3).intValue(),
                        new Double(Gdx.graphics.getHeight() * 0.42).intValue(),
                        new Double(Gdx.graphics.getWidth() * 0.12).intValue(),
                        new Double(Gdx.graphics.getHeight() * 0.3).intValue());
                batch.end();

                if(hasFailed){
                    batch.begin();
                    batch.draw(failSprite, new Double(Gdx.graphics.getWidth() * 0.48).intValue(),
                            new Double(Gdx.graphics.getHeight() * 0.4).intValue(),
                            new Double(Gdx.graphics.getWidth() * 0.2).intValue(),
                            new Double(Gdx.graphics.getHeight() * 0.4).intValue());
                    batch.end();
                }

                timer += Gdx.app.getGraphics().getDeltaTime();
                fillAmount = new Double((limitTimer - timer) / limitTimer).floatValue();

                if(timer >= limitTimer){
                    NextGem();
                }
            }
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
}
