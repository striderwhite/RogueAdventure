//an entity can change it position and contains information about the type of tile that it was on before it moved.

package com.game.rogueadventure;

import android.graphics.Rect;

import java.util.Random;

public class Entity
{
    int level;
    int x, y;
    int health, mana, attackStrength, experience, damage;
    int fovSize = 5;
    World world;
    Tile_Type type;
    Tile_Type typeBeforeEntityMoved; //this is the type of tile before the player was on it


    
    Entity(int x, int y, World world, Tile_Type type)
    {
        this.x = x;
        this.y = y;
        this.world = world;
        this.type = type;
        typeBeforeEntityMoved = world.tiles[x][y].type;
        world.tiles[x][y].type = type;
        
        //default stats
        level = 1;
        health = 50;
        mana = 50;
        attackStrength = 15;
        experience = 10;
    }
    
    public void act(Direction direction)
    {
    	move(direction); //the entity might not move
    }
      
    /*
     * 
     */
     public void move(Direction direction)
     {
         Tile tileToMoveOn = world.getTileFacing(x, y, direction);
         if(tileToMoveOn.isWall() || tileToMoveOn.isPlayer() || tileToMoveOn.isEnemy())
         {
        	 return;
         }
         else
         {
         	world.getTile(x,y).setType(typeBeforeEntityMoved);
        	switch(direction)
        	{
	        	case NORTH:
	        		y--;
	        		break;
	        		
				case EAST:
					x++;
					break;
					
				case NORTHEAST:
		             y--;
		             x++;
					break;
					
				case NORTHWEST:
		             y--;
		             x--;
					break;
					
				case SOUTH:
	                y++;
					break;
					
				case SOUTHEAST:
		             y++;
		             x++;
					break;
					
				case SOUTHWEST:
		             y++;
		             x--;
					break;
					
				case WEST:
	                x--;
					break;
					
				default:
					break;
        	}
            typeBeforeEntityMoved = world.getTile(x,y).getType();
        	world.getTile(x,y).setType(type);
         }  
         world.graphics.invalidate();
     }
     
     public void move(Tile tile)
     {
         if(tile.isTraversable())
         {
             x = tile.x;
             y = tile.y;
         }
     }
     
     public void attack(Entity e)
     {
         Random gen = new Random();
         int modifier = gen.nextInt(5)+1;
         damage = attackStrength - modifier;
         e.health = e.health - damage;
     }
     
     public Rect getFov()
     {
         int top = x - fovSize;
         int left = y - fovSize;
         int right = x +  fovSize;
         int bottom = y + fovSize;
         
         if(top < 0)
         {
             top = 0;
         }
         if(left < 0)
         {
             left = 0;
         }
         if(right > world.width)
         {
             right = world.width;
         }
         if(bottom > world.height)
         {
             bottom = world.height;
         }  
         
         return new Rect(top,left,right,bottom);
     }
     
     public int getX()
     {
         return x;
     }
     
     public int getY()
     {
         return y;
     }
     
     public boolean isAdjacent(Entity e)
     {
         if( x == e.getX() )
         {
             if( Math.abs(y - e.getY()) == 1)
             {
                 return true;
             }
         }
         if( y == e.getY() )
         {
             if( Math.abs( x - e.getX()) == 1)
             {
                 return true;
             }
         }
         
         return false;
     }
     
     public boolean isDead()
     {
         if(health <= 0)
         {
             return true;
         }
         else
             return false;
     }
}
