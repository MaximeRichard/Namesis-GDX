package maje.gameleon.hunterd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.Color;

/**
 * Created by Pierre on 09/02/2016.
 */

enum EnemyState{
    DEFENDING,
    VULNERABLE
}

enum GameState{
    INGAME,
    STOPPED
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

    //Text variable to give a feedback to the player
    private BitmapFont font;

    @Override
    public void create () {
        batch = new SpriteBatch();
        backgroundImage = new Texture("data/counter_attack_background.png");
        backgroundSprite = new Sprite(backgroundImage);

        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);

        vulnerabiltyTimer = 2 + (Math.random() * (2));
        vulnerabiltyDuration = vulnerabiltyTimer + 0.5f;

        enemyState = EnemyState.DEFENDING;
        enemyImage = new Texture("data/monster_shield_on.png");

        elapsedTime = 0;

        touchPos = new Vector3();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 512, 512);

        gameState = GameState.INGAME;
    }

    @Override
    public void render () {

        if(gameState == GameState.INGAME) {
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //Listen for a click on the enemy sprite
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x > enemySprite.getX() && touchPos.x < enemySprite.getX() + enemySprite.getWidth()) {
                    if (touchPos.y > enemySprite.getY() && touchPos.y < enemySprite.getY() + enemySprite.getHeight()) {
                        if (enemyState == EnemyState.VULNERABLE) {
                            NotifyWin();
                        } else {
                            NotifyLoose();
                        }
                    }
                }
            }
            //Set the enemy state based on the timer
            elapsedTime += Gdx.app.getGraphics().getDeltaTime();

            if (elapsedTime >= vulnerabiltyTimer && elapsedTime < vulnerabiltyDuration) {
                enemyState = EnemyState.VULNERABLE;
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
            batch.draw(backgroundSprite, 0, 0, 1920, 1080);
            batch.draw(enemySprite, 700, 300, 512, 512);
            batch.end();
        }
    }

    private void NotifyWin(){
        gameState = GameState.STOPPED;
        batch.begin();
        font.draw(batch, "Vous avez gagné !", 700, 300);
        batch.end();
    }

    private void NotifyLoose(){
        gameState = GameState.STOPPED;
        batch.begin();
        font.draw(batch, "Vous avez perdu !", 700, 300);
        batch.end();
    }
}