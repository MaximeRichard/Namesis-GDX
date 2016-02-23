package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Pierre on 17/02/2016.
 */

public class Attack extends ApplicationAdapter {

    private final String gemsSequence = "GPYBGGYPBBGY";

    private SpriteBatch batch;

    private TextureAtlas applicationAtlas;
    private Skin applicationSkin;
    private Stage stage;

    private Sprite background;
    private Sprite sword;

    float swordSpeed;
    float swordX;

    int stoneIndex;

    //Gems management
    private int gemSize;
    private int startPos;
    private LinkedList<Sprite> sprites;
    private LinkedList<Sprite> gems;

    private GameState gameState;
    private OrthographicCamera camera;

    private ImageButton blueButton;
    private ImageButton yellowButton;
    private ImageButton greenButton;
    private ImageButton purpleButton;
    public ActionResolver act;

    public Attack(ActionResolver actionResolver) {
        this.act = actionResolver;
    }

    @Override
    public void create()
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        gemSize = new Double(Gdx.graphics.getWidth() * 0.066f).intValue();
        startPos = new Double(Gdx.graphics.getWidth() * 0.1f).intValue();

        swordSpeed = 0.2f*Gdx.graphics.getWidth(); // 10 pixels per second.
        stoneIndex = 0;

        applicationAtlas = new TextureAtlas("data/buttons.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);

        sword = new Sprite(new Texture("data/sword.png"));
        background = new Sprite(new Texture("data/defense_background.png"));

        sprites = new LinkedList<Sprite>();
        sprites.add(new Sprite(new Texture("data/blue_stone.png")));
        sprites.add(new Sprite(new Texture("data/yellow_stone.png")));
        sprites.add(new Sprite(new Texture("data/purple_stone.png")));
        sprites.add(new Sprite(new Texture("data/green_stone.png")));

        gems = new LinkedList<Sprite>();
        for(int i = 0; i < 12; i++){
            gems.add(sprites.get(0 + (int) (Math.random() * 4)));
        }

        InitializeButtons();

        gameState = GameState.INGAME;
    }

    @Override
    public void render()
    {
        if(gameState == GameState.INGAME)
        {
            if(sword.getX() <= Gdx.graphics.getWidth()*0.10f)
                swordX += Gdx.graphics.getDeltaTime() * swordSpeed;
            else if (sword.getX() >= Gdx.graphics.getWidth()*0.60f)
                swordX -= Gdx.graphics.getDeltaTime() * swordSpeed;
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(sword, swordX, Gdx.graphics.getHeight() * 0.60f, Gdx.graphics.getWidth() * 0.09f, Gdx.graphics.getHeight() * 0.3f);

            batch.draw(gems.get(0), startPos, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(1), startPos + gemSize, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(2), startPos + gemSize * 2, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(3), startPos + gemSize * 3, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(4), startPos + gemSize * 4, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(5), startPos + gemSize * 5, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(6), startPos + gemSize * 6, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(7), startPos + gemSize * 7, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(8), startPos + gemSize * 8, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(9), startPos + gemSize * 9, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(10), startPos + gemSize * 10, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);
            batch.draw(gems.get(11), startPos + gemSize * 11, new Double(Gdx.graphics.getHeight() * 0.5f).intValue(), gemSize, gemSize);

            batch.end();

            batch.begin();
            stage.draw();
            batch.end();
        }
    }

    public void createStones(){

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
                System.out.println("Click sur blue button");
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
                System.out.println("Click sur yellow button");
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
                System.out.println("Click sur green button");
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
                System.out.println("Click sur purple button");
                return true;
            }
        });

        stage.addActor(purpleButton);
    }


    public void SpawnBrick(BrickColor color)
    {

    }

    public void NotifyWin()
    {
        gameState = GameState.STOPPED;
    }

    public void NotifyLoose()
    {
        gameState = GameState.STOPPED;
    }
}
