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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pierre on 12/02/2016.
 */

public class TrapDefender implements Screen {

    private GameState gameState;

    SpriteBatch batch;
    private Texture backgroundImage;
    private Sprite backgroundSprite;
    private Texture trapImage;
    private Sprite trapSprite;
    private Texture fillBarImage;
    private Sprite fillBarSprite;

    //Text to give the user a feedback
    private BitmapFont font;

    private double timer;
    private float countdown;

    private int iTrapIndex;

    //Initializing sounds, plus the boolean to avoid looping on frames
    private Sound trapSetting;
    private Sound trapFree;
    private Sound endTimer;

    private boolean isEndTimerPlayed = false;

    //Variables to resize the trap sprite to match multiple resolution
    private int spriteSize;
    private int posX;
    private int posY;

    private boolean isTrapSet = false;

    int iLoops;
    boolean hasClicked;
    private String resultString;
    private String countdownText;
    private float fillAmount;

    //Variables used fo touch handling
    private Vector3 touchPos;
    private OrthographicCamera camera;

    public NamesisGame game;

    public TrapDefender(NamesisGame game){
        this.game = game;
    batch = new SpriteBatch();

    backgroundImage = new Texture("data/trap_background.png");
    backgroundSprite = new Sprite(backgroundImage);

    fillBarImage = new Texture("data/fill_bar.png");
    fillBarSprite = new Sprite(fillBarImage);

    trapImage = new Texture("data/trap0.png");
    trapSprite = new Sprite(trapImage);

    trapSetting = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/setting_trap.mp3"));
    trapFree = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/trap_free.mp3"));
    endTimer = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/end_timer.mp3"));

    camera = new OrthographicCamera();
    camera.setToOrtho(true, 600, 600);
    touchPos = new Vector3();

    resultString = "";
    countdownText = "";
    fillAmount = 1;

    //Creating text using free type font generator. This allows to create
    //bitmap font without any quality loss.
    FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/HAMLETORNOT.TTF"));
    FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    fontParameter.size = 30 * (Gdx.graphics.getWidth() / 800);
    fontParameter.color = Color.WHITE;
    font = fontGenerator.generateFont(fontParameter);
    fontGenerator.dispose();

    spriteSize = new Double(Gdx.graphics.getWidth() * 0.5).intValue();
    posX = new Double((Gdx.graphics.getWidth() - spriteSize) / 2).intValue();
    posY = new Double(Gdx.graphics.getHeight() * 0.01).intValue();

    timer = 0;
    iLoops = 0;
    hasClicked = false;
    countdown = 5;

    iTrapIndex = 0;

    gameState = GameState.COUNTDOWN;
}

    public void NotifyWin(){
        resultString = "Le piege est désamorcé !";
        gameState = GameState.STOPPED;
        float delay = 1; // seconds

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new ExplorationScreen(game, PlayerMode.defenseur));
            }
        }, delay);
    }

    public void NotifyLoose(){
        resultString = "Echec, vous etes pris au piege !";
        gameState = GameState.STOPPED;
        float delay = 1; // seconds

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new ExplorationScreen(game, PlayerMode.defenseur));
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
                resultString = "Désamorce le piege !";
            }
            else{
                resultString = "";
                gameState = GameState.INGAME;
            }
        }

        batch.begin();
        batch.draw(backgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(fillBarSprite, new Double(Gdx.graphics.getWidth() * 0.31).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.91).intValue(),
                new Double((Gdx.graphics.getWidth() * 0.4) * fillAmount).intValue(),
                new Double(Gdx.graphics.getWidth() * 0.015).intValue());
        batch.draw(trapSprite, posX, posY, spriteSize, spriteSize);

        font.draw(batch, resultString, new Double(Gdx.graphics.getWidth() * 0.33).intValue(), new Double(Gdx.graphics.getHeight() * 0.8).intValue());
        font.draw(batch, countdownText, new Double(Gdx.graphics.getWidth() * 0.5).intValue(), new Double(Gdx.graphics.getHeight() * 0.8).intValue());
        batch.end();

        if(gameState == GameState.INGAME)
        {
            timer += Gdx.app.getGraphics().getDeltaTime();

            if(timer >= 3f) {
                NotifyLoose();
                if(!isEndTimerPlayed){
                    isEndTimerPlayed = true;
                    endTimer.play(0.5f);
                }
            }

            fillAmount = new Double((3 - timer) / 3).floatValue();

            //Listen for a click on the trap sprite
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x > trapSprite.getX() && touchPos.x < trapSprite.getX() + trapSprite.getWidth()) {
                    if (touchPos.y > trapSprite.getY() && touchPos.y < trapSprite.getY() + trapSprite.getHeight()) {
                        if(iLoops == 0){
                            hasClicked = true;
                            if(iTrapIndex < 5){
                                iTrapIndex ++;
                                trapSetting.play(0.5f);
                                trapImage = new Texture("data/trap" + iTrapIndex + ".png");
                            }
                        }
                    }
                }
            }

            if(iTrapIndex == 5){
                if(!isTrapSet){
                    isTrapSet = true;
                    trapFree.play(0.5f);
                }
                NotifyWin();
            }
            trapSprite = new Sprite(trapImage);

            System.out.println("Index : " + iTrapIndex);

            if(hasClicked){
                iLoops++;
            }

            if(iLoops >= 10){
                hasClicked = false;
                iLoops = 0;
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
