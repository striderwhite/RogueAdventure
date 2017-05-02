//an entity can change it position and contains information about the type of tile that it was on before it moved.

package com.game.rogueadventure;

public class Entity
{
    int x, y;
    World world;
    Game game;
    Tile_Type type;
    Tile_Type typeBeforeEntityMoved; //this is the type of tile before the player was on it


    
    Entity(int x, int y, World world, Game game, Tile_Type type)
    {
        this.x = x;
        this.y = y;
        this.world = world;
        this.game = game;
        this.type = type;
        typeBeforeEntityMoved = world.tiles[x][y].type;
        world.tiles[x][y].type = type;
    }
    
    public void act(Direction direction)
    {
    	move(direction); //the entity might not move
    	game.graphics.invalidate();
    }
      
    /*
     * 
     */
     public void move(Direction direction)
     {
         Tile tileToMoveOn = world.getTileFacing(x, y, direction);
         if(tileToMoveOn.isWall())
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
					
				case NORTH_EAST:
		             y++;
		             x++;
					break;
					
				case NORTH_WEST:
		             y++;
		             x--;
					break;
					
				case SOUTH:
	                y++;
					break;
					
				case SOUTH_EAST:
		             y--;
		             x++;
					break;
					
				case SOUTH_WEST:
		             y--;
		             x--;
					break;
					
				case WEST:
	                x--;
					break;
					
				default:
					break;
        	}
        	world.getTile(x,y).setType(type);
         }  
     }
}
