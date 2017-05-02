//an entity can change it position and contains information about the type of tile that it was on before it moved.

package com.game.rogueadventure;

import android.graphics.Rect;

import java.util.Random;

public class Entity
{
    int level;
    int x, y;
    int health, mana, attack_strength, experience, damage;
    int fovSize = 5;
    World world;
    Block_Type type;
    Block_Type typeBeforeEntityMoved; //this is the type of tile before the player was on it


    
    Entity(int x, int y, World world, Block_Type type)
    {
        this.x = x;
        this.y = y;
        this.world = world;
        this.type = type;
        typeBeforeEntityMoved = world.blocks[x][y].type;
        //world.blocks[x][y].type = type;
        
        //default stats
        level = 1;
        health = 50;
        mana = 50;
        attack_strength = 15;
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
         if(direction == null){ return; } 
         Block tileToMoveOn = world.getTileFacing(x, y, direction);
         if(tileToMoveOn.isWall() || tileToMoveOn.isPlayer() || tileToMoveOn.isEnemy())
         {
        	 return;
         }
         else
         {
         	world.getBlock(x,y).setType(typeBeforeEntityMoved);
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
            typeBeforeEntityMoved = world.getBlock(x,y).getType();
        	world.getBlock(x,y).setType(type);
         }  
         //world.graphics.invalidate();
     }
     
     public void move(Block block)
     {
         if(block.isTraversable())
         { 
             x = block.x;
             y = block.y;
         }
     }
     
     public void attack(Entity e, EventLog log)
     {
         
         
         int tile_modifier = getTileBuff(this);
         int opponent_tile_modifier = getTileBuff(e);
         int tentative_damage = attack_strength + tile_modifier + opponent_tile_modifier;
         if(tentative_damage < 0)
         {
             tentative_damage = 0;
         }
         e.health = e.health - tentative_damage;
         
         if(e.type == Block_Type.PLAYER) //the case that enemy is attacking player
         {
             /*if(e.typeBeforeEntityMoved == Tile_Type.GRASS || e.typeBeforeEntityMoved == Tile_Type.WATER || e.typeBeforeEntityMoved == Tile_Type.ICE)
             {
                 log.logText("combat" , "Enemy defensive buff: " + tile_modifier + " ");
             }
             else
             {
                 log.logText("combat" , "Enemy offensive buff: " + tile_modifier + " ");
             }
             
             if(typeBeforeEntityMoved == Tile_Type.GRASS || typeBeforeEntityMoved == Tile_Type.WATER || typeBeforeEntityMoved == Tile_Type.ICE)
             {
                 log.logText("combat" , "Player defensive buff: " + opponent_tile_modifier + " ");
             }
             else
             {
                 log.logText("combat" , "Player offensive buff: " + opponent_tile_modifier + " ");
             }*/
             
             log.logText("combat" , "Enemy -> Player " + tentative_damage + " damage ");
             
         }
         else
         {
             
             /*if(e.typeBeforeEntityMoved == Tile_Type.GRASS || e.typeBeforeEntityMoved == Tile_Type.WATER || e.typeBeforeEntityMoved == Tile_Type.ICE)
             {
                 log.logText("combat" , "Player defensive buff: " + tile_modifier + " ");
             }
             else
             {
                 log.logText("combat" , "Player offensive buff: " + tile_modifier + " ");
             }
             
             if(typeBeforeEntityMoved == Tile_Type.GRASS || typeBeforeEntityMoved == Tile_Type.WATER || typeBeforeEntityMoved == Tile_Type.ICE)
             {
                 log.logText("combat" , "Enemy defensive buff: " + opponent_tile_modifier + " ");
             }
             else
             {
                 log.logText("combat" , "Enemy offensive buff: " + opponent_tile_modifier + " ");
             }*/
             
             log.logText("combat" , "Player -> Enemy " + tentative_damage + " damage ");
         } 
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
     
     /*
      * Defensive Buffs:
      * -> Grass (1-5)
      * -> Water (1-10)
      * -> Ice (1-7)
      * 
      * Offensive Buffs:
      * -> Rock (1-8)
      * -> Metal (1-5)
      * 
      * Defensive buffs return a negative number
      * Offensive buffs return a positive number
      */
     private int getTileBuff(Entity e)
     {
         Random gen = new Random();
         switch(e.typeBeforeEntityMoved)
         {
            case ENEMY:
                break;
            case GRASS:
                return -(gen.nextInt(level + 5) + 1); //grass gets between 1 and 5 
            case ICE:
                return -(gen.nextInt(level + 10) + 1); //ice gets between 1 and 10
            case METAL:
                return (gen.nextInt(level + 5) + 1); //Metal gets between 1 and 5;
            case NONE:
                break;
            case PLAYER:
                break;
            case ROCK:
                return (gen.nextInt(level + 4) + 1); //Rock gets between 1 and 4;
            case UPSTAIRS:
                break;
            case DOWNSTAIRS:
                break;
            case WALL:
                break;
            case WATER:
                return -(gen.nextInt(level + 4) + 1); //grass gets between 1 and 4
            default:
                break;
             
         }
         return 0; //if failed
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
         if(Math.abs(x - e.getX()) == 1 && Math.abs(y - e.getY()) == 1)
         {
             return true;
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
     
     public void setWorld(World w)
     {
         world = w;
     }
}
