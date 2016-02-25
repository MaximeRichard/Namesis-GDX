package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.LinkedList;

/**
 * Created by Pierre on 17/02/2016.
 */

public class Attack extends ApplicationAdapter
{
    private SpriteBatch batch;

    private final int limitTimer = 10;
    private final float lockingTime = 0.5f;
    private final float swordMoveSpeed = 0.5f;

    //Text to give the user a feedback
    private BitmapFont scoreFont;
    private BitmapFont gameFont;

    private TextureAtlas applicationAtlas;
    private Skin applicationSkin;
    private Stage stage;

    private Sprite background;
    private Sword sword;
    private Sprite fillBarSprite;

    float swordSpeed;

    private int bricksValidated;
    private float timeLocked;
    private float countdown;

    private String gameTextString;
    private String countdownText;

    //Gems management
    private int gemSize;
    private LinkedList<String> imagesPath;
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
        //Creating text using free type font generator. This allows to create
        //bitmap font without any quality loss.
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("data/HAMLETORNOT.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 30 * (Gdx.graphics.getWidth() / 800);
        fontParameter.color = Color.WHITE;
        scoreFont = fontGenerator.generateFont(fontParameter);
        gameFont = fontGenerator.generateFont(fontParameter);

        fontGenerator.dispose();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        gemSize = new Double(Gdx.graphics.getWidth() * 0.066f).intValue();

        swordSpeed = swordMoveSpeed * Gdx.graphics.getWidth(); // 10 pixels per second.
        timer = 0;
        bricksValidated = 0;
        fillAmount = 1;
        timeLocked = 0;
        countdown = 5;
        gameTextString = "";
        countdownText = "";

        applicationAtlas = new TextureAtlas("data/buttons.pack");
        applicationSkin = new Skin();
        applicationSkin.addRegions(applicationAtlas);

        sword = new Sword();
        background = new Sprite(new Texture("data/attack_background.png"));
        fillBarSprite = new Sprite(new Texture("data/fill_bar.png"));

        imagesPath = new LinkedList<String>();
        imagesPath.add("data/blue_stone.png");
        imagesPath.add("data/yellow_stone.png");
        imagesPath.add("data/purple_stone.png");
        imagesPath.add("data/green_stone.png");

        gems = new LinkedList<Gem>();
        for(int i = 0; i < 12; i++){
            int randImage = 0 + (int) (Math.random() * imagesPath.size());
            gems.add(new Gem(imagesPath.get(randImage), i, true));
        }

        InitializeButtons();

        sword.setSwordState(SwordState.LOCKED);
        gameState = GameState.COUNTDOWN;
    }

