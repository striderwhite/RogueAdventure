package com.game.rogueadventure;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Dijkstra 
{
    World world;
    HashMap<Tile,Integer> distance = new HashMap<Tile,Integer>();
    HashMap<Tile,Tile> came_from = new HashMap<Tile,Tile>();
    ArrayList<Tile> settled = new ArrayList<Tile>();
    ArrayList<Tile> unsettled = new ArrayList<Tile>();
    final Integer MAX_DISTANCE = new Integer(Integer.MAX_VALUE);
    
    Dijkstra(World world)
    {
      this.world = world;  
    }
    
    public ArrayList<Tile> dijkstra2(Tile start, Tile goal)
    {
        for(int x = 0 ; x < world.width ; x++)
        {
            for(int y = 0 ; y < world.height ; y++)
            {
                Tile t = world.getTile(x,y);
                if(t.isTraversableAndNotEnemy())
                {
                    distance.put(t, Integer.MAX_VALUE);
                }
            }
        }
       
        distance.put(start, Integer.valueOf(0));
        Tile current = start;
       
        while(!settled.contains(goal))
        {
            for(int x = 0 ; x < world.width ; x++)
            {
                for(int y = 0 ; y < world.height ; y++)
                {
                    unsettled.add(world.getTile(x,y));
                }
            }
            
            for( Tile t : getSurroundingTiles(current))
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
    
    public ArrayList<Tile> dijkstra(Tile start, Tile goal)
    {
        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * assign every node a tentative distance. Zero for the first node, 'max_distance' for the others. *
         * mark all nodes unvisited      (add to unsetled)                                                 *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        for(int x = 0 ; x < world.width ; x ++)
        {
            for(int y = 0 ; y < world.height ; y ++)
            {
                Tile t = world.getTile(x, y);
                distance.put(t, MAX_DISTANCE);
                unsettled.add(t);
                t.visited = false;
            }
        }
        
        distance.put(start, Integer.valueOf(0)); //dist from start -> start is zero
        unsettled.remove(start); //remove from unsettled because we are about to check it
        Tile current = start;
        
        while(goal.visited != true)
        {
            
            for(Tile neighbor : getSurroundingTiles(current))
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
    
    private Tile minimum_distance() //the lowest
    {
        Tile minimum = unsettled.get(0);
        for(Tile t : unsettled)
        {
            if(distance.get(t) < distance.get(minimum))
            {
                minimum = t;
            }      
        }
        return minimum;
    }
    
    private void relaxNeighbors(Tile tile)
    {
        for(Tile t : getSurroundingTiles(tile))
        {
            int  tentative_distance = distance.get(tile) + dist_between(tile,t);
            if(!distance.containsKey(t) || tentative_distance < distance.get(t))
            {
                distance.put(t, Integer.valueOf(tentative_distance));
                came_from.put(t, tile);
                unsettled.add(t);
            }
        }
    }
    
    private ArrayList<Tile> getSurroundingTiles(Tile location) 
    {
        ArrayList<Tile> surroundingTiles = new ArrayList<Tile>(0);

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
            
            
            
            Tile t = world.getTileFacing(location.x,location.y,d);
            if(t != null && !t.isWall() && t.type != Tile_Type.NONE )
            {
                surroundingTiles.add(t);
            }
        }
        surroundingTiles.trimToSize();
        return surroundingTiles;
    }
    
    private int dist_between(Tile current, Tile neighbor)
    {
        return Math.abs(current.x - neighbor.x) + Math.abs(neighbor.y - current.y);
    }
    
    private ArrayList<Tile> retrace_path(Tile goal)
    {
        Tile endPoint = goal;
        ArrayList<Tile> path = new ArrayList<Tile>(0);
        while(came_from.containsKey(endPoint))
        {
            path.add(endPoint);
            endPoint = came_from.get(endPoint);
        }
        Collections.reverse(path); //reverses the order
        path.trimToSize();
        return path;
    }
    
    private ArrayList<Tile> retrace_path(Tile goal, Tile start)
    {
        Tile endPoint = goal;
        ArrayList<Tile> path = new ArrayList<Tile>(0);
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
