package com.game.rogueadventure;

import java.util.Random;

public class Enemy extends Entity 
{

    Enemy(int x, int y, World world, Block_Type type) 
    {
        super(x, y, world, type); 
    }
    
    Enemy(Enemy e, int randX, int randY)
    {
        super(randX,randY,e.world,e.type);
        fovSize = 3;
        level += e.level++;
        attack_strength = e.attack_strength++;
        experience += e.experience += 5;
    }
    
    public void move()
    {
        
        /*
        if(getFov().intersects(world.getPlayerLoc().x, world.getPlayerLoc().y, world.getPlayerLoc().x, world.getPlayerLoc().y))
        {
            
           AStarSearch astar = new AStarSearch(world);
           Dijkstra path_finder = new Dijkstra(world);
           
           ArrayList<Tile> path = path_finder.dijkstra(world.getTile(x, y), world.getTile(world.player.x,world.player.y));
           for(Tile t : path)
           {
               t.color = Color.GREEN;
           }
           world.graphics.invalidate();
           if(!path.isEmpty())
           {
               move(path.get(0));
           }
            
           Direction toPlayer = world.getDirection(world.player,this);
           move(toPlayer);
            
        }
        else
        {

        }
    */
        
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
