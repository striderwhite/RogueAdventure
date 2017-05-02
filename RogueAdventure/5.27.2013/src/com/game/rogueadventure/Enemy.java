package com.game.rogueadventure;

import java.util.Random;

public class Enemy extends Entity 
{

    Enemy(int x, int y, World world, Tile_Type type) 
    {
        super(x, y, world, type); 
    }
    
    Enemy(Enemy e, int randX, int randY)
    {
        super(randX,randY,e.world,e.type);
        level += e.level++;
        attackStrength += e.attackStrength++;
        experience += e.experience += 5;
    }
    
    public void move()+
    {
        if(world.player.getFov().intersect(getFov()))
        {
            move(getDirectionTowards(world.player));
        }
        else
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
}
