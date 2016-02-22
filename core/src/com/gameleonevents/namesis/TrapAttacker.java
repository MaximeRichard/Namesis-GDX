package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Pierre on 12/02/2016.
 */

enum TrapSate{
    CLOSED,
    OPENING,
    OPEN
}

public class TrapAttacker extends ApplicationAdapter{

    private GameState gameState;

    SpriteBatch batch;
    private Texture backgroundImage;
    private Sprite backgroundSprite;
    private Texture trapImage;
    private Sprite trapSprite;

    private TrapSate trapSate;

    private double timer;

    int iLoops;
    boolean hasClicked;

    //Variables used fo touch handling
    private Vector3 touchPos;
    private OrthographicCamera camera;

    @Override
    public void create(){

        batch = new SpriteBatch();

        backgroundImage = new Texture("data/trap_background.png");
        backgroundSprite = new Sprite(backgroundImage);

        trapImage = new Texture("data/trap_closed.png");
        trapSprite = new Sprite(trapImage);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, 600, 600);
        touchPos = new Vector3();

        timer = 0;
        iLoops = 0;
        hasClicked = false;

        trapSate = TrapSate.CLOSED;

        gameState = GameState.INGAME;
    }

    @Override
    public void render()
    {
        if(gameState == GameState.INGAME)
        {
            timer += Gdx.app.getGraphics().getDeltaTime();

            if(timer >= 2f) {
                NotifyLoose();
            }

            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            //Listen for a click on the trap sprite
            if (Gdx.input.isTouched()) {
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                if (touchPos.x > trapSprite.getX() && touchPos.x < trapSprite.getX() + trapSprite.getWidth()) {
                    if (touchPos.y > trapSprite.getY() && touchPos.y < trapSprite.getY() + trapSprite.getHeight()) {
                        if(iLoops == 0){
                            hasClicked = true;
                            if(trapSate == TrapSate.CLOSED){
                                trapSate = trapSate.OPENING;
                            }
                            else {
                                trapSate = TrapSate.OPEN;
                            }
                        }
                    }
                }
            }

            if(trapSate == trapSate.CLOSED){
                trapImage = new Texture("data/trap_closed.png");
            }
            else if(trapSate == TrapSate.OPENING){
                trapImage = new Texture("data/trap_opening.png");
            }
            else{
                trapImage = new Texture("data/trap_open.png");
                NotifyWin();
            }
            trapSprite = new Sprite(trapImage);

            batch.begin();
            batch.draw(backgroundSprite, 0, 0, 1920, 1080);
            batch.draw(trapSprite, 600, 100, 600, 600);
            batch.end();

            if(hasClicked){
                iLoops++;
            }

            if(iLoops >= 4){
                hasClicked = false;
                iLoops = 0;
            }
        }
    }

    public void NotifyWin(){
        gameState = GameState.STOPPED;
    }

    public void NotifyLoose(){
        gameState = GameState.STOPPED;
    }
}
