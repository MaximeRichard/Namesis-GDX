package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Pierre on 09/02/2016.
 */

enum EnemyState{
    DEFENDING,
    VULNERABLE
}

enum GameState{
    INGAME,
    STOPPED,
    COUNTDOWN
}

public class CounterAttack implements Screen {

    //Global variable to handle the game state
    private GameState gameState;

    SpriteBatch batch;
    private Texture backgroundImage;
    private Sprite backgroundSprite;

    private Texture enemyImage;
    private Sprite enemySprite;

    private Sprite slashSprite;

    private EnemyState enemyState;

    //Variable used to handle the touch event
    private Vector3 touchPos;
    private OrthographicCamera camera;

    //Timer variables
    private double vulnerabiltyTimer;
    private double vulnerabiltyDuration;
    private double elapsedTime;
    private float countdown;
    private boolean hasWon;

    //Text to give the user a feedback
    private BitmapFont font;
    private BitmapFont countdownFont;
    private String resultString;
    private String countdownText;

    //Aspect variables
    private int enemySpriteSize;
    private int enemyPosX;
    private int enemyPosY;

    //Sounds
    private Sound shieldHit;
    private Sound shieldOpening;
    private Sound counterhit;

    private boolean shieldHitPlayed = false;
    private boolean shieldOpeningPlayed = false;
    private boolean counterHitPlayed = false;

    public NamesisGame game;

    public CounterAttack(NamesisGame game) {
        this.game = game;

        batch = new SpriteBatch();
        backgroundImage = new Texture("data/counter_attack_background.png");
        backgroundSprite = new Sprite(backgroundImage);

        //Creating text using free type font generator. This allows to create
        //bitmap font without any quality loss.
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/HAMLETORNOT.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 30 * (Gdx.graphics.getWidth() / 800);
        fontParameter.color = Color.WHITE;
        font = fontGenerator.generateFont(fontParameter);
        countdownFont = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        vulnerabiltyTimer = 2 + (Math.random() * (2));
        vulnerabiltyDuration = vulnerabiltyTimer + 0.5f;

        enemyState = EnemyState.DEFENDING;
        enemyImage = new Texture("data/monster_shield_on.png");
        slashSprite = new Sprite(new Texture("data/slash.png"));

        elapsedTime = 0;
        countdown = 5;
        resultString = "";
        countdownText = "";
        hasWon = false;

        //Dynamically assign the height, width, x pos and y pos of the enemy based on the screen size
        enemySpriteSize = new Double(Gdx.graphics.getWidth() * 0.4).intValue();
        enemyPosX = new Double(Gdx.graphics.getWidth() * 0.3).intValue();
        enemyPosY = new Double(Gdx.graphics.getHeight() * 0.23).intValue();

        //Initializing sounds
        shieldHit = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hit_shield.mp3"));
        shieldOpening = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/opening_shield.mp3"));
        counterhit = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/counter_hit.mp3"));

        touchPos = new Vector3();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, enemySpriteSize, enemySpriteSize);

        gameState = GameState.COUNTDOWN;
    }




    private void NotifyWin(){
        resultString = "Vous avez vaincu votre adversaire !";
        gameState = GameState.STOPPED;
        hasWon = true;
        float delay = 1; // seconds

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new ExplorationScreen(game, PlayerMode.defenseur));
            }
        }, delay);
    }

    private void NotifyLoose(){
        resultString = "La contre-attaque a échoué !";
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
                resultString = "Guette une faille dans sa défense !";
            }
            else{
                countdownText = "";
                gameState = GameState.INGAME;
            }
        }

        if(gameState == GameState.INGAME) {
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x > enemySprite.getX() && touchPos.x < enemySprite.getX() + enemySprite.getWidth()) {
                    if (touchPos.y > enemySprite.getY() && touchPos.y < enemySprite.getY() + enemySprite.getHeight()) {
                        if (enemyState == EnemyState.VULNERABLE) {
                            if (!counterHitPlayed) {
                                counterhit.play(0.5f);
                                counterHitPlayed = true;
                            }
                            NotifyWin();
                        } else {
                            if (!shieldHitPlayed) {
                                shieldHit.play(0.5f);
                                shieldHitPlayed = true;
                            }
                            NotifyLoose();
                        }
                    }
                }
            }
            //Set the enemy state based on the timer
            elapsedTime += Gdx.app.getGraphics().getDeltaTime();

            if (elapsedTime >= vulnerabiltyTimer && elapsedTime < vulnerabiltyDuration) {
                enemyState = EnemyState.VULNERABLE;
                if (!shieldOpeningPlayed) {
                    shieldOpening.play(0.5f);
                    shieldOpeningPlayed = true;
                }
            } else {
                enemyState = EnemyState.DEFENDING;
            }

            //Check if the player failed to counter-attack in time
            if (elapsedTime > vulnerabiltyTimer + vulnerabiltyDuration) {
                //Stop the game in this case
                NotifyLoose();
            }
        }

        //Set the right sprite
        if (enemyState == EnemyState.DEFENDING) {
            enemyImage = new Texture("data/monster_shield_on.png");
        } else {
            enemyImage = new Texture("data/monster_shield_off.png");
        }

        enemySprite = new Sprite(enemyImage);

        //Render background and enemy sprite
        batch.begin();
        batch.draw(backgroundSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(enemySprite, enemyPosX, enemyPosY, enemySpriteSize, enemySpriteSize);

        if(hasWon)
            batch.draw(slashSprite, new Double(Gdx.graphics.getWidth() * 0.4).intValue(),
                    new Double(Gdx.graphics.getHeight() * 0.3).intValue(),
                    new Double(Gdx.graphics.getWidth() * 0.2).intValue(),
                    new Double(Gdx.graphics.getHeight() * 0.5).intValue());

        font.draw(batch, resultString, new Double(Gdx.graphics.getWidth() * 0.26).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.14).intValue());
        font.draw(batch, countdownText, new Double(Gdx.graphics.getWidth() * 0.5).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.14).intValue());
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

    }
}
