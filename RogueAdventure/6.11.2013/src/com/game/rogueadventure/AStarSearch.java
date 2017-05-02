/*package com.game.rogueadventure;

import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AStarSearch 
{
    World world;
    
    AStarSearch(World w)
    {
        world = w;
    }
    
    //THIS IS THE A* SEARCH ALGORITHIM THAT RETURNS AN ARRAYLIST WHICH CONTAINS A SEQUENCE OF TILES TO FROM START TO FINISH
    public ArrayList<Block> aStar (Block start, Block goal)
    {
        ArrayList<Block> closed = new ArrayList<Block>(0); //set of notes evaluated
        ArrayList<Block> open = new ArrayList<Block>(0); //set of notes to be evaluated
        HashMap<Block,Block> came_from = new HashMap<Block,Block>(0); // map of navigated nodes (each node accociated with a parent)
        HashMap<Block,Integer> g_score = new HashMap<Block,Integer>(0);//associates a tile with a g score
        HashMap<Block,Integer> f_score = new HashMap<Block,Integer>(0);//Associates a tile with an f score (f = g + h), h being cost estimate
        Block current;
        open.add(start);
        update_gfx(open,0);
        
        g_score.put(start, 0);
        f_score.put(start, g_score.get(start) + heuristic_cost_estimate2(start, goal) );
        
           
        while(!open.isEmpty())
        {
            open.trimToSize();
            current = lowest_f_score(open,f_score);
            if(current == goal)
            {
                return reconstruct_path(came_from,goal);
            }
            open.remove(current);
            closed.add(current);
            update_gfx(closed,1);
            for(Block neighbor : getSurroundingTiles(current))
            {
                int tentative_g_score = g_score.get(current) + ( dist_between2(current,neighbor) ); //tentative_g_score of the 
                if(closed.contains(neighbor) && tentative_g_score >= g_score.get(neighbor)) //if its in the closed list OR the current neighboring tile has a higher cost
                {
                    continue;
                }
                if(!open.contains(neighbor) || tentative_g_score < g_score.get(neighbor)) //if the tile isnt in the openset OR the g_score is better than the 
                {
                    came_from.put(neighbor, current);
                    g_score.put(neighbor, tentative_g_score);
                    f_score.put(neighbor, g_score.get(neighbor) + heuristic_cost_estimate2(neighbor, goal)); //f = g + h
                    if(!open.contains(neighbor))
                    {
                        open.add(neighbor);
                        update_gfx(open,0);
                    }
                }
            }
        }
        return null; //if failed
        
        
    }
    
    private void update_gfx(ArrayList<Block> blocks , int tag)
    {
        Graphics g = world.graphics;
        if(tag == 0) //if open list
        {
            for(Block t : blocks)
            {
               // t.color = Color.BLUE;
            }
        }
        if(tag == 1) //if closed list
        {
            for(Block t : blocks)
            {
                //t.color = Color.GREEN;
            }
        }
        g.invalidate();
        return;
    }
    
    
    private Integer heuristic_cost_estimate(Block start, Block goal)
    {
        Integer heuristic_cost_estimate = new Integer( (Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y) )); 
        return heuristic_cost_estimate;
    }
    
    private Block lowest_f_score(ArrayList<Block> open, HashMap<Block,Integer> f_score)
    {
        open.trimToSize();
        Block lowest = open.get(0);
        for(Block t : open)
        {
            if(f_score.get(t) < f_score.get(lowest))
            {
                lowest = t;
            }
        }
        return lowest;
    }
    
    private ArrayList<Block> reconstruct_path(HashMap<Block,Block> came_from, Block current_node)
    {
        ArrayList<Block> path = new ArrayList<Block>(1);
        while(came_from.containsKey(current_node))
        {
            path.add(current_node);
            current_node = came_from.get(current_node);
        }
        Collections.reverse(path); //reverses the order
        return path;
    }
    
    private int dist_between(Block current, Block neighbor)
    {
        int dist = Math.abs(current.x - neighbor.x) + Math.abs(current.y - neighbor.y);
        return dist;
    }
    

    
    private ArrayList<Block> getSurroundingTiles(Block location) 
    {
        ArrayList<Block> surroundingTiles = new ArrayList<Block>(0);
        int counter = 0;
        
        for(Direction d : Direction.values())
        {
            counter++;
            Block t = world.getTileFacing(location.x,location.y,d);
            Boolean walkable = t.isTraversableToEnemyNotIncludingEnemy();
            if(walkable)
            {
                surroundingTiles.add(t);
            }
        }
        surroundingTiles.trimToSize();
        return surroundingTiles;
    }
    
    
    
    
    
    //SECOND VERSION OF ASTAR
    
    public ArrayList<Block> aStar2 (Block start, Block goal)
    {
        ArrayList<Block> closed = new ArrayList<Block>(1); //set of notes evaluated
        ArrayList<Block> open = new ArrayList<Block>(); //set of notes to be evaluated
        HashMap<Block,Block> came_from = new HashMap<Block,Block>(1); // map of navigated nodes (each node accociated with a parent)
        HashMap<Block,Integer> g_score = new HashMap<Block,Integer>(1);//associates a tile with a g score
        HashMap<Block,Integer> f_score = new HashMap<Block,Integer>(1);//Associates a tile with an f score (f = g + h), h being cost estimate
        
        open.add(start);
        g_score.put(start, 0);
        f_score.put(start, g_score.get(start) + heuristic_cost_estimate2(start, goal));
        
        while(!closed.contains(goal) || !open.isEmpty()) //closed contains the goal or open list is empty
        {
            Block current = lowest_f_score(open,f_score);
            open.remove(current);
            closed.add(current);
            
            for(Block surrounding_tile : getSurroundingTiles(current))
            {
                if(surrounding_tile.isTraversableToEnemyNotIncludingEnemy() || closed.contains(surrounding_tile))
                {
                    continue;
                }
                if(!open.contains(surrounding_tile))
                {
                    open.add(surrounding_tile);
                    came_from.put(surrounding_tile, current);
                    g_score.put(surrounding_tile, g_score.get(current) + ( dist_between2(current,surrounding_tile) ));
                    f_score.put(surrounding_tile, g_score.get(surrounding_tile) + heuristic_cost_estimate2(current,goal));
                }
                if(open.contains(surrounding_tile))
                {
                    if(g_score.get(surrounding_tile) < g_score.get(current))
                    {
                        came_from.put(surrounding_tile,current);
                        g_score.put(surrounding_tile, g_score.get(current) + ( dist_between2(current,surrounding_tile) ));
                        f_score.put(surrounding_tile, g_score.get(surrounding_tile) + heuristic_cost_estimate2(current,goal));
                    }
                }
            }
            
        }
        
        
        return reconstruct_path(came_from,start);
    }
    
    
    
    //USED FOR ASTAR 2
    private Integer heuristic_cost_estimate2(Block start, Block goal)
    {
        Integer heuristic_cost_estimate =  Integer.valueOf((Math.abs(start.x - goal.x) + Math.abs(start.y - goal.y) )); 
        return heuristic_cost_estimate * 10;
    }
    
    
    //USED FOR ASTAR2
    private int dist_between2(Block current, Block neighbor)
    {
        Direction dir = current.getDirectionTowards(neighbor);
        if(dir == Direction.NORTH || dir == Direction.SOUTH || dir == Direction.EAST || dir == Direction.WEST)
            return 10;
        else
            return 14;
    }
    
    
    
    
    
    
    
    
    
}*/
