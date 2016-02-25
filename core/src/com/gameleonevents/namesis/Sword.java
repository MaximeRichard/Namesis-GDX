package com.gameleonevents.namesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Pierre on 24/02/2016.
 */

enum Direction{
    LEFT,
    RIGHT
}

enum SwordState{
    LOCKED,
    MOVING
}

public class Sword extends Sprite {

    private Direction _direction;
    private SwordState _state;

    public Sword(){
        super(new Texture("data/sword.png"));
        _direction = Direction.LEFT;
        _state = SwordState.MOVING;
    }

    public Direction getDirection(){
        return _direction;
    }

    public void setDirection(Direction direction){
        _direction = direction;
    }

    public void setSwordState(SwordState state){
        _state = state;
    }

    public SwordState getSwordState(){
        return _state;
    }
}
