package com.game.rogueadventure;

import java.io.Serializable;
import java.util.Random;


public class Tile implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3930868888384229719L;
	Tile_Type type;
	boolean discovered = false;
      
    Tile() // this constructor chooses a type randomly
    { 
        Random gen = new Random();
        int num = gen.nextInt(4);
        
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
    
    Tile(Tile_Type type)
    {
        this.type = type;
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
}
