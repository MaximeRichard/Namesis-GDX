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

public class Defense extends ApplicationAdapter {

    private SpriteBatch batch;

    private TextureAtlas applicationAtlas;
    private Skin applicationSkin;
    private Stage stage;

    private Sprite background;

    private GameState gameState;
    private OrthographicCamera camera;
    private ImageButton blueButton;
    private ImageButton yellowButton;
    private ImageButton greenButton;
    private ImageButton purpleButton;
    public ActionResolver act;

    private Gem gemSelected;
    private int gemIndex;
    private float timer;
    private int gemsValidated;

    private LinkedList<String> imagesPath;
    private LinkedList<Gem> gems;

    public Defense(ActionResolver actionResolver) {
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

        applicationAtlas = new TextureAtlas("data/buttons.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);
        background = new Sprite(new Texture("data/defense_background.png"));
        timer = 0;
        gemIndex = 0;
        gemsValidated = 0;

        imagesPath = new LinkedList<String>();
        imagesPath.add("data/blue_stone.png");
        imagesPath.add("data/yellow_stone.png");
        imagesPath.add("data/purple_stone.png");
        imagesPath.add("data/green_stone.png");

        InitializeButtons();

        gems = new LinkedList<Gem>();
        for(int i = 0; i < 12; i++){
            int gemIndex = 0 + (int) (Math.random() * imagesPath.size());
            gems.add(new Gem(imagesPath.get(gemIndex), 0, true));
        }

        gameState = GameState.INGAME;
    }

    @Override
    public void render()
    {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        batch.begin();
        stage.draw();
        batch.end();
        
        if(gameState == GameState.INGAME)
        {
            gemSelected = gems.get(gemIndex);

            batch.begin();
            batch.draw(gemSelected, new Double((Gdx.graphics.getWidth() - gemSelected.getSize()) / 2).intValue(),
                    new Double(Gdx.graphics.getHeight() * 0.2).intValue(), gemSelected.getSize(), gemSelected.getSize());
            batch.end();

            timer += Gdx.app.getGraphics().getDeltaTime();

            if(timer >= 0.5f){
                NextGem();
            }

            if(gemIndex == 11){
                gameState = GameState.STOPPED;
            }
        }
    }

    private void NextGem(){
        timer = 0;
        gemIndex ++;
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
               if(gemSelected.getGemColor() == GemColor.BLUE){
                    gemsValidated++;
               }
               else
                   NextGem();
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
                if(gemSelected.getGemColor() == GemColor.YELLOW){
                    gemsValidated++;
                }
                else
                    NextGem();
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
                if(gemSelected.getGemColor() == GemColor.GREEN){
                    gemsValidated++;
                }
                else
                    NextGem();
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
                if(gemSelected.getGemColor() == GemColor.PURPLE){
                    gemsValidated++;
                }
                else
                    NextGem();
                return true;
            }
        });

        stage.addActor(purpleButton);
    }


    public void NotifyEnd()
    {
        gameState = GameState.STOPPED;
    }

}
