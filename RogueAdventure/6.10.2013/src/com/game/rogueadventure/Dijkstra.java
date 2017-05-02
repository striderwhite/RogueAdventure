package com.game.rogueadventure;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Dijkstra 
{
    World world;
    HashMap<Block,Integer> distance = new HashMap<Block,Integer>();
    HashMap<Block,Block> came_from = new HashMap<Block,Block>();
    ArrayList<Block> settled = new ArrayList<Block>();
    ArrayList<Block> unsettled = new ArrayList<Block>();
    final Integer MAX_DISTANCE = new Integer(Integer.MAX_VALUE);
    
    Dijkstra(World world)
    {
      this.world = world;  
    }
    
    public ArrayList<Block> dijkstra2(Block start, Block goal)
    {
        for(int x = 0 ; x < world.width ; x++)
        {
            for(int y = 0 ; y < world.height ; y++)
            {
                Block t = world.getBlock(x,y);
                if(t.isTraversableAndNotEnemy())
                {
                    distance.put(t, Integer.MAX_VALUE);
                }
            }
        }
       
        distance.put(start, Integer.valueOf(0));
        Block current = start;
       
        while(!settled.contains(goal))
        {
            for(int x = 0 ; x < world.width ; x++)
            {
                for(int y = 0 ; y < world.height ; y++)
                {
                    unsettled.add(world.getBlock(x,y));
                }
            }
            
            for( Block t : getSurroundingTiles(current))
            {
                if(unsettled.contains(t))
                {
                    int tentative_distance = distance.get(current) + dist_between(current,t);
                    if(tentative_distance < distance.get(t))
                    {
                        distance.put(t,tentative_distance);
                    }
                }
            }
            
            unsettled.remove(current);
            settled.add(current);
            current = minimum_distance();
        }

        return retrace_path(goal);
        
    }
    
    public ArrayList<Block> dijkstra(Block start, Block goal)
    {
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * assign every node a tentative distance. Zero for the first node, 'max_distance' for the others. *
         * mark all nodes unvisited      (add to unsetled)                                                 *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        for(int x = 0 ; x < world.width ; x ++)
        {
            for(int y = 0 ; y < world.height ; y ++)
            {
                Block t = world.getBlock(x, y);
                distance.put(t, MAX_DISTANCE);
                unsettled.add(t);
                t.visited = false;
            }
        }
        
        distance.put(start, Integer.valueOf(0)); //dist from start -> start is zero
        unsettled.remove(start); //remove from unsettled because we are about to check it
        Block current = start;
        
        while(goal.visited != true)
        {
            
            for(Block neighbor : getSurroundingTiles(current))
            {
                int tentative_distance = distance.get(current) + dist_between(neighbor,current);
                if(tentative_distance < distance.get(neighbor))
                {
                    distance.put(neighbor, Integer.valueOf(tentative_distance));
                    came_from.put(neighbor, current);
                }
            }
            
            current.visited = true;
            unsettled.remove(current);
            
            if(goal.visited == true)
            {
                return retrace_path(goal,start);
            }
            
            current = minimum_distance();
        }  
        
        return retrace_path(goal,start);
    }
    
    private Block minimum_distance() //the lowest
    {
        Block minimum = unsettled.get(0);
        for(Block t : unsettled)
        {
            if(distance.get(t) < distance.get(minimum))
            {
                minimum = t;
            }      
        }
        return minimum;
    }
    
    private void relaxNeighbors(Block block)
    {
        for(Block t : getSurroundingTiles(block))
        {
            int  tentative_distance = distance.get(block) + dist_between(block,t);
            if(!distance.containsKey(t) || tentative_distance < distance.get(t))
            {
                distance.put(t, Integer.valueOf(tentative_distance));
                came_from.put(t, block);
                unsettled.add(t);
            }
        }
    }
    
    private ArrayList<Block> getSurroundingTiles(Block location) 
    {
        ArrayList<Block> surroundingTiles = new ArrayList<Block>(0);

        for(Direction d : Direction.values())
        {
            if(location.x == 0)
            {
                if(d == Direction.WEST || d == Direction.NORTHWEST || d == Direction.SOUTHWEST)
                {
                    continue;
                }
            }
            if(location.x == world.width)
            {
                if(d == Direction.EAST || d == Direction.NORTHEAST || d == Direction.SOUTHEAST)
                {
                    continue;
                }
            }
            if(location.y == 0)
            {
                if(d == Direction.NORTH || d == Direction.NORTHEAST || d == Direction.NORTHWEST)
                {
                    continue;
                }
            }
            if(location.y == world.height)
            {
                if(d == Direction.SOUTH || d == Direction.SOUTHEAST || d == Direction.SOUTHWEST)
                {
                    continue;
                }
            }
            if(location.x == 0 && location.y == 0)
            {
                if(d == Direction.SOUTHWEST || d == Direction.WEST ||d == Direction.NORTHWEST || d == Direction.NORTH || d == Direction.NORTHEAST )
                {
                    continue;
                }
            }
            
            if(location.x == 0 && location.y == world.height)
            {
                if(d == Direction.SOUTHEAST || d == Direction.SOUTH || d == Direction.SOUTHWEST || d == Direction.WEST || d == Direction.NORTHWEST )
                {
                    continue;
                }
            }
            
            if(location.x == world.width && location.y == 0)
            {
                if(d == Direction.NORTHWEST || d == Direction.NORTH || d == Direction.NORTHEAST || d == Direction.EAST || d == Direction.SOUTHEAST )
                {
                    continue;
                }
            }
            if(location.x == world.width && location.y == world.height)
            {
                if(d == Direction.SOUTHWEST || d == Direction.SOUTH || d == Direction.SOUTHEAST || d == Direction.EAST || d == Direction.NORTHEAST)
                {
                    continue;
                }
            }            
            
            
            
            Block t = world.getTileFacing(location.x,location.y,d);
            if(t != null && !t.isWall() && t.type != Block_Type.NONE )
            {
                surroundingTiles.add(t);
            }
        }
        surroundingTiles.trimToSize();
        return surroundingTiles;
    }
    
    private int dist_between(Block current, Block neighbor)
    {
        return Math.abs(current.x - neighbor.x) + Math.abs(neighbor.y - current.y);
    }
    
    private ArrayList<Block> retrace_path(Block goal)
    {
        Block endPoint = goal;
        ArrayList<Block> path = new ArrayList<Block>(0);
        while(came_from.containsKey(endPoint))
        {
            path.add(endPoint);
            endPoint = came_from.get(endPoint);
        }
        Collections.reverse(path); //reverses the order
        path.trimToSize();
        return path;
    }
    
    private ArrayList<Block> retrace_path(Block goal, Block start)
    {
        Block endPoint = goal;
        ArrayList<Block> path = new ArrayList<Block>(0);
        while(came_from.containsKey(endPoint))
        {
            path.add(endPoint);
            endPoint = came_from.get(endPoint);
        }
        Collections.reverse(path); //reverses the order
        path.remove(goal);
        path.remove(start);
        path.trimToSize(); 
        return path;
    }
}
