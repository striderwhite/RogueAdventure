package com.game.rogueadventure;

import java.util.ArrayList;
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
        fovSize = 3;
        level += e.level++;
        attackStrength = e.attackStrength++;
        experience += e.experience += 5;
    }
    
    public void move()
    {
        if(getFov().intersects(world.player.x, world.player.y, world.player.x, world.player.y))
        {
           //AStarSearch astar = new AStarSearch(world);
           Dijkstra path_finder = new Dijkstra(world);
           
           ArrayList<Tile> path = path_finder.dijkstra(world.getTile(x, y), world.getTile(world.player.x,world.player.y));
           move(path.get(1));
        }
        else
        {
            Random gen = new Random();
            int dir  = gen.nextInt(8) + 1;
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
                move(Direction.SOUTHWEST);
            }
            if(dir == 5)
            {
                move(Direction.SOUTHEAST);
            }
            if(dir == 6)
            {
                move(Direction.NORTHWEST);
            }
            if(dir == 7)
            {
                move(Direction.NORTHEAST);
            }
            if(dir == 8)
            {
                move(Direction.WEST);
            }
        }
    }
}
