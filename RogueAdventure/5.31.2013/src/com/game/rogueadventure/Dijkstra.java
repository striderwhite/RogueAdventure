package com.game.rogueadventure;

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
    
    Dijkstra(World world)
    {
      this.world = world;  
    }
    
    public ArrayList<Tile> dijkstra(Tile start, Tile goal)
    {
        unsettled.add(start);
        distance.put(start, Integer.valueOf(0)); //dist from start -> start is zero
        
        while(!unsettled.isEmpty())
        {
            Tile current = minimum_distance();
            if(current == goal)
            {
                break;
            }
            unsettled.remove(current);
            settled.add(current);
            relaxNeighbors(current);
        }
        
        return retrace_path(goal);
        
    }
    
    private Tile minimum_distance()
    {
        Tile minimum = unsettled.get(0);
        for(Tile t : unsettled)
        {
            if(settled.contains(t))
            {
                continue;
            }
            else
            {
                if(distance.get(t) < distance.get(minimum))
                {
                    minimum = t;
                }
            }
        }
        return minimum;
    }
    
    private void relaxNeighbors(Tile tile)
    {
        for(Tile t : getSurroundingTiles(tile))
        {
            if(distance.get(t) > distance.get(tile) + dist_between(tile,t))
            {
                distance.put(t, Integer.valueOf(distance.get(tile) + dist_between(tile,t)));
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
            Tile t = world.getTileFacing(location.x,location.y,d);
            Boolean walkable = t.isTraversableToEnemyNotIncludingEnemy();
            if(walkable)
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

        ArrayList<Tile> path = new ArrayList<Tile>(1);
        while(came_from.containsKey(goal))
        {
            path.add(goal);
            goal = came_from.get(goal);
        }
        Collections.reverse(path); //reverses the order
        return path;
    
    }
    
}
