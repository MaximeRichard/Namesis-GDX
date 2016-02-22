package com.gameleonevents.namesis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class NamesisGame extends ApplicationAdapter {
    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
	public ActionResolver act;
    SpriteBatch batch;

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
            textButtonStyle = new TextButton.TextButtonStyle();
            textButtonStyle.font = font;
            textButtonStyle.up = skin.getDrawable("green_orb");
            textButtonStyle.down = skin.getDrawable("blue_orb");
            textButtonStyle.checked = skin.getDrawable("purple_orb");
            button = new TextButton("Button1", textButtonStyle);
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
