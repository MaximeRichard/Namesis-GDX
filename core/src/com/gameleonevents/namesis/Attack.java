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

import java.awt.Image;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Pierre on 17/02/2016.
 */

public class Attack extends ApplicationAdapter
{
    private SpriteBatch batch;

    private TextureAtlas applicationAtlas;
    private Skin applicationSkin;
    private Stage stage;

    private Sprite background;
    private Sprite sword;
    private Sprite fillBarSprite;

    float swordSpeed;
    private boolean sens = false;

    int stoneIndex;

    //Gems management
    private int gemSize;
    private int startPos;
    private LinkedList<Texture> images;
    private LinkedList<Gem> gems;

    private GameState gameState;
    private OrthographicCamera camera;

    private ImageButton blueButton;
    private ImageButton yellowButton;
    private ImageButton greenButton;
    private ImageButton purpleButton;
    public ActionResolver act;

    private double timer;
    private float fillAmount;

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

        swordSpeed = 0.3f*Gdx.graphics.getWidth(); // 10 pixels per second.
        stoneIndex = 0;
        timer = 0;
        fillAmount = 1;

        applicationAtlas = new TextureAtlas("data/buttons.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);

        sword = new Sprite(new Texture("data/sword.png"));
        background = new Sprite(new Texture("data/attack_background.png"));
        fillBarSprite = new Sprite(new Texture("data/fill_bar.png"));

        images = new LinkedList<Texture>();
        images.add(new Texture("data/blue_stone.png"));
        images.add(new Texture("data/yellow_stone.png"));
        images.add(new Texture("data/purple_stone.png"));
        images.add(new Texture("data/green_stone.png"));

        gems = new LinkedList<Gem>();
        for(int i = 0; i < 12; i++){
            int randImage = 0 + (int) (Math.random() * images.size());
            gems.add(new Gem(images.get(randImage), i));
        }

        InitializeButtons();

        gameState = GameState.INGAME;
    }

    @Override
    public void render()
    {
        if(gameState == GameState.INGAME)
        {
            timer += Gdx.app.getGraphics().getDeltaTime();
            fillAmount = new Double((10 - timer) / 10).floatValue();

            if(sword.getX() <= Gdx.graphics.getWidth()*0.85f && sens == false) {
                sword.setX(sword.getX()+Gdx.graphics.getDeltaTime() * swordSpeed);
            }
            else if (sword.getX() >= Gdx.graphics.getWidth()*0.05f && sens == true) {
                sword.setX(sword.getX()-Gdx.graphics.getDeltaTime() * swordSpeed);
            }
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(sword, sword.getX(), Gdx.graphics.getHeight()*0.43f + gemSize, Gdx.graphics.getWidth()*0.09f, Gdx.graphics.getHeight()*0.3f);

            for(int i = 0; i < gems.size(); i++){
                Gem myGem = gems.get(i);
                batch.draw(myGem, myGem.getPositionX(), myGem.getPositionY(), myGem.getSize(), myGem.getSize());
            }

            batch.draw(fillBarSprite, new Double(Gdx.graphics.getWidth() * 0.31).intValue(),
                    new Double(Gdx.graphics.getHeight() * 0.925).intValue(),
                    new Double((Gdx.graphics.getWidth() * 0.4) * fillAmount).intValue(),
                    new Double(Gdx.graphics.getWidth() * 0.015).intValue());

            batch.end();

            batch.begin();
            stage.draw();
            batch.end();

            if(sword.getX() >= Gdx.graphics.getWidth()*0.85f && sens == false) {
                sens = true;
            }
            else if (sword.getX() <= Gdx.graphics.getWidth()*0.05f && sens == true) {
                sens = false;
            }

            if(timer >= 10){
                NotifyLoose();
            }
        }
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

    public void NotifyWin()
    {
        gameState = GameState.STOPPED;
    }

    public void NotifyLoose()
    {
        gameState = GameState.STOPPED;
    }
}
