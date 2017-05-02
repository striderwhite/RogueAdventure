package com.game.rogueadventure;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Random;


public class Block implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3930868888384229719L;
	Tile_Type type;
	int x, y;
	boolean discovered = false;
	boolean visited; //used for dijkstra
	int color;

    Block(int x, int y) // this constructor chooses a type randomly
    { 
        Random gen = new Random();
        int num = gen.nextInt(4);
        this.x = x;
        this.y = y;
        
        if(num == 0)
        {
            type = Tile_Type.GRASS;
        }
        if(num == 1)
        {
            type = Tile_Type.WATER;
        }
        if(num == 2)
        {
            type = Tile_Type.ICE;
        }
        if(num == 3)
        {
            type = Tile_Type.ROCK;
        }
        if(num == 4)
        {
            type = Tile_Type.METAL;
        }       
    }
    
    Block(int x, int y, Tile_Type type)
    {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    
	public Tile_Type getType()
    {
        return type;
    }

	public boolean isWall() 
	{
		if(type == Tile_Type.WALL)
			return true;
		else
			return false;
		
	}

	public void setType(Tile_Type type)
	{
		this.type = type;
	}

    public boolean isPlayer()
    {
        if(type == Tile_Type.PLAYER)
            return true;
        else
            return false;
    }
    
    public boolean isEnemy()
    {
        if(type == Tile_Type.ENEMY)
            return true;
        else
            return false;
    }

    public boolean isTraversable()
    {
        if(type == Tile_Type.WALL || type == Tile_Type.PLAYER || type == Tile_Type.NONE || type == Tile_Type.ENEMY )
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean isTraversableAndNotEnemy()
    {
        if(type == Tile_Type.WALL || type == Tile_Type.PLAYER || type == Tile_Type.NONE)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean isTraversableToEnemy()
    {
        if(type == Tile_Type.WALL || type == Tile_Type.PLAYER || type == Tile_Type.NONE || type == Tile_Type.ENEMY || type == Tile_Type.UPSTAIRS  || type == Tile_Type.DOWNSTAIRS )
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public boolean isTraversableToEnemyNotIncludingEnemy()
    {
        if(type == Tile_Type.WALL || type == Tile_Type.NONE)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean isStairs() 
    {
        if(type == Tile_Type.UPSTAIRS || type == Tile_Type.DOWNSTAIRS)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

	public Direction getDirectionTowards(int x2, int y2)
	{
		if(x2 > x && y2 > y)
		{
			return Direction.SOUTHEAST;
		}
		if(x2 > x && y2 == y)
		{
			return Direction.EAST;
		}
		if(x2 > x && y2 > y)
		{
			return Direction.NORTHEAST;
		}
		if(x2 == x && y2 < y)
		{
			return Direction.NORTH;
		}
		if(x2 < x && y2 < y)
		{
			return Direction.NORTHWEST;
		}
		if( x2 < x && y2 == y)
		{
			return Direction.WEST;
		}
		if( x2 < x && y2 > y)
		{
			return Direction.SOUTHWEST;
		}
		if( x2 == x && y2 > y)
		{
			return Direction.SOUTH;
		}
		
		return null;
	}

    public Direction getDirectionTowards(Block neighbor) 
    {
        if(neighbor.x > x && neighbor.y > y)
        {
            return Direction.SOUTHEAST;
        }
        if(neighbor.x > x && neighbor.y == y)
        {
            return Direction.EAST;
        }
        if(neighbor.x > x && neighbor.y > y)
        {
            return Direction.NORTHEAST;
        }
        if(neighbor.x == x && neighbor.y < y)
        {
            return Direction.NORTH;
        }
        if(neighbor.x < x && neighbor.y < y)
        {
            return Direction.NORTHWEST;
        }
        if( neighbor.x < x && neighbor.y == y)
        {
            return Direction.WEST;
        }
        if( neighbor.x < x && neighbor.y > y)
        {
            return Direction.SOUTHWEST;
        }
        if( neighbor.x == x && neighbor.y > y)
        {
            return Direction.SOUTH;
        }
        
        return null;
    }
}
