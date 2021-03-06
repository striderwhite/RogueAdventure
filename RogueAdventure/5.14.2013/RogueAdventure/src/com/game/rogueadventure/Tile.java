package com.game.rogueadventure;

import java.util.Random;


public class Tile
{
    Tiles type;
    
    Tile() // this constructor chooses a type randomly
    { 
        Random gen = new Random();
        int num = gen.nextInt(4);
        
        if(num == 0)
        {
            type = Tiles.GRASS;
        }
        if(num == 1)
        {
            type = Tiles.WATER;
        }
        if(num == 2)
        {
            type = Tiles.ICE;
        }
        if(num == 3)
        {
            type = Tiles.ROCK;
        }
        if(num == 4)
        {
            type = Tiles.METAL;
        }       
    }
    
    Tile(Tiles type)
    {
        this.type = type;
    }

    
    public Tiles getType()
    {
        return type;
    }
}
