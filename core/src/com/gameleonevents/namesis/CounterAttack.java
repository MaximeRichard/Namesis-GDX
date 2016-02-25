package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

public class CounterAttack extends ApplicationAdapter {

    //Global variable to handle the game state
    private GameState gameState;

    SpriteBatch batch;
    private Texture backgroundImage;
    private Sprite backgroundSprite;

    private Texture enemyImage;
    private Sprite enemySprite;

    private EnemyState enemyState;

    //Variable used to handle the touch event
    private Vector3 touchPos;
    private OrthographicCamera camera;

    //Timer variables
    private double vulnerabiltyTimer;
    private double vulnerabiltyDuration;
    private double elapsedTime;

    private boolean won = false;
    private boolean lost = false;

    //Text to give the user a feedback
    private BitmapFont font;

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

    public ActionResolver act;

    public CounterAttack(ActionResolver actionResolver) {
        this.act = actionResolver;
    }

    @Override
    public void create () {
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
        fontGenerator.dispose();

        vulnerabiltyTimer = 2 + (Math.random() * (2));
        vulnerabiltyDuration = vulnerabiltyTimer + 0.5f;

        enemyState = EnemyState.DEFENDING;
        enemyImage = new Texture("data/monster_shield_on.png");

        elapsedTime = 0;

        //Dynamically assign the height, width, x pos and y pos of the enemy based on the screen size
        enemySpriteSize = new Double(Gdx.graphics.getWidth() * 0.4).intValue();
        enemyPosX = new Double(Gdx.graphics.getWidth() * 0.3).intValue();
        enemyPosY = new Double(Gdx.graphics.getHeight() * 0.12).intValue();

        //Initializing sounds
        shieldHit = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/hit_shield.mp3"));
        shieldOpening = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/opening_shield.mp3"));
        counterhit = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/counter_hit.mp3"));

        touchPos = new Vector3();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, enemySpriteSize, enemySpriteSize);

        gameState = GameState.INGAME;
    }

    @Override
    public void render () {

        if(gameState == GameState.INGAME) {
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if(act.SendBeaconInfo() == "PROXIMITY_IMMEDIATE" || act.SendBeaconInfo() == "PROXIMITY_NEAR")
                vulnerabiltyDuration = vulnerabiltyTimer + 1.5f;
            //Listen for a click on the enemy sprite
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x > enemySprite.getX() && touchPos.x < enemySprite.getX() + enemySprite.getWidth()) {
                    if (touchPos.y > enemySprite.getY() && touchPos.y < enemySprite.getY() + enemySprite.getHeight()) {
                        if (enemyState == EnemyState.VULNERABLE) {
                            if(!counterHitPlayed){
                                counterhit.play(0.5f);
                                counterHitPlayed = true;
                            }
                            NotifyWin();
                        } else {
                            if(!shieldHitPlayed){
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
                if(!shieldOpeningPlayed){
                    shieldOpening.play(0.5f);
                    shieldOpeningPlayed = true;
                }
            } else {
                enemyState = EnemyState.DEFENDING;
            }

            //Check if the player failed to counter-attack in time
            if(elapsedTime > vulnerabiltyTimer + vulnerabiltyDuration){
                //Stop the game in this case
                NotifyLoose();
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
            String resultString = "";
            if(won){
                resultString = "Vous avez gagné !";
            }
            if(lost){
                resultString = "Vous avez perdu !";
            }
            font.draw(batch, resultString, new Double(Gdx.graphics.getWidth() * 0.36).intValue(),
                    new Double(Gdx.graphics.getHeight() * 0.92).intValue());
            batch.end();
        }
    }

    private void NotifyWin(){
        gameState = GameState.STOPPED;
        won = true;
    }

    private void NotifyLoose(){
        gameState = GameState.STOPPED;
        lost = true;
    }
}
