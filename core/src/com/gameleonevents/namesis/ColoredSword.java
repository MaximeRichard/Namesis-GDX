package com.gameleonevents.namesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedHashMap;

/**
 * Created by Pierre on 25/02/2016.
 */

enum SwordColor{
    BLUE,
    GREEN,
    YELLOW,
    PURPLE
}

public class ColoredSword extends Sprite {

    private SwordColor _swordColor;
    private LinkedHashMap<String, SwordColor> textureColor = new LinkedHashMap<String, SwordColor>();

    public ColoredSword(String image){
        super(new Texture(image));

        textureColor.put("data/blue_sword.png", SwordColor.BLUE);
        textureColor.put("data/yellow_sword.png", SwordColor.YELLOW);
        textureColor.put("data/green_sword.png", SwordColor.GREEN);
        textureColor.put("data/purple_sword.png", SwordColor.PURPLE);

        _swordColor = textureColor.get(image);
    }

    public SwordColor getSwordColor(){
        return  _swordColor;
    }
}
