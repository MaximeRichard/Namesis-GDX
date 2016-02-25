package com.gameleonevents.namesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedHashMap;

/**
 * Created by Pierre on 24/02/2016.
 */

public class Gem extends Sprite {

    private int gemSize;
    private int xPosition;
    private int yPosition;
    private final int xOffset = new Double(Gdx.graphics.getWidth() * 0.11f).intValue();;
    private int yOffset = new Double(Gdx.graphics.getHeight() * 0.45f).intValue();
    private GemColor _gemColor;

    private boolean _isDisplayed;

    private LinkedHashMap<String, GemColor> textureColor = new LinkedHashMap<String, GemColor>();

    public Gem(String image, int gemIndex, boolean isDisplayed){
        super(new Texture(image));
        gemSize = new Double(Gdx.graphics.getWidth() * 0.066f).intValue();
        xPosition = xOffset + gemIndex * gemSize;
        yPosition = yOffset;

        textureColor.put("data/blue_stone.png", GemColor.BLUE);
        textureColor.put("data/yellow_stone.png", GemColor.YELLOW);
        textureColor.put("data/purple_stone.png", GemColor.PURPLE);
        textureColor.put("data/green_stone.png", GemColor.GREEN);

        _gemColor = textureColor.get(image);
        _isDisplayed = isDisplayed;
    }

    public int getPositionX(){
        return this.xPosition;
    }

    public int getPositionY(){
        return this.yPosition;
    }

    public int getSize(){
        return this.gemSize;
    }

    public Boolean GetDisplayed(){
        return _isDisplayed;
    }

    public void setDisplayed(boolean isDisplayed){
        _isDisplayed = isDisplayed;
    }

    public GemColor getGemColor(){
        return _gemColor;
    }
}