    @Override
    public void render()
    {
        if(gameState == GameState.COUNTDOWN)
        {
            countdown -= Gdx.app.getGraphics().getDeltaTime();

            if(countdown > 1){
                countdownText = new Integer(new Double(Math.floor(countdown)).intValue()).toString();
            }
            else if(countdown <= 1 && countdown > 0){
                countdownText = "";
                gameTextString = "Détruis les gemmes !";
            }
            else{
                gameTextString = "";
                gameState = GameState.INGAME;
            }
        }

        if(gameState == GameState.INGAME)
        {
            Gdx.gl.glClearColor(1, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if(sword.getSwordState() == SwordState.MOVING)
            {
                if(sword.getX() <= Gdx.graphics.getWidth()*0.85f && sword.getDirection() == Direction.LEFT) {
                    sword.setX(sword.getX()+Gdx.graphics.getDeltaTime() * swordSpeed);
                }
                else if (sword.getX() >= Gdx.graphics.getWidth()*0.05f && sword.getDirection() == Direction.RIGHT) {
                    sword.setX(sword.getX()-Gdx.graphics.getDeltaTime() * swordSpeed);
                }

                if(sword.getX() >= Gdx.graphics.getWidth()*0.85f && sword.getDirection() == Direction.LEFT) {
                    sword.setDirection(Direction.RIGHT);
                }
                else if (sword.getX() <= Gdx.graphics.getWidth()*0.05f && sword.getDirection() == Direction.RIGHT) {
                    sword.setDirection(Direction.LEFT);
                }
            }
            else
            {
                timeLocked += Gdx.app.getGraphics().getDeltaTime();
                if(timeLocked > lockingTime){
                    sword.setSwordState(SwordState.MOVING);
                    //CHANGE SWORD SPRITE TO MOVING
                }
            }

            //Updating timer
            timer += Gdx.app.getGraphics().getDeltaTime();
            fillAmount = new Double((limitTimer - timer) / limitTimer).floatValue();

            //Victory/Game end checkings
            if(timer >= limitTimer){
                NotifyEnd();
            }

            if(bricksValidated == 12){
                NotifyEnd();
            }

            System.out.println(bricksValidated);
        }

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(sword, sword.getX(), Gdx.graphics.getHeight()*0.43f + gemSize, Gdx.graphics.getWidth()*0.09f, Gdx.graphics.getHeight()*0.3f);
        scoreFont.draw(batch, new Integer(bricksValidated).toString(), new Double(Gdx.graphics.getWidth() * 0.92).intValue(), new Double(Gdx.graphics.getHeight() * 0.965).intValue());
        gameFont.draw(batch, gameTextString, new Double(Gdx.graphics.getWidth() * 0.36).intValue(), new Double(Gdx.graphics.getHeight() * 0.3).intValue());
        gameFont.draw(batch, countdownText, new Double(Gdx.graphics.getWidth() * 0.5).intValue(), new Double(Gdx.graphics.getHeight() * 0.3).intValue());

        for(int i = 0; i < gems.size(); i++){
            Gem myGem = gems.get(i);
            if(myGem.GetDisplayed()){
                batch.draw(myGem, myGem.getPositionX(), myGem.getPositionY(), myGem.getSize(), myGem.getSize());
            }
        }

        batch.draw(fillBarSprite, new Double(Gdx.graphics.getWidth() * 0.31).intValue(),
                new Double(Gdx.graphics.getHeight() * 0.925).intValue(),
                new Double((Gdx.graphics.getWidth() * 0.4) * fillAmount).intValue(),
                new Double(Gdx.graphics.getWidth() * 0.015).intValue());

        batch.end();

        batch.begin();
        stage.draw();
        batch.end();
    }

    public int CheckCollision(){
        int swordPosition = new Double(sword.getX() + sword.getWidth() / 2).intValue() - new Double(Gdx.graphics.getWidth() * 0.11f).intValue();
        float swordPercentage = (swordPosition * 100) / Gdx.graphics.getWidth();

        int gemIndex = new Double(Math.floor(new Float(swordPercentage / 6.6))).intValue();

        if(gemIndex >= 0 && gemIndex <= 11){
                return gemIndex;
        }
        else
            return -1;
    }

    private void LockSword(){
        timeLocked = 0;
        sword.setSwordState(SwordState.LOCKED);
        //CHANGE SWORD SPRITE TO MOVING
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
                int gemIndex = CheckCollision();
                if(gemIndex != -1){
                    if(gems.get(gemIndex).getGemColor() == GemColor.BLUE){
                        if(gems.get(gemIndex).GetDisplayed()){
                            bricksValidated++;
                            gems.get(gemIndex).setDisplayed(false);
                        }
                    }
                    else
                        LockSword();
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
                System.out.println("Click sur yellow button");
                int gemIndex = CheckCollision();
                if(gemIndex != -1){
                    if(gems.get(gemIndex).getGemColor() == GemColor.YELLOW){
                        if(gems.get(gemIndex).GetDisplayed()){
                            bricksValidated++;
                            gems.get(gemIndex).setDisplayed(false);
                        }
                    }
                    else
                        LockSword();
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
                System.out.println("Click sur green button");
                int gemIndex = CheckCollision();
                if(gemIndex != -1){
                    if(gems.get(gemIndex).getGemColor() == GemColor.GREEN){
                        if(gems.get(gemIndex).GetDisplayed()){
                            bricksValidated++;
                            gems.get(gemIndex).setDisplayed(false);
                        }
                    }
                    else
                        LockSword();
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
                System.out.println("Click sur purple button");
                int gemIndex = CheckCollision();
                if(gemIndex != -1){
                    if(gems.get(gemIndex).getGemColor() == GemColor.PURPLE){
                        if(gems.get(gemIndex).GetDisplayed()){
                            bricksValidated++;
                            gems.get(gemIndex).setDisplayed(false);
                        }
                    }
                    else
                        LockSword();
                }
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
