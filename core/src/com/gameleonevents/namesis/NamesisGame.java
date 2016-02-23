package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class NamesisGame extends ApplicationAdapter {
    Stage stage;
    ImageButton button;
    ImageButton.ImageButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
	public ActionResolver act;
    SpriteBatch batch;
    Texture backgroundTexture;
    Sprite backgroundSprite;

    public NamesisGame(ActionResolver actionResolver) {
        this.act = actionResolver;
    }

        @Override
	public void create () {
            batch = new SpriteBatch();
            stage = new Stage(new ScreenViewport());
            Gdx.input.setInputProcessor(stage);
            font = new BitmapFont();
            skin = new Skin();
            buttonAtlas = new TextureAtlas(Gdx.files.internal("data/packed/textures.atlas"));
            skin.addRegions(buttonAtlas);
            textButtonStyle = new ImageButton.ImageButtonStyle();
            textButtonStyle.up = skin.getDrawable("green_orb");
            textButtonStyle.down = skin.getDrawable("blue_orb");
            textButtonStyle.checked = skin.getDrawable("purple_orb");
            button = new ImageButton(textButtonStyle);
            /*backgroundTexture = buttonAtlas.findRegion("trap_background").getTexture();
            backgroundSprite =new Sprite(backgroundTexture);
            backgroundSprite.scale(1);
            backgroundSprite.setPosition(30, 50);*/
            stage.addActor(button);
		//font = new BitmapFont();
		font.setColor(Color.WHITE);
            //batch = new SpriteBatch();


	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        stage.getBatch().begin();
        //backgroundSprite.draw(stage.getBatch());
        stage.getBatch().end();
        stage.draw();

        batch.end();
        if(this.act.SendBeaconInfo() == "PROXIMITY_NEAR"){
            textButtonStyle.up = skin.getDrawable("blue_orb");
        }
        else{
            textButtonStyle.up = skin.getDrawable("yellow_orb");
        }
        /*batch.begin();
        font.draw(batch, this.act.SendBeaconInfo(), 600, 600);
        batch.end();*/
	}
}
