package com.gameleonevents.namesis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Pierre on 24/02/2016.
 */

public class Gem extends Sprite {

    private int gemSize;
    private int xPosition;
    private int yPosition;
    private final int xOffset = new Double(Gdx.graphics.getWidth() * 0.1f).intValue();;
    private int yOffset = new Double(Gdx.graphics.getHeight() * 0.45f).intValue();

    public Gem(Texture image, int gemIndex){
        super(image);
        gemSize = new Double(Gdx.graphics.getWidth() * 0.066f).intValue();
        xPosition = xOffset + gemIndex * gemSize;
        yPosition = yOffset;
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

}
