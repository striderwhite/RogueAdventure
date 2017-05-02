package com.game.rogueadventure;

import java.util.Random;

public class Enemy extends Entity 
{

    Enemy(int x, int y, World world, Tile_Type type) 
    {
        super(x, y, world, type);
    }
    
    public void move()
    {
        Random gen = new Random();
        int dir  = gen.nextInt(4) + 1;
        if(dir == 1)
        {
            move(Direction.NORTH);
        }
        if(dir == 2)
        {
            move(Direction.SOUTH);
        }
        if(dir == 3)
        {
            move(Direction.EAST);
        }
        if(dir == 4)
        {
            move(Direction.WEST);
        }
    }

}
